package com.barbearia.controller;

import com.barbearia.model.ContatoInfo;
import com.barbearia.repository.InMemoryStore;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@Tag(name = "Público", description = "Informações públicas da barbearia")
public class PublicController {

    private final InMemoryStore store;

    public PublicController(InMemoryStore store) {
        this.store = store;
    }

    @GetMapping("/contato")
    public ContatoInfo contato() {
        return store.getContatoInfo();
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("nome", "Barbearia E2E");
        info.put("descricao", "Estilo, tradição e atendimento premium");
        info.put("servicos", store.listarServicos());
        info.put("barbeiros", store.listarBarbeiros().stream().filter(b -> b.isAtivo()).toList());
        return info;
    }
}
