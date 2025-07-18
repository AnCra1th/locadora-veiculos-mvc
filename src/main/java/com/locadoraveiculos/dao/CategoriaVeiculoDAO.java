package com.locadoraveiculos.dao;

import com.locadoraveiculos.config.DatabaseConnection;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.CategoriaVeiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaVeiculoDAO {

    public void salvar(CategoriaVeiculo categoria) {
        String sql = "INSERT INTO categoria_veiculo (nome_categoria, descricao, valor_diaria_base) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, categoria.getNomeCategoria());
            pstmt.setString(2, categoria.getDescricao());
            pstmt.setBigDecimal(3, categoria.getValorDiariaBase());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new PersistenceException("Falha ao salvar categoria de veículo, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    categoria.setIdCategoriaVeiculo(generatedKeys.getInt(1));
                } else {
                    throw new PersistenceException("Falha ao obter o ID gerado para a categoria.");
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao salvar categoria de veículo: " + e.getMessage(), e);
        }
    }

    public CategoriaVeiculo buscarPorId(int id) {
         String sql = "SELECT * FROM categoria_veiculo WHERE id_categoria_veiculo = ?";
         CategoriaVeiculo categoria = null;
         try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.setInt(1, id);
             try(ResultSet rs = pstmt.executeQuery()) {
                 if(rs.next()){
                     categoria = new CategoriaVeiculo();
                     categoria.setIdCategoriaVeiculo(rs.getInt("id_categoria_veiculo"));
                     categoria.setNomeCategoria(rs.getString("nome_categoria"));
                     categoria.setDescricao(rs.getString("descricao"));
                     categoria.setValorDiariaBase(rs.getBigDecimal("valor_diaria_base"));
                 }
             }
         } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar categoria por ID: " + e.getMessage(), e);
        }
        return categoria;
    }

    public List<CategoriaVeiculo> listarTodas() {
        String sql = "SELECT * FROM categoria_veiculo ORDER BY nome_categoria";
        List<CategoriaVeiculo> categorias = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CategoriaVeiculo categoria = new CategoriaVeiculo();
                categoria.setIdCategoriaVeiculo(rs.getInt("id_categoria_veiculo"));
                categoria.setNomeCategoria(rs.getString("nome_categoria"));
                categoria.setDescricao(rs.getString("descricao"));
                categoria.setValorDiariaBase(rs.getBigDecimal("valor_diaria_base"));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar categorias de veículo: " + e.getMessage(), e);
        }
        return categorias;
    }

}
