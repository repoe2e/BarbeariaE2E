package com.barbearia.model;

import java.util.UUID;

public class Barbeiro {

    private String id;
    private String nome;
    private String especialidade;
    private boolean ativo;

    public Barbeiro() {
        this.id = UUID.randomUUID().toString();
        this.ativo = true;
    }

    public Barbeiro(String nome, String especialidade) {
        this();
        this.nome = nome;
        this.especialidade = especialidade;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
