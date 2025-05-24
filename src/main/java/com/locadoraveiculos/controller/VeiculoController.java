package com.locadoraveiculos.controller;

import com.locadoraveiculos.dao.VeiculoDAO;
import com.locadoraveiculos.model.Veiculo;

import java.util.List;

public class VeiculoController {

    private VeiculoDAO veiculoDAO;

    public VeiculoController() {
        this.veiculoDAO = new VeiculoDAO();
    }

    public void addVeiculo(Veiculo veiculo) {
        veiculoDAO.save(veiculo);
    }

    public void updateVeiculo(Veiculo veiculo) {
        veiculoDAO.update(veiculo);
    }

    public void deleteVeiculo(int id) {
        veiculoDAO.delete(id);
    }

    public Veiculo getVeiculo(int id) {
        return veiculoDAO.findById(id);
    }

    public List<Veiculo> getAllVeiculos() {
        return veiculoDAO.findAll();
    }
}