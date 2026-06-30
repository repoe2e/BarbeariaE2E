package com.barbearia.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Agendamento {

    private String id;
    private String clienteId;
    private String barbeiroId;
    private TipoServico tipoServico;
    private FormaPagamento formaPagamento;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private double valor;
    private LocalDateTime criadoEm;

    public Agendamento() {
        this.id = UUID.randomUUID().toString();
        this.status = StatusAgendamento.AGENDADO;
        this.criadoEm = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public String getBarbeiroId() { return barbeiroId; }
    public void setBarbeiroId(String barbeiroId) { this.barbeiroId = barbeiroId; }
    public TipoServico getTipoServico() { return tipoServico; }
    public void setTipoServico(TipoServico tipoServico) { this.tipoServico = tipoServico; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
