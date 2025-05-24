package com.locadoraveiculos.model;

import java.time.LocalDate;

public class Locacao {
    private Long id;
    private LocalDate dataLocacao;
    private LocalDate dataRetorno;
    private Cliente cliente;
    private Veiculo veiculo;

    public Locacao() {
    }

    public Locacao(Long id, LocalDate dataLocacao, LocalDate dataRetorno, Cliente cliente, Veiculo veiculo) {
        this.id = id;
        this.dataLocacao = dataLocacao;
        this.dataRetorno = dataRetorno;
        this.cliente = cliente;
        this.veiculo = veiculo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataLocacao() {
        return dataLocacao;
    }

    public void setDataLocacao(LocalDate dataLocacao) {
        this.dataLocacao = dataLocacao;
    }

    public LocalDate getDataRetorno() {
        return dataRetorno;
    }

    public void setDataRetorno(LocalDate dataRetorno) {
        this.dataRetorno = dataRetorno;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
}