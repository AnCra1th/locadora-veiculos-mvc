package com.locadoraveiculos.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Representa a entidade Reserva.
 */
public class Reserva {
    private int idReserva;
    private int idCliente;
    private int idCategoriaVeiculo;
    private Integer idFuncionario; // Pode ser nulo
    private Date dataReserva; // O banco de dados define um default
    private Date dataPrevistaRetirada;
    private Date dataPrevistaDevolucao;
    private BigDecimal valorEstimado;
    private BigDecimal valorSinalReserva;
    private String statusReserva;
    private String observacoes;

    public Reserva() {
        this.valorEstimado = BigDecimal.ZERO;
        this.valorSinalReserva = BigDecimal.ZERO;
    }

    // Getters e Setters
    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdCategoriaVeiculo() {
        return idCategoriaVeiculo;
    }

    public void setIdCategoriaVeiculo(int idCategoriaVeiculo) {
        this.idCategoriaVeiculo = idCategoriaVeiculo;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Date getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Date dataReserva) {
        this.dataReserva = dataReserva;
    }

    public Date getDataPrevistaRetirada() {
        return dataPrevistaRetirada;
    }

    public void setDataPrevistaRetirada(Date dataPrevistaRetirada) {
        this.dataPrevistaRetirada = dataPrevistaRetirada;
    }

    public Date getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(Date dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public BigDecimal getValorEstimado() {
        return valorEstimado;
    }

    public void setValorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public BigDecimal getValorSinalReserva() {
        return valorSinalReserva;
    }

    public void setValorSinalReserva(BigDecimal valorSinalReserva) {
        this.valorSinalReserva = valorSinalReserva;
    }

    public String getStatusReserva() {
        return statusReserva;
    }

    public void setStatusReserva(String statusReserva) {
        this.statusReserva = statusReserva;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "Reserva ID: " + idReserva +
                ", Cliente ID: " + idCliente +
                ", Categoria ID: " + idCategoriaVeiculo +
                ", Retirada Prevista: " + (dataPrevistaRetirada != null ? sdf.format(dataPrevistaRetirada) : "N/A") +
                ", Status: '" + statusReserva + '\'';
    }
}