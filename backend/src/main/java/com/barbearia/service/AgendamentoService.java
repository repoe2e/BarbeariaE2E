package com.barbearia.service;

import com.barbearia.dto.*;
import com.barbearia.exception.BusinessException;
import com.barbearia.model.*;
import com.barbearia.repository.InMemoryStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    private static final LocalTime HORA_INICIO = LocalTime.of(9, 0);
    private static final LocalTime HORA_FIM = LocalTime.of(20, 0);

    private final InMemoryStore store;

    public AgendamentoService(InMemoryStore store) {
        this.store = store;
    }

    public AgendamentoResponse criar(AgendamentoRequest req) {
        LocalDateTime dataHora = parseDataHora(req.getData(), req.getHora());
        validarHorario(dataHora);
        validarBarbeiro(req.getBarbeiroId());
        validarDisponibilidade(req.getBarbeiroId(), dataHora, null);

        Cliente cliente = store.buscarClientePorWhatsapp(req.getWhatsapp())
                .orElseGet(() -> {
                    Cliente novo = new Cliente(req.getNome(), req.getWhatsapp(), req.getDiaNascimento(), req.getMesNascimento());
                    return store.salvarCliente(novo);
                });

        if (!cliente.getNome().equals(req.getNome())) {
            cliente.setNome(req.getNome());
            cliente.setDiaNascimento(req.getDiaNascimento());
            cliente.setMesNascimento(req.getMesNascimento());
            store.salvarCliente(cliente);
        }

        Servico servico = store.buscarServico(req.getTipoServico())
                .orElseThrow(() -> new BusinessException("Serviço não encontrado"));

        if (!servico.isAtivo()) {
            throw new BusinessException("Serviço indisponível no momento");
        }

        Agendamento ag = new Agendamento();
        ag.setClienteId(cliente.getId());
        ag.setBarbeiroId(req.getBarbeiroId());
        ag.setTipoServico(req.getTipoServico());
        ag.setFormaPagamento(req.getFormaPagamento());
        ag.setDataHora(dataHora);
        ag.setValor(servico.getPreco());

        store.salvarAgendamento(ag);
        return toResponse(ag);
    }

    public AgendamentoResponse editar(String id, EditarAgendamentoRequest req) {
        Agendamento ag = buscarAgendamentoAtivo(id);
        validarPrazoAlteracao(ag.getDataHora());

        LocalDateTime novaDataHora = parseDataHora(req.getData(), req.getHora());
        validarHorario(novaDataHora);

        String barbeiroId = req.getBarbeiroId() != null ? req.getBarbeiroId() : ag.getBarbeiroId();
        validarBarbeiro(barbeiroId);
        validarDisponibilidade(barbeiroId, novaDataHora, id);

        if (req.getTipoServico() != null) {
            Servico servico = store.buscarServico(req.getTipoServico())
                    .orElseThrow(() -> new BusinessException("Serviço não encontrado"));
            ag.setTipoServico(req.getTipoServico());
            ag.setValor(servico.getPreco());
        }

        if (req.getFormaPagamento() != null) {
            ag.setFormaPagamento(req.getFormaPagamento());
        }

        ag.setBarbeiroId(barbeiroId);
        ag.setDataHora(novaDataHora);
        store.salvarAgendamento(ag);
        return toResponse(ag);
    }

    public AgendamentoResponse cancelar(String id, String motivo) {
        Agendamento ag = buscarAgendamentoAtivo(id);
        validarPrazoAlteracao(ag.getDataHora());

        ag.setStatus(StatusAgendamento.CANCELADO);
        store.salvarAgendamento(ag);

        Cancelamento cancelamento = new Cancelamento();
        cancelamento.setAgendamentoId(ag.getId());
        cancelamento.setClienteId(ag.getClienteId());
        cancelamento.setMotivo(motivo != null ? motivo : "Cancelado pelo cliente");
        cancelamento.setDataHoraAgendamento(ag.getDataHora());
        cancelamento.setValor(ag.getValor());
        store.salvarCancelamento(cancelamento);

        return toResponse(ag);
    }

    public List<AgendamentoResponse> listarTodos() {
        return store.listarAgendamentos().stream()
                .sorted(Comparator.comparing(Agendamento::getDataHora))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponse> listarPorWhatsapp(String whatsapp) {
        return store.buscarClientePorWhatsapp(whatsapp)
                .map(c -> store.listarAgendamentosPorCliente(c.getId()).stream()
                        .sorted(Comparator.comparing(Agendamento::getDataHora).reversed())
                        .map(this::toResponse)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    public AgendamentoResponse buscarPorId(String id) {
        return store.buscarAgendamento(id)
                .map(this::toResponse)
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado"));
    }

    public List<String> horariosDisponiveis(String barbeiroId, String data) {
        validarBarbeiro(barbeiroId);
        LocalDate dia = LocalDate.parse(data);
        List<String> horarios = new ArrayList<>();

        for (int h = 9; h <= 19; h++) {
            LocalDateTime slot = LocalDateTime.of(dia, LocalTime.of(h, 0));
            if (slot.isBefore(LocalDateTime.now())) continue;
            try {
                validarDisponibilidade(barbeiroId, slot, null);
                horarios.add(String.format("%02d:00", h));
            } catch (BusinessException ignored) {}
        }
        return horarios;
    }

    public void concluir(String id) {
        Agendamento ag = store.buscarAgendamento(id)
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado"));
        ag.setStatus(StatusAgendamento.CONCLUIDO);
        store.salvarAgendamento(ag);
    }

    private Agendamento buscarAgendamentoAtivo(String id) {
        Agendamento ag = store.buscarAgendamento(id)
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado"));
        if (ag.getStatus() != StatusAgendamento.AGENDADO) {
            throw new BusinessException("Agendamento não está ativo");
        }
        return ag;
    }

    private void validarPrazoAlteracao(LocalDateTime dataHora) {
        if (LocalDateTime.now().plusHours(2).isAfter(dataHora)) {
            throw new BusinessException("Alteração permitida apenas até 2 horas antes do horário");
        }
    }

    private void validarHorario(LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é possível agendar no passado");
        }
        LocalTime hora = dataHora.toLocalTime();
        if (hora.isBefore(HORA_INICIO) || hora.isAfter(HORA_FIM.minusHours(1))) {
            throw new BusinessException("Horário deve ser entre 09:00 e 20:00");
        }
        if (hora.getMinute() != 0) {
            throw new BusinessException("Agendamentos em horários cheios (ex: 09:00, 10:00)");
        }
    }

    private void validarBarbeiro(String barbeiroId) {
        Barbeiro b = store.buscarBarbeiro(barbeiroId)
                .orElseThrow(() -> new BusinessException("Barbeiro não encontrado"));
        if (!b.isAtivo()) {
            throw new BusinessException("Barbeiro indisponível");
        }
    }

    private void validarDisponibilidade(String barbeiroId, LocalDateTime dataHora, String excluirId) {
        boolean ocupado = store.listarAgendamentos().stream()
                .filter(a -> a.getStatus() == StatusAgendamento.AGENDADO)
                .filter(a -> excluirId == null || !a.getId().equals(excluirId))
                .anyMatch(a -> a.getBarbeiroId().equals(barbeiroId)
                        && a.getDataHora().equals(dataHora));
        if (ocupado) {
            throw new BusinessException("Horário indisponível para este barbeiro");
        }
    }

    private LocalDateTime parseDataHora(String data, String hora) {
        try {
            LocalDate d = LocalDate.parse(data);
            LocalTime t = LocalTime.parse(hora.length() == 5 ? hora : hora + ":00");
            return LocalDateTime.of(d, t);
        } catch (Exception e) {
            throw new BusinessException("Data ou hora inválida");
        }
    }

    private AgendamentoResponse toResponse(Agendamento ag) {
        Cliente cliente = store.buscarCliente(ag.getClienteId()).orElse(null);
        Barbeiro barbeiro = store.buscarBarbeiro(ag.getBarbeiroId()).orElse(null);
        Servico servico = store.buscarServico(ag.getTipoServico()).orElse(null);
        boolean podeAlterar = ag.getStatus() == StatusAgendamento.AGENDADO
                && LocalDateTime.now().plusHours(2).isBefore(ag.getDataHora());
        return AgendamentoResponse.from(ag, cliente, barbeiro, servico, podeAlterar);
    }
}
