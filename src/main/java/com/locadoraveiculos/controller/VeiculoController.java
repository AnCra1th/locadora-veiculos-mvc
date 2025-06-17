package com.locadoraveiculos.controller;

import com.locadoraveiculos.dao.VeiculoDAO;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class VeiculoController {
    private VeiculoDAO veiculoDAO;

    public VeiculoController() {
        this.veiculoDAO = new VeiculoDAO();
    }

    public boolean cadastrarVeiculo(Veiculo veiculo) {
        try {
            if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
                System.err.println("Erro de validação: Placa do veículo não pode ser vazia.");
                return false;
            }
            if (veiculo.getModelo() == null || veiculo.getModelo().trim().isEmpty()) {
                System.err.println("Erro de validação: Modelo do veículo não pode ser vazio.");
                return false;
            }
            if (veiculo.getMarca() == null || veiculo.getMarca().trim().isEmpty()) {
                System.err.println("Erro de validação: Marca do veículo não pode ser vazia.");
                return false;
            }
            if (veiculo.getIdCategoriaVeiculo() == 0) {
                System.err.println("Erro de validação: ID da categoria do veículo é inválido.");
                return false;
            }

            veiculoDAO.salvar(veiculo);
            System.out.println("INFO: Veículo placa '" + veiculo.getPlaca() + "' cadastrado com sucesso!");
            return true;
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: " + e.getMessage());
            return false;
        }
    }

    public Veiculo buscarVeiculoPorPlaca(String placa) {
        try {
            if (placa == null || placa.trim().isEmpty()) {
                System.err.println("Erro de validação: Placa para busca não pode ser vazia.");
                return null;
            }
            return veiculoDAO.buscarPorPlaca(placa.toUpperCase());
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: " + e.getMessage());
            return null;
        }
    }

    public List<Veiculo> listarTodosVeiculos() {
        try {
            return veiculoDAO.listarTodos();
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao listar veículos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean atualizarVeiculo(Veiculo veiculo) {
        try {
             if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
                System.err.println("Erro de validação: Placa do veículo inválida para atualização.");
                return false;
            }
            veiculoDAO.atualizar(veiculo);
            System.out.println("INFO: Veículo placa '" + veiculo.getPlaca() + "' atualizado com sucesso!");
            return true;
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: " + e.getMessage());
            return false;
        }
    }

    public boolean excluirVeiculo(String placa) {
        try {
            if (placa == null || placa.trim().isEmpty()) {
                System.err.println("Erro de validação: Placa para exclusão não pode ser vazia.");
                return false;
            }
            veiculoDAO.excluir(placa.toUpperCase());
            System.out.println("INFO: Veículo placa '" + placa.toUpperCase() + "' excluído (ou tentativa de exclusão registrada).");
            return true;
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: " + e.getMessage());
            return false;
        }
    }
}