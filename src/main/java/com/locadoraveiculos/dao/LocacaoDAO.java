package com.locadoraveiculos.dao;

import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Locacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocacaoDAO {

    private Connection connection;

    public LocacaoDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(Locacao locacao) throws PersistenceException {
        String sql = "INSERT INTO locacao (data_inicio, data_fim, cliente_id, veiculo_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(locacao.getDataInicio()));
            stmt.setDate(2, java.sql.Date.valueOf(locacao.getDataFim()));
            stmt.setInt(3, locacao.getClienteId());
            stmt.setInt(4, locacao.getVeiculoId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error saving locacao", e);
        }
    }

    public List<Locacao> findAll() throws PersistenceException {
        List<Locacao> locacoes = new ArrayList<>();
        String sql = "SELECT * FROM locacao";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Locacao locacao = new Locacao();
                locacao.setId(rs.getInt("id"));
                locacao.setDataInicio(rs.getDate("data_inicio").toLocalDate());
                locacao.setDataFim(rs.getDate("data_fim").toLocalDate());
                locacao.setClienteId(rs.getInt("cliente_id"));
                locacao.setVeiculoId(rs.getInt("veiculo_id"));
                locacoes.add(locacao);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving locacoes", e);
        }
        return locacoes;
    }

    public void update(Locacao locacao) throws PersistenceException {
        String sql = "UPDATE locacao SET data_inicio = ?, data_fim = ?, cliente_id = ?, veiculo_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(locacao.getDataInicio()));
            stmt.setDate(2, java.sql.Date.valueOf(locacao.getDataFim()));
            stmt.setInt(3, locacao.getClienteId());
            stmt.setInt(4, locacao.getVeiculoId());
            stmt.setInt(5, locacao.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error updating locacao", e);
        }
    }

    public void delete(int id) throws PersistenceException {
        String sql = "DELETE FROM locacao WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error deleting locacao", e);
        }
    }
}