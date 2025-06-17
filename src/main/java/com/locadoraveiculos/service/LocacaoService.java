package com.locadoraveiculos.service;

import com.locadoraveiculos.dao.LocacaoDAO;
import com.locadoraveiculos.model.Locacao;

public class LocacaoService {

    private LocacaoDAO locacaoDAO;

    public LocacaoService(LocacaoDAO locacaoDAO) {
        this.locacaoDAO = locacaoDAO;
    }

    public boolean registrarLocacao(Locacao locacao) {
        if (locacao == null || locacao.getIdCliente() <= 0 || locacao.getPlacaVeiculo() == null || locacao.getPlacaVeiculo().isBlank()) {
            System.err.println("SERVIÇO ERRO: Cliente e Placa do Veículo são obrigatórios para a locação.");
            return false;
        }

        if (locacao.getDataPrevistaDevolucao().before(locacao.getDataRetirada())) {
            System.err.println("SERVIÇO ERRO: Data de devolução não pode ser anterior à data de retirada.");
            return false;
        }

        try {
            locacaoDAO.salvar(locacao);
            return true;
        } catch (Exception e) {
            System.err.println("SERVIÇO ERRO: Falha ao salvar locação: " + e.getMessage());
            return false;
        }
    }
}