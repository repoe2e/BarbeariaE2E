package com.barbearia.dto;

public class RankingBarbeiroResponse {

    private String barbeiroId;
    private String barbeiroNome;
    private double mediaNota;
    private int totalAvaliacoes;
    private int totalAtendimentos;

    public RankingBarbeiroResponse(String barbeiroId, String barbeiroNome, double mediaNota, int totalAvaliacoes, int totalAtendimentos) {
        this.barbeiroId = barbeiroId;
        this.barbeiroNome = barbeiroNome;
        this.mediaNota = mediaNota;
        this.totalAvaliacoes = totalAvaliacoes;
        this.totalAtendimentos = totalAtendimentos;
    }

    public String getBarbeiroId() { return barbeiroId; }
    public String getBarbeiroNome() { return barbeiroNome; }
    public double getMediaNota() { return mediaNota; }
    public int getTotalAvaliacoes() { return totalAvaliacoes; }
    public int getTotalAtendimentos() { return totalAtendimentos; }
}
