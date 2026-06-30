package com.barbearia.model;

public class Servico {

    private TipoServico tipo;
    private String nome;
    private double preco;
    private boolean ativo;

    public Servico() {}

    public Servico(TipoServico tipo, String nome, double preco) {
        this.tipo = tipo;
        this.nome = nome;
        this.preco = preco;
        this.ativo = true;
    }

    public TipoServico getTipo() { return tipo; }
    public void setTipo(TipoServico tipo) { this.tipo = tipo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
