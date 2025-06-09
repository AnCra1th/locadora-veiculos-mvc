package com.locadoraveiculos.controller;

import com.locadoraveiculos.dao.CategoriaVeiculoDAO;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.CategoriaVeiculo;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller para gerenciar operações relacionadas a Categoria de Veículo.
 */
public class CategoriaVeiculoController {
    private CategoriaVeiculoDAO categoriaVeiculoDAO;

    public CategoriaVeiculoController() {
        this.categoriaVeiculoDAO = new CategoriaVeiculoDAO();
    }

    public List<CategoriaVeiculo> listarTodasCategorias() {
        try {
            return categoriaVeiculoDAO.listarTodas();
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao listar categorias de veículo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}