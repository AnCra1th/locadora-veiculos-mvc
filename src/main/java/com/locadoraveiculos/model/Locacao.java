package com.locadoraveiculos.model;

import java.math.BigDecimal;
import java.util.Date; // Usaremos java.util.Date para simplicidade, convertendo para SQL na DAO

/**
 * Representa a entidade Locacao.
 */
public class Locacao {
    private int idLocacao;
    private int idCliente;
    private String placaVeiculo;
    private int idFuncionarioRetirada;
    private Integer idFuncionarioDevolucao; // Pode ser nulo
    private Integer idReserva; // Pode ser nulo
    private Date dataRetirada;
    private Date dataPrevistaDevolucao;
    private Date dataEfetivaDevolucao; // Pode ser nulo
    private BigDecimal valorDiariaLocacao;
    private BigDecimal valorCaucao;
    private BigDecimal valorSeguro;
    private BigDecimal valorMultaAtraso;
    private BigDecimal valorTotalPrevisto;
    private BigDecimal valorTotalFinal; // Pode ser nulo
    private String statusLocacao;
    private String observacoesRetirada;
    private String observacoesDevolucao;

    public Locacao() {
        // Inicializar BigDecimals para evitar NullPointerException se não forem setados e usados em cálculos
        this.valorDiariaLocacao = BigDecimal.ZERO;
        this.valorCaucao = BigDecimal.ZERO;
        this.valorSeguro = BigDecimal.ZERO;
        this.valorMultaAtraso = BigDecimal.ZERO;
    }

    public Locacao(int idCliente, String placaVeiculo, int idFuncionarioRetirada, Date dataRetirada, Date dataPrevistaDevolucao, BigDecimal valorDiariaLocacao) {
        this(); // Chama o construtor padrão para inicializar BigDecimals
        this.idCliente = idCliente;
        this.placaVeiculo = placaVeiculo;
        this.idFuncionarioRetirada = idFuncionarioRetirada;
        this.dataRetirada = dataRetirada;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.valorDiariaLocacao = valorDiariaLocacao;
        this.statusLocacao = "ativa"; // Default
    }

    // Getters e Setters
    public int getIdLocacao() {
        return idLocacao;
    }

    public void setIdLocacao(int idLocacao) {
        this.idLocacao = idLocacao;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public int getIdFuncionarioRetirada() {
        return idFuncionarioRetirada;
    }

    public void setIdFuncionarioRetirada(int idFuncionarioRetirada) {
        this.idFuncionarioRetirada = idFuncionarioRetirada;
    }

    public Integer getIdFuncionarioDevolucao() {
        return idFuncionarioDevolucao;
    }

    public void setIdFuncionarioDevolucao(Integer idFuncionarioDevolucao) {
        this.idFuncionarioDevolucao = idFuncionarioDevolucao;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Date getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(Date dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public Date getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(Date dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public Date getDataEfetivaDevolucao() {
        return dataEfetivaDevolucao;
    }

    public void setDataEfetivaDevolucao(Date dataEfetivaDevolucao) {
        this.dataEfetivaDevolucao = dataEfetivaDevolucao;
    }

    public BigDecimal getValorDiariaLocacao() {
        return valorDiariaLocacao;
    }

    public void setValorDiariaLocacao(BigDecimal valorDiariaLocacao) {
        this.valorDiariaLocacao = (valorDiariaLocacao != null) ? valorDiariaLocacao : BigDecimal.ZERO;
    }

    public BigDecimal getValorCaucao() {
        return valorCaucao;
    }

    public void setValorCaucao(BigDecimal valorCaucao) {
        this.valorCaucao = (valorCaucao != null) ? valorCaucao : BigDecimal.ZERO;
    }

    public BigDecimal getValorSeguro() {
        return valorSeguro;
    }

    public void setValorSeguro(BigDecimal valorSeguro) {
        this.valorSeguro = (valorSeguro != null) ? valorSeguro : BigDecimal.ZERO;
    }

    public BigDecimal getValorMultaAtraso() {
        return valorMultaAtraso;
    }

    public void setValorMultaAtraso(BigDecimal valorMultaAtraso) {
        this.valorMultaAtraso = (valorMultaAtraso != null) ? valorMultaAtraso : BigDecimal.ZERO;
    }

    public BigDecimal getValorTotalPrevisto() {
        return valorTotalPrevisto;
    }

    public void setValorTotalPrevisto(BigDecimal valorTotalPrevisto) {
        this.valorTotalPrevisto = valorTotalPrevisto;
    }

    public BigDecimal getValorTotalFinal() {
        return valorTotalFinal;
    }

    public void setValorTotalFinal(BigDecimal valorTotalFinal) {
        this.valorTotalFinal = valorTotalFinal;
    }

    public String getStatusLocacao() {
        return statusLocacao;
    }

    public void setStatusLocacao(String statusLocacao) {
        this.statusLocacao = statusLocacao;
    }

    public String getObservacoesRetirada() {
        return observacoesRetirada;
    }

    public void setObservacoesRetirada(String observacoesRetirada) {
        this.observacoesRetirada = observacoesRetirada;
    }

    public String getObservacoesDevolucao() {
        return observacoesDevolucao;
    }

    public void setObservacoesDevolucao(String observacoesDevolucao) {
        this.observacoesDevolucao = observacoesDevolucao;
    }

    @Override
    public String toString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "Locação ID: " + idLocacao +
               ", Cliente ID: " + idCliente +
               ", Veículo Placa: '" + placaVeiculo + '\'' +
               ", Retirada: " + (dataRetirada != null ? sdf.format(dataRetirada) : "N/A") +
               ", Status: '" + statusLocacao + '\'';
    }
}