package com.locadoraveiculos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por estabelecer e gerenciar a conexão com o banco de dados MySQL.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/locadora_veiculos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root"; // SEU USUÁRIO DO MYSQL
    private static final String PASSWORD = "root"; // SUA SENHA DO MYSQL

    private static Connection connection = null;

    /**
     * Obtém uma conexão com o banco de dados.
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    System.err.println("Driver MySQL não encontrado. Verifique o classpath e a dependência no pom.xml.");
                    throw new SQLException("Driver MySQL não encontrado", e);
                }
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                // System.out.println("Conexão com o banco de dados estabelecida com sucesso!"); // Comentado para não poluir console na operação normal
            }
        } catch (SQLException e) {
            System.err.println("Falha ao conectar ao banco de dados: " + e.getMessage());
            // e.printStackTrace(); // Descomente para depuração detalhada se necessário
            throw e;
        }
        return connection;
    }

    /**
     * Fecha a conexão ativa com o banco de dados.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    // System.out.println("Conexão com o banco de dados fechada."); // Comentado
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }

    // Método main para teste rápido da conexão (opcional)
    public static void main(String[] args) {
        try {
            Connection connTest = DatabaseConnection.getConnection();
            if (connTest != null && !connTest.isClosed()) {
                System.out.println("Teste de conexão bem-sucedido!");
            } else {
                System.err.println("Falha no teste de conexão.");
            }
        } catch (SQLException e) {
            System.err.println("Exceção durante o teste de conexão: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}