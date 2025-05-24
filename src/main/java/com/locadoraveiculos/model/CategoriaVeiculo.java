package com.locadoraveiculos.model;

import java.math.BigDecimal;

/**
 * Representa a entidade CategoriaVeiculo.
 */
public class CategoriaVeiculo {
    private int idCategoriaVeiculo;
    private String nomeCategoria;
    private String descricao;
    private BigDecimal valorDiariaBase;

    public CategoriaVeiculo() {
    }

    public CategoriaVeiculo(String nomeCategoria, BigDecimal valorDiariaBase) {
        this.nomeCategoria = nomeCategoria;
        this.valorDiariaBase = valorDiariaBase;
    }

    // Getters e Setters
    public int getIdCategoriaVeiculo() {
        return idCategoriaVeiculo;
    }

    public void setIdCategoriaVeiculo(int idCategoriaVeiculo) {
        this.idCategoriaVeiculo = idCategoriaVeiculo;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorDiariaBase() {
        return valorDiariaBase;
    }

    public void setValorDiariaBase(BigDecimal valorDiariaBase) {
        this.valorDiariaBase = valorDiariaBase;
    }

    @Override
    public String toString() {
        return "Categoria ID: " + idCategoriaVeiculo +
               ", Nome: '" + nomeCategoria + '\'' +
               ", Valor Diária Base: " + valorDiariaBase;
    }
}