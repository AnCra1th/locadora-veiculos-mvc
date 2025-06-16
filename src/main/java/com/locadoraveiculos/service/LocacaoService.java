package com.locadoraveiculos.service;

import com.locadoraveiculos.dao.LocacaoDAO;
import com.locadoraveiculos.model.Locacao;

public class LocacaoService {

    private LocacaoDAO locacaoDAO;

    public LocacaoService(LocacaoDAO locacaoDAO) {
        this.locacaoDAO = locacaoDAO;
    }

    /**
     * Valida e registra uma nova locação.
     * @param locacao A locação a ser registrada.
     * @return true se a locação for válida e salva, false caso contrário.
     */
    public boolean registrarLocacao(Locacao locacao) {
        // A validação deve ser feita com os IDs, que é o que o model possui.
        if (locacao == null || locacao.getIdCliente() <= 0 || locacao.getPlacaVeiculo() == null || locacao.getPlacaVeiculo().isBlank()) {
            System.err.println("SERVIÇO ERRO: Cliente e Placa do Veículo são obrigatórios para a locação.");
            return false;
        }

        // Outras validações podem ser adicionadas aqui
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