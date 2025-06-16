package com.locadoraveiculos.service;

import com.locadoraveiculos.dao.VeiculoDAO;
import com.locadoraveiculos.model.Veiculo;

public class VeiculoService {

    private VeiculoDAO veiculoDAO;

    public VeiculoService(VeiculoDAO veiculoDAO) {
        this.veiculoDAO = veiculoDAO;
    }

    public boolean adicionarVeiculo(Veiculo veiculo) {
        if (veiculo == null || veiculo.getModelo() == null || veiculo.getModelo().isBlank()) {
            return false;
        }
        veiculoDAO.salvar(veiculo);
        return true;
    }
}
