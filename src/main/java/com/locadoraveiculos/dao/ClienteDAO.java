package com.locadoraveiculos.dao;

import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private Connection connection;

    public ClienteDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(Cliente cliente) throws PersistenceException {
        String sql = "INSERT INTO clientes (nome, endereco, contato) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getContato());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error saving client", e);
        }
    }

    public Cliente findById(int id) throws PersistenceException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cliente(rs.getInt("id"), rs.getString("nome"), rs.getString("endereco"), rs.getString("contato"));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error finding client by id", e);
        }
        return null;
    }

    public List<Cliente> findAll() throws PersistenceException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(rs.getInt("id"), rs.getString("nome"), rs.getString("endereco"), rs.getString("contato")));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error finding all clients", e);
        }
        return clientes;
    }

    public void update(Cliente cliente) throws PersistenceException {
        String sql = "UPDATE clientes SET nome = ?, endereco = ?, contato = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getContato());
            stmt.setInt(4, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error updating client", e);
        }
    }

    public void delete(int id) throws PersistenceException {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error deleting client", e);
        }
    }
}