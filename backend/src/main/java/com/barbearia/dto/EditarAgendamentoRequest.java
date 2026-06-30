package com.barbearia.dto;

import com.barbearia.model.FormaPagamento;
import com.barbearia.model.TipoServico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EditarAgendamentoRequest {

    private TipoServico tipoServico;
    private FormaPagamento formaPagamento;
    private String barbeiroId;

    @NotBlank
    private String data;

    @NotBlank
    private String hora;

    public TipoServico getTipoServico() { return tipoServico; }
    public void setTipoServico(TipoServico tipoServico) { this.tipoServico = tipoServico; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }
    public String getBarbeiroId() { return barbeiroId; }
    public void setBarbeiroId(String barbeiroId) { this.barbeiroId = barbeiroId; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}
