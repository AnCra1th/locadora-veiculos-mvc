package com.locadoraveiculos.model;


public class Veiculo {
    private String placa;
    private int idCategoriaVeiculo;
    private String modelo;
    private String marca;
    private Integer anoFabricacao; 
    private String cor;
    private String chassi;
    private String renavam;
    private String statusVeiculo;
    private String observacoes;

    public Veiculo() {
    }

    public Veiculo(String placa, int idCategoriaVeiculo, String modelo, String marca, String statusVeiculo) {
        this.placa = placa;
        this.idCategoriaVeiculo = idCategoriaVeiculo;
        this.modelo = modelo;
        this.marca = marca;
        this.statusVeiculo = statusVeiculo;
    }

    // Getters e Setters
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getIdCategoriaVeiculo() {
        return idCategoriaVeiculo;
    }

    public void setIdCategoriaVeiculo(int idCategoriaVeiculo) {
        this.idCategoriaVeiculo = idCategoriaVeiculo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getStatusVeiculo() {
        return statusVeiculo;
    }

    public void setStatusVeiculo(String statusVeiculo) {
        this.statusVeiculo = statusVeiculo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return "Veiculo Placa: '" + placa + '\'' +
               ", Marca: '" + marca + '\'' +
               ", Modelo: '" + modelo + '\'' +
               ", Ano: " + (anoFabricacao != null ? anoFabricacao : "N/A") +
               ", Status: '" + statusVeiculo + '\'';
    }
}