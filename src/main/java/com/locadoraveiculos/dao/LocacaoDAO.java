package com.locadoraveiculos.dao;

import com.locadoraveiculos.config.DatabaseConnection;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Locacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocacaoDAO {

    public void salvar(Locacao locacao) {
        String sql = "INSERT INTO locacao (id_cliente, placa_veiculo, id_funcionario_retirada, " +
                     "data_retirada, data_prevista_devolucao, valor_diaria_locacao, status_locacao, " +
                     "valor_caucao, valor_seguro, observacoes_retirada, id_reserva, " +
                     "valor_multa_atraso, valor_total_previsto) " + // Adicionado id_reserva e outros
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, locacao.getIdCliente());
            pstmt.setString(2, locacao.getPlacaVeiculo());
            pstmt.setInt(3, locacao.getIdFuncionarioRetirada());
            pstmt.setTimestamp(4, new Timestamp(locacao.getDataRetirada().getTime()));
            pstmt.setTimestamp(5, new Timestamp(locacao.getDataPrevistaDevolucao().getTime()));
            pstmt.setBigDecimal(6, locacao.getValorDiariaLocacao());
            pstmt.setString(7, locacao.getStatusLocacao());
            pstmt.setBigDecimal(8, locacao.getValorCaucao());
            pstmt.setBigDecimal(9, locacao.getValorSeguro());
            pstmt.setString(10, locacao.getObservacoesRetirada());
            
            if (locacao.getIdReserva() != null) {
                pstmt.setInt(11, locacao.getIdReserva());
            } else {
                pstmt.setNull(11, Types.INTEGER);
            }
            pstmt.setBigDecimal(12, locacao.getValorMultaAtraso());
            pstmt.setBigDecimal(13, locacao.getValorTotalPrevisto());


            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new PersistenceException("Falha ao salvar locação, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    locacao.setIdLocacao(generatedKeys.getInt(1)); // CORRIGIDO AQUI
                } else {
                    throw new PersistenceException("Falha ao obter o ID gerado para a locação.");
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao salvar locação: " + e.getMessage(), e);
        }
    }

    public Locacao buscarPorId(int idLocacao) {
        String sql = "SELECT * FROM locacao WHERE id_locacao = ?";
        Locacao locacao = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idLocacao);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    locacao = new Locacao();
                    locacao.setIdLocacao(rs.getInt("id_locacao"));
                    locacao.setIdCliente(rs.getInt("id_cliente"));
                    locacao.setPlacaVeiculo(rs.getString("placa_veiculo"));
                    locacao.setIdFuncionarioRetirada(rs.getInt("id_funcionario_retirada"));
                    locacao.setIdFuncionarioDevolucao(rs.getObject("id_funcionario_devolucao", Integer.class));
                    locacao.setIdReserva(rs.getObject("id_reserva", Integer.class));
                    locacao.setDataRetirada(rs.getTimestamp("data_retirada"));
                    locacao.setDataPrevistaDevolucao(rs.getTimestamp("data_prevista_devolucao"));
                    locacao.setDataEfetivaDevolucao(rs.getTimestamp("data_efetiva_devolucao"));
                    locacao.setValorDiariaLocacao(rs.getBigDecimal("valor_diaria_locacao"));
                    locacao.setValorCaucao(rs.getBigDecimal("valor_caucao"));
                    locacao.setValorSeguro(rs.getBigDecimal("valor_seguro"));
                    locacao.setValorMultaAtraso(rs.getBigDecimal("valor_multa_atraso"));
                    locacao.setValorTotalPrevisto(rs.getBigDecimal("valor_total_previsto"));
                    locacao.setValorTotalFinal(rs.getBigDecimal("valor_total_final"));
                    locacao.setStatusLocacao(rs.getString("status_locacao"));
                    locacao.setObservacoesRetirada(rs.getString("observacoes_retirada"));
                    locacao.setObservacoesDevolucao(rs.getString("observacoes_devolucao"));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar locação por ID " + idLocacao + ": " + e.getMessage(), e);
        }
        return locacao;
    }
    
    public List<Locacao> listarTodas() {
        String sql = "SELECT * FROM locacao ORDER BY data_retirada DESC";
        List<Locacao> locacoes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Locacao locacao = new Locacao();
                locacao.setIdLocacao(rs.getInt("id_locacao"));
                locacao.setIdCliente(rs.getInt("id_cliente"));
                locacao.setPlacaVeiculo(rs.getString("placa_veiculo"));
                locacao.setIdFuncionarioRetirada(rs.getInt("id_funcionario_retirada"));
                locacao.setDataRetirada(rs.getTimestamp("data_retirada"));
                locacao.setDataPrevistaDevolucao(rs.getTimestamp("data_prevista_devolucao"));
                locacao.setStatusLocacao(rs.getString("status_locacao"));
                locacao.setValorDiariaLocacao(rs.getBigDecimal("valor_diaria_locacao"));
                // Adicionar mais campos se necessário para a visualização em lista
                locacoes.add(locacao);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar locações: " + e.getMessage(), e);
        }
        return locacoes;
    }

    public void atualizar(Locacao locacao) {
        String sql = "UPDATE locacao SET id_cliente = ?, placa_veiculo = ?, id_funcionario_retirada = ?, " +
                     "id_funcionario_devolucao = ?, id_reserva = ?, data_retirada = ?, " +
                     "data_prevista_devolucao = ?, data_efetiva_devolucao = ?, valor_diaria_locacao = ?, " +
                     "valor_caucao = ?, valor_seguro = ?, valor_multa_atraso = ?, valor_total_previsto = ?, " +
                     "valor_total_final = ?, status_locacao = ?, observacoes_retirada = ?, observacoes_devolucao = ? " +
                     "WHERE id_locacao = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, locacao.getIdCliente());
            pstmt.setString(2, locacao.getPlacaVeiculo());
            pstmt.setInt(3, locacao.getIdFuncionarioRetirada());

            if (locacao.getIdFuncionarioDevolucao() != null) {
                pstmt.setInt(4, locacao.getIdFuncionarioDevolucao());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            if (locacao.getIdReserva() != null) {
                pstmt.setInt(5, locacao.getIdReserva());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setTimestamp(6, new Timestamp(locacao.getDataRetirada().getTime()));
            pstmt.setTimestamp(7, new Timestamp(locacao.getDataPrevistaDevolucao().getTime()));
            if (locacao.getDataEfetivaDevolucao() != null) {
                pstmt.setTimestamp(8, new Timestamp(locacao.getDataEfetivaDevolucao().getTime()));
            } else {
                pstmt.setNull(8, Types.TIMESTAMP);
            }
            pstmt.setBigDecimal(9, locacao.getValorDiariaLocacao());
            pstmt.setBigDecimal(10, locacao.getValorCaucao());
            pstmt.setBigDecimal(11, locacao.getValorSeguro());
            pstmt.setBigDecimal(12, locacao.getValorMultaAtraso());
            pstmt.setBigDecimal(13, locacao.getValorTotalPrevisto());
            pstmt.setBigDecimal(14, locacao.getValorTotalFinal());
            pstmt.setString(15, locacao.getStatusLocacao());
            pstmt.setString(16, locacao.getObservacoesRetirada());
            pstmt.setString(17, locacao.getObservacoesDevolucao());
            pstmt.setInt(18, locacao.getIdLocacao());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new PersistenceException("Falha ao atualizar locação, ID " + locacao.getIdLocacao() + " não encontrado ou dados inalterados.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar locação ID " + locacao.getIdLocacao() + ": " + e.getMessage(), e);
        }
    }
}