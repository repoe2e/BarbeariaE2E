package com.barbearia.dto;

import com.barbearia.model.*;
import java.time.LocalDateTime;

public class AgendamentoResponse {

    private String id;
    private String clienteId;
    private String clienteNome;
    private String clienteWhatsapp;
    private String barbeiroId;
    private String barbeiroNome;
    private TipoServico tipoServico;
    private String servicoNome;
    private FormaPagamento formaPagamento;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private double valor;
    private boolean podeEditar;
    private boolean podeCancelar;

    public static AgendamentoResponse from(Agendamento ag, Cliente cliente, Barbeiro barbeiro, Servico servico, boolean podeAlterar) {
        AgendamentoResponse r = new AgendamentoResponse();
        r.id = ag.getId();
        r.clienteId = ag.getClienteId();
        r.clienteNome = cliente != null ? cliente.getNome() : null;
        r.clienteWhatsapp = cliente != null ? cliente.getWhatsapp() : null;
        r.barbeiroId = ag.getBarbeiroId();
        r.barbeiroNome = barbeiro != null ? barbeiro.getNome() : null;
        r.tipoServico = ag.getTipoServico();
        r.servicoNome = servico != null ? servico.getNome() : ag.getTipoServico().getDescricao();
        r.formaPagamento = ag.getFormaPagamento();
        r.dataHora = ag.getDataHora();
        r.status = ag.getStatus();
        r.valor = ag.getValor();
        r.podeEditar = podeAlterar;
        r.podeCancelar = podeAlterar;
        return r;
    }

    public String getId() { return id; }
    public String getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public String getClienteWhatsapp() { return clienteWhatsapp; }
    public String getBarbeiroId() { return barbeiroId; }
    public String getBarbeiroNome() { return barbeiroNome; }
    public TipoServico getTipoServico() { return tipoServico; }
    public String getServicoNome() { return servicoNome; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public LocalDateTime getDataHora() { return dataHora; }
    public StatusAgendamento getStatus() { return status; }
    public double getValor() { return valor; }
    public boolean isPodeEditar() { return podeEditar; }
    public boolean isPodeCancelar() { return podeCancelar; }
}
