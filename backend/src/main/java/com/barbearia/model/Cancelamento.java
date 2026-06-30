package com.barbearia.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Cancelamento {

    private String id;
    private String agendamentoId;
    private String clienteId;
    private String motivo;
    private LocalDateTime dataHoraAgendamento;
    private double valor;
    private LocalDateTime canceladoEm;

    public Cancelamento() {
        this.id = UUID.randomUUID().toString();
        this.canceladoEm = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAgendamentoId() { return agendamentoId; }
    public void setAgendamentoId(String agendamentoId) { this.agendamentoId = agendamentoId; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getDataHoraAgendamento() { return dataHoraAgendamento; }
    public void setDataHoraAgendamento(LocalDateTime dataHoraAgendamento) { this.dataHoraAgendamento = dataHoraAgendamento; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public LocalDateTime getCanceladoEm() { return canceladoEm; }
    public void setCanceladoEm(LocalDateTime canceladoEm) { this.canceladoEm = canceladoEm; }
}
