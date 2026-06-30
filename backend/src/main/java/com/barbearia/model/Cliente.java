package com.barbearia.model;

import java.util.UUID;

public class Cliente {

    private String id;
    private String nome;
    private String whatsapp;
    private int diaNascimento;
    private int mesNascimento;

    public Cliente() {
        this.id = UUID.randomUUID().toString();
    }

    public Cliente(String nome, String whatsapp, int diaNascimento, int mesNascimento) {
        this();
        this.nome = nome;
        this.whatsapp = whatsapp;
        this.diaNascimento = diaNascimento;
        this.mesNascimento = mesNascimento;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
    public int getDiaNascimento() { return diaNascimento; }
    public void setDiaNascimento(int diaNascimento) { this.diaNascimento = diaNascimento; }
    public int getMesNascimento() { return mesNascimento; }
    public void setMesNascimento(int mesNascimento) { this.mesNascimento = mesNascimento; }
}
