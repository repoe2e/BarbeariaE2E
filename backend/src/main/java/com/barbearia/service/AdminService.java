package com.barbearia.service;

import com.barbearia.dto.AvaliacaoRequest;
import com.barbearia.dto.FaturamentoResponse;
import com.barbearia.dto.RankingBarbeiroResponse;
import com.barbearia.exception.BusinessException;
import com.barbearia.model.*;
import com.barbearia.repository.InMemoryStore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    private final InMemoryStore store;

    public AdminService(InMemoryStore store) {
        this.store = store;
    }

    public Map<String, String> login(String usuario, String senha) {
        if (!ADMIN_USER.equals(usuario) || !ADMIN_PASS.equals(senha)) {
            throw new BusinessException("Usuário ou senha inválidos");
        }
        String token = UUID.randomUUID().toString();
        store.adicionarToken(token);
        Map<String, String> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("usuario", usuario);
        return resp;
    }

    public void logout(String token) {
        store.removerToken(token);
    }

    public void validarToken(String token) {
        if (token == null || !store.validarToken(token)) {
            throw new BusinessException("Não autorizado");
        }
    }

    public List<Cliente> listarClientes() {
        return store.listarClientes();
    }

    public List<Servico> listarServicos() {
        return store.listarServicos();
    }

    public Servico atualizarServico(TipoServico tipo, Servico dados) {
        Servico servico = store.buscarServico(tipo)
                .orElseThrow(() -> new BusinessException("Serviço não encontrado"));
        if (dados.getNome() != null) servico.setNome(dados.getNome());
        if (dados.getPreco() > 0) servico.setPreco(dados.getPreco());
        servico.setAtivo(dados.isAtivo());
        return store.atualizarServico(servico);
    }

    public Barbeiro criarBarbeiro(Barbeiro barbeiro) {
        return store.salvarBarbeiro(barbeiro);
    }

    public Barbeiro editarBarbeiro(String id, Barbeiro dados) {
        Barbeiro barbeiro = store.buscarBarbeiro(id)
                .orElseThrow(() -> new BusinessException("Barbeiro não encontrado"));
        if (dados.getNome() != null) barbeiro.setNome(dados.getNome());
        if (dados.getEspecialidade() != null) barbeiro.setEspecialidade(dados.getEspecialidade());
        barbeiro.setAtivo(dados.isAtivo());
        return store.salvarBarbeiro(barbeiro);
    }

    public void desativarBarbeiro(String id) {
        Barbeiro barbeiro = store.buscarBarbeiro(id)
                .orElseThrow(() -> new BusinessException("Barbeiro não encontrado"));
        barbeiro.setAtivo(false);
        store.salvarBarbeiro(barbeiro);
    }

    public List<Barbeiro> listarBarbeiros() {
        return store.listarBarbeiros();
    }

    public List<Barbeiro> buscarBarbeiros(String nome) {
        if (nome == null || nome.isBlank()) return listarBarbeiros();
        return store.buscarBarbeirosPorNome(nome);
    }

    public FaturamentoResponse faturamentoMensal(int mes, int ano) {
        YearMonth ym = YearMonth.of(ano, mes);

        double realizado = store.listarAgendamentos().stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO)
                .filter(a -> YearMonth.from(a.getDataHora()).equals(ym))
                .mapToDouble(Agendamento::getValor)
                .sum();

        double previsto = store.listarAgendamentos().stream()
                .filter(a -> a.getStatus() == StatusAgendamento.AGENDADO || a.getStatus() == StatusAgendamento.CONCLUIDO)
                .filter(a -> YearMonth.from(a.getDataHora()).equals(ym))
                .mapToDouble(Agendamento::getValor)
                .sum();

        return new FaturamentoResponse(realizado, previsto, mes, ano);
    }

    public List<Cancelamento> historicoCancelamentos() {
        return store.listarCancelamentos().stream()
                .sorted(Comparator.comparing(Cancelamento::getCanceladoEm).reversed())
                .collect(Collectors.toList());
    }

    public Avaliacao validarAvaliacao(String id) {
        Avaliacao av = store.buscarAvaliacao(id)
                .orElseThrow(() -> new BusinessException("Avaliação não encontrada"));
        av.setValidada(true);
        return store.salvarAvaliacao(av);
    }

    public List<Avaliacao> listarAvaliacoes() {
        return store.listarAvaliacoes().stream()
                .sorted(Comparator.comparing(Avaliacao::getCriadaEm).reversed())
                .collect(Collectors.toList());
    }

    public List<RankingBarbeiroResponse> rankingBarbeiros() {
        Map<String, List<Integer>> notasPorBarbeiro = new HashMap<>();
        Map<String, Integer> atendimentosPorBarbeiro = new HashMap<>();

        for (Agendamento ag : store.listarAgendamentos()) {
            if (ag.getStatus() == StatusAgendamento.CONCLUIDO) {
                atendimentosPorBarbeiro.merge(ag.getBarbeiroId(), 1, Integer::sum);
            }
        }

        for (Avaliacao av : store.listarAvaliacoes()) {
            if (!av.isValidada()) continue;
            store.buscarAgendamento(av.getAgendamentoId()).ifPresent(ag -> {
                notasPorBarbeiro.computeIfAbsent(ag.getBarbeiroId(), k -> new ArrayList<>()).add(av.getNota());
            });
        }

        return store.listarBarbeiros().stream()
                .map(b -> {
                    List<Integer> notas = notasPorBarbeiro.getOrDefault(b.getId(), List.of());
                    double media = notas.isEmpty() ? 0 : notas.stream().mapToInt(Integer::intValue).average().orElse(0);
                    int atendimentos = atendimentosPorBarbeiro.getOrDefault(b.getId(), 0);
                    return new RankingBarbeiroResponse(b.getId(), b.getNome(), Math.round(media * 10.0) / 10.0, notas.size(), atendimentos);
                })
                .sorted(Comparator.comparingDouble(RankingBarbeiroResponse::getMediaNota).reversed()
                        .thenComparing(Comparator.comparingInt(RankingBarbeiroResponse::getTotalAtendimentos).reversed()))
                .collect(Collectors.toList());
    }
}
