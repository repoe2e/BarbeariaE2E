package com.barbearia.controller;

import com.barbearia.dto.*;
import com.barbearia.model.Barbeiro;
import com.barbearia.service.AgendamentoService;
import com.barbearia.service.AvaliacaoService;
import com.barbearia.repository.InMemoryStore;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos", description = "Agendamento, horários, serviços e avaliações do cliente")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final AvaliacaoService avaliacaoService;
    private final InMemoryStore store;

    public AgendamentoController(AgendamentoService agendamentoService, AvaliacaoService avaliacaoService, InMemoryStore store) {
        this.agendamentoService = agendamentoService;
        this.avaliacaoService = avaliacaoService;
        this.store = store;
    }

    @PostMapping
    public AgendamentoResponse criar(@Valid @RequestBody AgendamentoRequest request) {
        return agendamentoService.criar(request);
    }

    @GetMapping("/{id}")
    public AgendamentoResponse buscar(@PathVariable String id) {
        return agendamentoService.buscarPorId(id);
    }

    @GetMapping("/cliente/{whatsapp}")
    public List<AgendamentoResponse> listarPorCliente(@PathVariable String whatsapp) {
        return agendamentoService.listarPorWhatsapp(whatsapp);
    }

    @PutMapping("/{id}")
    public AgendamentoResponse editar(@PathVariable String id, @Valid @RequestBody EditarAgendamentoRequest request) {
        return agendamentoService.editar(id, request);
    }

    @DeleteMapping("/{id}")
    public AgendamentoResponse cancelar(@PathVariable String id, @RequestBody(required = false) Map<String, String> body) {
        String motivo = body != null ? body.get("motivo") : null;
        return agendamentoService.cancelar(id, motivo);
    }

    @GetMapping("/horarios")
    public List<String> horarios(@RequestParam String barbeiroId, @RequestParam String data) {
        return agendamentoService.horariosDisponiveis(barbeiroId, data);
    }

    @GetMapping("/barbeiros")
    public List<Barbeiro> barbeiros() {
        return store.listarBarbeiros().stream().filter(Barbeiro::isAtivo).toList();
    }

    @GetMapping("/servicos")
    public Object servicos() {
        return store.listarServicos().stream().filter(s -> s.isAtivo()).toList();
    }

    @PostMapping("/avaliacoes")
    public Object avaliar(@Valid @RequestBody AvaliacaoRequest request) {
        return avaliacaoService.criar(request);
    }

    @GetMapping("/avaliacoes/media")
    public Map<String, Double> mediaAvaliacoes() {
        return Map.of("media", avaliacaoService.mediaNotas());
    }
}
