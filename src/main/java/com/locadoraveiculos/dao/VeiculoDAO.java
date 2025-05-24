package com.locadoraveiculos.dao;

import com.locadoraveiculos.config.DatabaseConnection;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Veiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public void salvar(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo (placa, id_categoria_veiculo, modelo, marca, ano_fabricacao, " +
                     "cor, chassi, renavam, status_veiculo, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, veiculo.getPlaca());
            pstmt.setInt(2, veiculo.getIdCategoriaVeiculo());
            pstmt.setString(3, veiculo.getModelo());
            pstmt.setString(4, veiculo.getMarca());
            if (veiculo.getAnoFabricacao() != null) {
                pstmt.setInt(5, veiculo.getAnoFabricacao());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setString(6, veiculo.getCor());
            pstmt.setString(7, veiculo.getChassi());
            pstmt.setString(8, veiculo.getRenavam());
            pstmt.setString(9, veiculo.getStatusVeiculo());
            pstmt.setString(10, veiculo.getObservacoes());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new PersistenceException("Falha ao salvar veículo, nenhuma linha afetada.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000") || e.getMessage().toLowerCase().contains("duplicate entry")) { //  Integrity constraint violation (PK or UNIQUE)
                 throw new PersistenceException("Erro ao salvar veículo: Placa '" + veiculo.getPlaca() + "' ou Chassi/Renavam já existem.", e);
            }
            throw new PersistenceException("Erro ao salvar veículo: " + e.getMessage(), e);
        }
    }

    public Veiculo buscarPorPlaca(String placa) {
        String sql = "SELECT * FROM veiculo WHERE placa = ?";
        Veiculo veiculo = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, placa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    veiculo = new Veiculo();
                    veiculo.setPlaca(rs.getString("placa"));
                    veiculo.setIdCategoriaVeiculo(rs.getInt("id_categoria_veiculo"));
                    veiculo.setModelo(rs.getString("modelo"));
                    veiculo.setMarca(rs.getString("marca"));
                    int ano = rs.getInt("ano_fabricacao");
                    veiculo.setAnoFabricacao(rs.wasNull() ? null : ano);
                    veiculo.setCor(rs.getString("cor"));
                    veiculo.setChassi(rs.getString("chassi"));
                    veiculo.setRenavam(rs.getString("renavam"));
                    veiculo.setStatusVeiculo(rs.getString("status_veiculo"));
                    veiculo.setObservacoes(rs.getString("observacoes"));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar veículo por placa '" + placa + "': " + e.getMessage(), e);
        }
        return veiculo;
    }

    public List<Veiculo> listarTodos() {
        String sql = "SELECT * FROM veiculo";
        List<Veiculo> veiculos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setPlaca(rs.getString("placa"));
                veiculo.setIdCategoriaVeiculo(rs.getInt("id_categoria_veiculo"));
                veiculo.setModelo(rs.getString("modelo"));
                veiculo.setMarca(rs.getString("marca"));
                int ano = rs.getInt("ano_fabricacao");
                veiculo.setAnoFabricacao(rs.wasNull() ? null : ano);
                veiculo.setCor(rs.getString("cor"));
                veiculo.setChassi(rs.getString("chassi"));
                veiculo.setRenavam(rs.getString("renavam"));
                veiculo.setStatusVeiculo(rs.getString("status_veiculo"));
                veiculo.setObservacoes(rs.getString("observacoes"));
                veiculos.add(veiculo);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar veículos: " + e.getMessage(), e);
        }
        return veiculos;
    }

    public void atualizar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET id_categoria_veiculo = ?, modelo = ?, marca = ?, " +
                     "ano_fabricacao = ?, cor = ?, chassi = ?, renavam = ?, status_veiculo = ?, " +
                     "observacoes = ? WHERE placa = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, veiculo.getIdCategoriaVeiculo());
            pstmt.setString(2, veiculo.getModelo());
            pstmt.setString(3, veiculo.getMarca());
            if (veiculo.getAnoFabricacao() != null) {
                pstmt.setInt(4, veiculo.getAnoFabricacao());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            pstmt.setString(5, veiculo.getCor());
            pstmt.setString(6, veiculo.getChassi());
            pstmt.setString(7, veiculo.getRenavam());
            pstmt.setString(8, veiculo.getStatusVeiculo());
            pstmt.setString(9, veiculo.getObservacoes());
            pstmt.setString(10, veiculo.getPlaca());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new PersistenceException("Falha ao atualizar veículo, veículo com placa '" + veiculo.getPlaca() + "' não encontrado ou dados inalterados.");
            }
        } catch (SQLException e) {
             if (e.getSQLState().equals("23000") || e.getMessage().toLowerCase().contains("duplicate entry")) {
                 throw new PersistenceException("Erro ao atualizar veículo: Chassi/Renavam já existem para outro veículo.", e);
            }
            throw new PersistenceException("Erro ao atualizar veículo placa '" + veiculo.getPlaca() + "': " + e.getMessage(), e);
        }
    }

    public void excluir(String placa) {
        String sql = "DELETE FROM veiculo WHERE placa = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, placa);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Aviso: Nenhuma linha afetada ao tentar excluir veículo placa: " + placa + ". Veículo pode não existir.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { // Integrity constraint violation
                throw new PersistenceException("Não é possível excluir o veículo placa '" + placa + "' pois ele possui registros associados (ex: locações).", e);
            }
            throw new PersistenceException("Erro ao excluir veículo placa '" + placa + "': " + e.getMessage(), e);
        }
    }
}