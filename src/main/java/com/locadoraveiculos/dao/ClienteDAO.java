package com.locadoraveiculos.dao;

import com.locadoraveiculos.config.DatabaseConnection;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, cpf, cnh, data_validade_cnh, telefone, email, " +
                     "endereco_rua, endereco_numero, endereco_complemento, endereco_bairro, " +
                     "endereco_cidade, endereco_estado, endereco_cep) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getCpf());
            pstmt.setString(3, cliente.getCnh());
            if (cliente.getDataValidadeCnh() != null) {
                pstmt.setDate(4, new java.sql.Date(cliente.getDataValidadeCnh().getTime()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            pstmt.setString(5, cliente.getTelefone());
            pstmt.setString(6, cliente.getEmail());
            pstmt.setString(7, cliente.getEnderecoRua());
            pstmt.setString(8, cliente.getEnderecoNumero());
            pstmt.setString(9, cliente.getEnderecoComplemento());
            pstmt.setString(10, cliente.getEnderecoBairro());
            pstmt.setString(11, cliente.getEnderecoCidade());
            pstmt.setString(12, cliente.getEnderecoEstado());
            pstmt.setString(13, cliente.getEnderecoCep());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new PersistenceException("Falha ao salvar cliente, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getInt(1));
                } else {
                    throw new PersistenceException("Falha ao salvar cliente, não foi possível obter o ID gerado.");
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao salvar cliente: " + e.getMessage(), e);
        }
    }

    public Cliente buscarPorId(int idCliente) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
        Cliente cliente = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setCnh(rs.getString("cnh"));
                    cliente.setDataValidadeCnh(rs.getDate("data_validade_cnh"));
                    cliente.setTelefone(rs.getString("telefone"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setEnderecoRua(rs.getString("endereco_rua"));
                    cliente.setEnderecoNumero(rs.getString("endereco_numero"));
                    cliente.setEnderecoComplemento(rs.getString("endereco_complemento"));
                    cliente.setEnderecoBairro(rs.getString("endereco_bairro"));
                    cliente.setEnderecoCidade(rs.getString("endereco_cidade"));
                    cliente.setEnderecoEstado(rs.getString("endereco_estado"));
                    cliente.setEnderecoCep(rs.getString("endereco_cep"));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar cliente por ID: " + e.getMessage(), e);
        }
        return cliente;
    }

    public List<Cliente> listarTodos() {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setCnh(rs.getString("cnh"));
                cliente.setDataValidadeCnh(rs.getDate("data_validade_cnh"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEmail(rs.getString("email"));
                // Preencher demais campos de endereço se necessário para a listagem
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, cpf = ?, cnh = ?, data_validade_cnh = ?, " +
                     "telefone = ?, email = ?, endereco_rua = ?, endereco_numero = ?, " +
                     "endereco_complemento = ?, endereco_bairro = ?, endereco_cidade = ?, " +
                     "endereco_estado = ?, endereco_cep = ? WHERE id_cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getCpf());
            pstmt.setString(3, cliente.getCnh());
            if (cliente.getDataValidadeCnh() != null) {
                pstmt.setDate(4, new java.sql.Date(cliente.getDataValidadeCnh().getTime()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            pstmt.setString(5, cliente.getTelefone());
            pstmt.setString(6, cliente.getEmail());
            pstmt.setString(7, cliente.getEnderecoRua());
            pstmt.setString(8, cliente.getEnderecoNumero());
            pstmt.setString(9, cliente.getEnderecoComplemento());
            pstmt.setString(10, cliente.getEnderecoBairro());
            pstmt.setString(11, cliente.getEnderecoCidade());
            pstmt.setString(12, cliente.getEnderecoEstado());
            pstmt.setString(13, cliente.getEnderecoCep());
            pstmt.setInt(14, cliente.getIdCliente());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new PersistenceException("Falha ao atualizar cliente, cliente com ID " + cliente.getIdCliente() + " não encontrado ou dados inalterados.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    public void excluir(int idCliente) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                // Não é necessariamente um erro se o cliente não existir, mas pode ser um aviso.
                System.out.println("Aviso: Nenhuma linha afetada ao tentar excluir cliente ID: " + idCliente + ". Cliente pode não existir.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { // Integrity constraint violation
                throw new PersistenceException("Não é possível excluir o cliente ID " + idCliente + " pois ele possui registros associados (ex: locações).", e);
            }
            throw new PersistenceException("Erro ao excluir cliente ID " + idCliente + ": " + e.getMessage(), e);
        }
    }
}