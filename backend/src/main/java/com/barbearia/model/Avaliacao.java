package com.barbearia.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Avaliacao {

    private String id;
    private String agendamentoId;
    private String clienteId;
    private int nota;
    private String comentario;
    private boolean validada;
    private LocalDateTime criadaEm;

    public Avaliacao() {
        this.id = UUID.randomUUID().toString();
        this.validada = false;
        this.criadaEm = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAgendamentoId() { return agendamentoId; }
    public void setAgendamentoId(String agendamentoId) { this.agendamentoId = agendamentoId; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public boolean isValidada() { return validada; }
    public void setValidada(boolean validada) { this.validada = validada; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public void setCriadaEm(LocalDateTime criadaEm) { this.criadaEm = criadaEm; }
}
