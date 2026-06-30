package com.barbearia.dto;

import com.barbearia.model.FormaPagamento;
import com.barbearia.model.TipoServico;
import jakarta.validation.constraints.*;

public class AgendamentoRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String whatsapp;

    @Min(1) @Max(31)
    private int diaNascimento;

    @Min(1) @Max(12)
    private int mesNascimento;

    @NotNull
    private TipoServico tipoServico;

    @NotNull
    private FormaPagamento formaPagamento;

    @NotBlank
    private String barbeiroId;

    @NotBlank
    private String data;

    @NotBlank
    private String hora;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
    public int getDiaNascimento() { return diaNascimento; }
    public void setDiaNascimento(int diaNascimento) { this.diaNascimento = diaNascimento; }
    public int getMesNascimento() { return mesNascimento; }
    public void setMesNascimento(int mesNascimento) { this.mesNascimento = mesNascimento; }
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
