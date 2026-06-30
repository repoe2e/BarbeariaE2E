package com.barbearia.repository;

import com.barbearia.model.*;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryStore {

    private final Map<String, Cliente> clientes = new ConcurrentHashMap<>();
    private final Map<String, Barbeiro> barbeiros = new ConcurrentHashMap<>();
    private final Map<String, Agendamento> agendamentos = new ConcurrentHashMap<>();
    private final Map<String, Avaliacao> avaliacoes = new ConcurrentHashMap<>();
    private final Map<String, Cancelamento> cancelamentos = new ConcurrentHashMap<>();
    private final Map<TipoServico, Servico> servicos = new ConcurrentHashMap<>();
    private ContatoInfo contatoInfo;
    private final Set<String> tokensAdmin = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void init() {
        servicos.put(TipoServico.CORTE_CABELO, new Servico(TipoServico.CORTE_CABELO, "Corte de Cabelo", 100.0));
        servicos.put(TipoServico.CORTE_BARBA, new Servico(TipoServico.CORTE_BARBA, "Corte de Barba", 100.0));
        servicos.put(TipoServico.COMBO, new Servico(TipoServico.COMBO, "Combo", 180.0));

        Barbeiro b1 = new Barbeiro("Carlos Silva", "Cortes clássicos");
        Barbeiro b2 = new Barbeiro("João Mendes", "Barba e degradê");
        Barbeiro b3 = new Barbeiro("Pedro Santos", "Combo premium");
        barbeiros.put(b1.getId(), b1);
        barbeiros.put(b2.getId(), b2);
        barbeiros.put(b3.getId(), b3);

        contatoInfo = new ContatoInfo(
                "Barbearia E2E",
                "Rua das Tesouras, 123 - Centro, São Paulo - SP",
                "(11) 3456-7890",
                "(11) 98765-4321",
                "contato@barbeariae2e.com.br",
                "@barbeariae2e",
                "Segunda a Sábado: 09:00 às 20:00"
        );
    }

    // Clientes
    public Cliente salvarCliente(Cliente cliente) { clientes.put(cliente.getId(), cliente); return cliente; }
    public Optional<Cliente> buscarCliente(String id) { return Optional.ofNullable(clientes.get(id)); }
    public Optional<Cliente> buscarClientePorWhatsapp(String whatsapp) {
        return clientes.values().stream().filter(c -> c.getWhatsapp().equals(whatsapp)).findFirst();
    }
    public List<Cliente> listarClientes() { return new ArrayList<>(clientes.values()); }

    // Barbeiros
    public Barbeiro salvarBarbeiro(Barbeiro barbeiro) { barbeiros.put(barbeiro.getId(), barbeiro); return barbeiro; }
    public Optional<Barbeiro> buscarBarbeiro(String id) { return Optional.ofNullable(barbeiros.get(id)); }
    public List<Barbeiro> listarBarbeiros() { return new ArrayList<>(barbeiros.values()); }
    public List<Barbeiro> buscarBarbeirosPorNome(String nome) {
        String termo = nome.toLowerCase();
        return barbeiros.values().stream()
                .filter(b -> b.getNome().toLowerCase().contains(termo))
                .collect(Collectors.toList());
    }

    // Agendamentos
    public Agendamento salvarAgendamento(Agendamento ag) { agendamentos.put(ag.getId(), ag); return ag; }
    public Optional<Agendamento> buscarAgendamento(String id) { return Optional.ofNullable(agendamentos.get(id)); }
    public List<Agendamento> listarAgendamentos() { return new ArrayList<>(agendamentos.values()); }
    public List<Agendamento> listarAgendamentosPorCliente(String clienteId) {
        return agendamentos.values().stream()
                .filter(a -> a.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
    }
    public List<Agendamento> listarAgendamentosPorBarbeiroEHorario(String barbeiroId, java.time.LocalDateTime inicio, java.time.LocalDateTime fim) {
        return agendamentos.values().stream()
                .filter(a -> a.getBarbeiroId().equals(barbeiroId))
                .filter(a -> a.getStatus() == StatusAgendamento.AGENDADO)
                .filter(a -> !a.getDataHora().isBefore(inicio) && a.getDataHora().isBefore(fim))
                .collect(Collectors.toList());
    }

    // Avaliações
    public Avaliacao salvarAvaliacao(Avaliacao av) { avaliacoes.put(av.getId(), av); return av; }
    public Optional<Avaliacao> buscarAvaliacao(String id) { return Optional.ofNullable(avaliacoes.get(id)); }
    public List<Avaliacao> listarAvaliacoes() { return new ArrayList<>(avaliacoes.values()); }

    // Cancelamentos
    public Cancelamento salvarCancelamento(Cancelamento c) { cancelamentos.put(c.getId(), c); return c; }
    public List<Cancelamento> listarCancelamentos() { return new ArrayList<>(cancelamentos.values()); }

    // Serviços
    public List<Servico> listarServicos() { return new ArrayList<>(servicos.values()); }
    public Optional<Servico> buscarServico(TipoServico tipo) { return Optional.ofNullable(servicos.get(tipo)); }
    public Servico atualizarServico(Servico servico) { servicos.put(servico.getTipo(), servico); return servico; }

    // Contato
    public ContatoInfo getContatoInfo() { return contatoInfo; }

    // Admin tokens
    public void adicionarToken(String token) { tokensAdmin.add(token); }
    public boolean validarToken(String token) { return tokensAdmin.contains(token); }
    public void removerToken(String token) { tokensAdmin.remove(token); }
}
