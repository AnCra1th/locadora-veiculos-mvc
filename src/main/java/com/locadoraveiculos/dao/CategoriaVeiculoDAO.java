package com.locadoraveiculos.dao;

import com.locadoraveiculos.config.DatabaseConnection;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.CategoriaVeiculo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade CategoriaVeiculo.
 */
public class CategoriaVeiculoDAO {

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

    // Outros métodos como salvar, buscarPorId, atualizar e excluir podem ser adicionados aqui se necessário.
}