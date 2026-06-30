package com.barbearia.dto;

public class FaturamentoResponse {

    private double realizado;
    private double previsto;
    private int mes;
    private int ano;

    public FaturamentoResponse(double realizado, double previsto, int mes, int ano) {
        this.realizado = realizado;
        this.previsto = previsto;
        this.mes = mes;
        this.ano = ano;
    }

    public double getRealizado() { return realizado; }
    public double getPrevisto() { return previsto; }
    public int getMes() { return mes; }
    public int getAno() { return ano; }
}
