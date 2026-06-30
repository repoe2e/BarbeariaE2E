package com.barbearia.model;

public enum TipoServico {
    CORTE_CABELO("Corte de Cabelo"),
    CORTE_BARBA("Corte de Barba"),
    COMBO("Combo");

    private final String descricao;

    TipoServico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
