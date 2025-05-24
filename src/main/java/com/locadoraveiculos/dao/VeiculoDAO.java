package com.locadoraveiculos.dao;

import com.locadoraveiculos.model.Veiculo;
import com.locadoraveiculos.exception.PersistenceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    private Connection connection;

    public VeiculoDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(Veiculo veiculo) throws PersistenceException {
        String sql = "INSERT INTO veiculos (make, model, year) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getMake());
            stmt.setString(2, veiculo.getModel());
            stmt.setInt(3, veiculo.getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error saving vehicle", e);
        }
    }

    public Veiculo findById(int id) throws PersistenceException {
        String sql = "SELECT * FROM veiculos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Veiculo(rs.getInt("id"), rs.getString("make"), rs.getString("model"), rs.getInt("year"));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error finding vehicle by id", e);
        }
        return null;
    }

    public List<Veiculo> findAll() throws PersistenceException {
        List<Veiculo> veiculos = new ArrayList<>();
        String sql = "SELECT * FROM veiculos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                veiculos.add(new Veiculo(rs.getInt("id"), rs.getString("make"), rs.getString("model"), rs.getInt("year")));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error finding all vehicles", e);
        }
        return veiculos;
    }

    public void update(Veiculo veiculo) throws PersistenceException {
        String sql = "UPDATE veiculos SET make = ?, model = ?, year = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getMake());
            stmt.setString(2, veiculo.getModel());
            stmt.setInt(3, veiculo.getYear());
            stmt.setInt(4, veiculo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error updating vehicle", e);
        }
    }

    public void delete(int id) throws PersistenceException {
        String sql = "DELETE FROM veiculos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error deleting vehicle", e);
        }
    }
}