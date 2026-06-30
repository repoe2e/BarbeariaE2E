package com.barbearia.service;

import com.barbearia.dto.AvaliacaoRequest;
import com.barbearia.exception.BusinessException;
import com.barbearia.model.Avaliacao;
import com.barbearia.model.StatusAgendamento;
import com.barbearia.repository.InMemoryStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    private final InMemoryStore store;

    public AvaliacaoService(InMemoryStore store) {
        this.store = store;
    }

    public Avaliacao criar(AvaliacaoRequest req) {
        var ag = store.buscarAgendamento(req.getAgendamentoId())
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado"));

        if (ag.getStatus() != StatusAgendamento.CONCLUIDO && ag.getStatus() != StatusAgendamento.AGENDADO) {
            throw new BusinessException("Agendamento não pode ser avaliado");
        }

        boolean jaAvaliou = store.listarAvaliacoes().stream()
                .anyMatch(a -> a.getAgendamentoId().equals(req.getAgendamentoId()));
        if (jaAvaliou) {
            throw new BusinessException("Este agendamento já foi avaliado");
        }

        Avaliacao av = new Avaliacao();
        av.setAgendamentoId(req.getAgendamentoId());
        av.setClienteId(ag.getClienteId());
        av.setNota(req.getNota());
        av.setComentario(req.getComentario());
        return store.salvarAvaliacao(av);
    }

    public List<Avaliacao> listarValidadas() {
        return store.listarAvaliacoes().stream()
                .filter(Avaliacao::isValidada)
                .collect(Collectors.toList());
    }

    public double mediaNotas() {
        return store.listarAvaliacoes().stream()
                .filter(Avaliacao::isValidada)
                .mapToInt(Avaliacao::getNota)
                .average()
                .orElse(0);
    }
}
