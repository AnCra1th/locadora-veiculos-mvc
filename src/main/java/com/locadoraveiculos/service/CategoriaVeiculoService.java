package com.locadoraveiculos.service;

import com.locadoraveiculos.dao.CategoriaVeiculoDAO;
import com.locadoraveiculos.model.CategoriaVeiculo;
import java.math.BigDecimal;

public class CategoriaVeiculoService {

    private CategoriaVeiculoDAO categoriaVeiculoDAO;

    public CategoriaVeiculoService(CategoriaVeiculoDAO categoriaVeiculoDAO) {
        this.categoriaVeiculoDAO = categoriaVeiculoDAO;
    }

    public boolean adicionarCategoria(CategoriaVeiculo categoria) {
        if (categoria == null || categoria.getNomeCategoria() == null || categoria.getNomeCategoria().isBlank() ||
            categoria.getValorDiariaBase() == null || categoria.getValorDiariaBase().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        try {
            categoriaVeiculoDAO.salvar(categoria);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}