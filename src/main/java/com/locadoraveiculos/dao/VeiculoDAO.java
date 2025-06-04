package com.locadoraveiculos.dao;

import com.locadoraveiculos.config.DatabaseConnection;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Veiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    // Dentro da classe com.locadoraveiculos.dao.VeiculoDAO

public void salvar(Veiculo veiculo) {
    String sql = "INSERT INTO veiculo (placa, id_categoria_veiculo, modelo, marca, ano_fabricacao, " +
                 "cor, chassi, renavam, status_veiculo, observacoes) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Logs de depuração foram removidos/comentados para uma saída mais limpa.
        // Se precisar depurar novamente, você pode descomentá-los:
        /*
        System.out.println("--- [VeiculoDAO.salvar] Preparando para salvar Veículo ---");
        System.out.println("Placa (objeto):         [" + (veiculo.getPlaca() != null ? veiculo.getPlaca() : "NULL") + "]");
        System.out.println("ID Categoria (objeto):  [" + veiculo.getIdCategoriaVeiculo() + "]");
        System.out.println("Modelo (objeto):        [" + (veiculo.getModelo() != null ? veiculo.getModelo() : "NULL") + "]");
        System.out.println("Marca (objeto):         [" + (veiculo.getMarca() != null ? veiculo.getMarca() : "NULL") + "]");
        System.out.println("Ano Fab. (objeto):      [" + (veiculo.getAnoFabricacao() != null ? veiculo.getAnoFabricacao() : "NULL") + "]");
        System.out.println("Cor (objeto):           [" + (veiculo.getCor() != null ? veiculo.getCor() : "NULL") + "]");
        System.out.println("Chassi (objeto):        [" + (veiculo.getChassi() != null ? veiculo.getChassi() : "NULL") + "]");
        System.out.println("Renavam (objeto):       [" + (veiculo.getRenavam() != null ? veiculo.getRenavam() : "NULL") + "]");
        System.out.println("Status (objeto):        [" + (veiculo.getStatusVeiculo() != null ? veiculo.getStatusVeiculo() : "NULL") + "]");
        System.out.println("Observações (objeto):   [" + (veiculo.getObservacoes() != null ? veiculo.getObservacoes() : "NULL") + "]");
        System.out.println("------------------------------------------------------");
        */

        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            // Esta exceção é mais para um erro de programação interno, não diretamente para o usuário.
            throw new PersistenceException("Validação interna falhou: Placa não pode ser nula ou vazia ao tentar salvar no DAO.");
        }
        pstmt.setString(1, veiculo.getPlaca().trim().toUpperCase());

        pstmt.setInt(2, veiculo.getIdCategoriaVeiculo());
        pstmt.setString(3, veiculo.getModelo());
        pstmt.setString(4, veiculo.getMarca());

        if (veiculo.getAnoFabricacao() != null) {
            pstmt.setInt(5, veiculo.getAnoFabricacao());
        } else {
            pstmt.setNull(5, Types.INTEGER);
        }

        pstmt.setString(6, veiculo.getCor());

        String chassi = veiculo.getChassi();
        if (chassi != null && !chassi.trim().isEmpty()) {
            pstmt.setString(7, chassi.trim().toUpperCase());
        } else {
            pstmt.setNull(7, Types.VARCHAR);
        }

        String renavam = veiculo.getRenavam();
        if (renavam != null && !renavam.trim().isEmpty()) {
            pstmt.setString(8, renavam.trim().toUpperCase());
        } else {
            pstmt.setNull(8, Types.VARCHAR);
        }

        pstmt.setString(9, veiculo.getStatusVeiculo());
        pstmt.setString(10, veiculo.getObservacoes());

        // System.out.println("--- [VeiculoDAO.salvar] Executando INSERT SQL ---"); // Log de depuração
        int affectedRows = pstmt.executeUpdate();
        // System.out.println("--- [VeiculoDAO.salvar] Linhas afetadas: " + affectedRows + " ---"); // Log de depuração

        if (affectedRows == 0) {
            throw new PersistenceException("Falha ao salvar veículo, nenhuma linha afetada. Verifique os dados.");
        }

    } catch (SQLException e) {
        String detailedErrorMessage = e.getMessage();
        String sqlState = e.getSQLState();
        int errorCode = e.getErrorCode();

        

        if ("23000".equals(sqlState)) {
            if (errorCode == 1062 || (detailedErrorMessage != null && detailedErrorMessage.toLowerCase().contains("duplicate entry"))) {
               
                throw new PersistenceException("Não foi possível salvar o veículo: Já existe um veículo com a mesma Placa, Chassi ou Renavam.", e);
            } else if (errorCode == 1452) {
                throw new PersistenceException("Não foi possível salvar o veículo: A Categoria do Veículo informada não existe.", e);
            } else {
                throw new PersistenceException("Não foi possível salvar o veículo devido a um problema de integridade dos dados. [Detalhe DB: " + detailedErrorMessage + "]", e);
            }
        }
        throw new PersistenceException("Ocorreu um erro inesperado ao tentar salvar o veículo. Por favor, tente novamente. [Detalhe DB: " + detailedErrorMessage + "]", e);
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
            pstmt.setString(6, veiculo.getChassi() != null ? veiculo.getChassi().trim() : null);
            pstmt.setString(7, veiculo.getRenavam() != null ? veiculo.getRenavam().trim() : null);
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