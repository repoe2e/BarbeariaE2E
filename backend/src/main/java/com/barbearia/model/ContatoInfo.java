package com.barbearia.model;

public class ContatoInfo {

    private String nome;
    private String endereco;
    private String telefone;
    private String whatsapp;
    private String email;
    private String instagram;
    private String horarioFuncionamento;

    public ContatoInfo() {}

    public ContatoInfo(String nome, String endereco, String telefone, String whatsapp,
                       String email, String instagram, String horarioFuncionamento) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.whatsapp = whatsapp;
        this.email = email;
        this.instagram = instagram;
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getInstagram() { return instagram; }
    public void setInstagram(String instagram) { this.instagram = instagram; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) { this.horarioFuncionamento = horarioFuncionamento; }
}
