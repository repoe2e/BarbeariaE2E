package com.barbearia.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AvaliacaoRequest {

    @NotBlank
    private String agendamentoId;

    @NotNull @Min(1) @Max(5)
    private Integer nota;

    private String comentario;

    public String getAgendamentoId() { return agendamentoId; }
    public void setAgendamentoId(String agendamentoId) { this.agendamentoId = agendamentoId; }
    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
