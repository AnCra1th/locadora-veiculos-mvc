package com.locadoraveiculos.dao;

import com.locadoraveiculos.config.DatabaseConnection;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Reserva;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public void salvar(Reserva reserva) {
        String sql = "INSERT INTO reserva (id_cliente, id_categoria_veiculo, id_funcionario, data_prevista_retirada, " +
                     "data_prevista_devolucao, valor_estimado, valor_sinal_reserva, status_reserva, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, reserva.getIdCliente());
            pstmt.setInt(2, reserva.getIdCategoriaVeiculo());
            if (reserva.getIdFuncionario() != null) {
                pstmt.setInt(3, reserva.getIdFuncionario());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setTimestamp(4, new Timestamp(reserva.getDataPrevistaRetirada().getTime()));
            pstmt.setTimestamp(5, new Timestamp(reserva.getDataPrevistaDevolucao().getTime()));
            pstmt.setBigDecimal(6, reserva.getValorEstimado());
            pstmt.setBigDecimal(7, reserva.getValorSinalReserva());
            pstmt.setString(8, reserva.getStatusReserva());
            pstmt.setString(9, reserva.getObservacoes());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new PersistenceException("Falha ao salvar reserva, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reserva.setIdReserva(generatedKeys.getInt(1));
                } else {
                    throw new PersistenceException("Falha ao obter o ID gerado para a reserva.");
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao salvar reserva: " + e.getMessage(), e);
        }
    }
    
    public Reserva buscarPorId(int idReserva) {
        String sql = "SELECT * FROM reserva WHERE id_reserva = ?";
        Reserva reserva = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idReserva);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    reserva = new Reserva();
                    reserva.setIdReserva(rs.getInt("id_reserva"));
                    reserva.setIdCliente(rs.getInt("id_cliente"));
                    reserva.setIdCategoriaVeiculo(rs.getInt("id_categoria_veiculo"));
                    reserva.setIdFuncionario(rs.getObject("id_funcionario", Integer.class));
                    reserva.setDataReserva(rs.getTimestamp("data_reserva"));
                    reserva.setDataPrevistaRetirada(rs.getTimestamp("data_prevista_retirada"));
                    reserva.setDataPrevistaDevolucao(rs.getTimestamp("data_prevista_devolucao"));
                    reserva.setValorEstimado(rs.getBigDecimal("valor_estimado"));
                    reserva.setValorSinalReserva(rs.getBigDecimal("valor_sinal_reserva"));
                    reserva.setStatusReserva(rs.getString("status_reserva"));
                    reserva.setObservacoes(rs.getString("observacoes"));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar reserva por ID " + idReserva + ": " + e.getMessage(), e);
        }
        return reserva;
    }
    
    public List<Reserva> listarTodas() {
        String sql = "SELECT * FROM reserva ORDER BY data_reserva DESC";
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setIdReserva(rs.getInt("id_reserva"));
                reserva.setIdCliente(rs.getInt("id_cliente"));
                reserva.setIdCategoriaVeiculo(rs.getInt("id_categoria_veiculo"));
                reserva.setDataReserva(rs.getTimestamp("data_reserva"));
                reserva.setDataPrevistaRetirada(rs.getTimestamp("data_prevista_retirada"));
                reserva.setDataPrevistaDevolucao(rs.getTimestamp("data_prevista_devolucao"));
                reserva.setStatusReserva(rs.getString("status_reserva"));
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar reservas: " + e.getMessage(), e);
        }
        return reservas;
    }

    public void atualizar(Reserva reserva) {
        String sql = "UPDATE reserva SET id_cliente = ?, id_categoria_veiculo = ?, id_funcionario = ?, " +
                     "data_prevista_retirada = ?, data_prevista_devolucao = ?, valor_estimado = ?, " +
                     "valor_sinal_reserva = ?, status_reserva = ?, observacoes = ? WHERE id_reserva = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reserva.getIdCliente());
            pstmt.setInt(2, reserva.getIdCategoriaVeiculo());
            if (reserva.getIdFuncionario() != null) {
                pstmt.setInt(3, reserva.getIdFuncionario());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setTimestamp(4, new Timestamp(reserva.getDataPrevistaRetirada().getTime()));
            pstmt.setTimestamp(5, new Timestamp(reserva.getDataPrevistaDevolucao().getTime()));
            pstmt.setBigDecimal(6, reserva.getValorEstimado());
            pstmt.setBigDecimal(7, reserva.getValorSinalReserva());
            pstmt.setString(8, reserva.getStatusReserva());
            pstmt.setString(9, reserva.getObservacoes());
            pstmt.setInt(10, reserva.getIdReserva());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new PersistenceException("Falha ao atualizar reserva, ID " + reserva.getIdReserva() + " não encontrado ou dados inalterados.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar reserva ID " + reserva.getIdReserva() + ": " + e.getMessage(), e);
        }
    }
}