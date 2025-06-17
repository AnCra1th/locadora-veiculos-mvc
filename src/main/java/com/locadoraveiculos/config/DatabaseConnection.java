package com.locadoraveiculos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/locadora_veiculos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection connection = null;

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
              
            }
        } catch (SQLException e) {
            System.err.println("Falha ao conectar ao banco de dados: " + e.getMessage());
           
            throw e;
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }

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