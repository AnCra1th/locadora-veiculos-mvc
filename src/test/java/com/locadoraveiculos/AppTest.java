// src/test/java/com/locadoraveiculos/AppTest.java
package com.locadoraveiculos;

// Imports necessários para os testes
import com.locadoraveiculos.config.DatabaseConnection;
import com.locadoraveiculos.dao.CategoriaVeiculoDAO;
import com.locadoraveiculos.dao.ClienteDAO;
import com.locadoraveiculos.dao.ReservaDAO;
import com.locadoraveiculos.dao.VeiculoDAO;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.CategoriaVeiculo;
import com.locadoraveiculos.model.Cliente;
import com.locadoraveiculos.model.Reserva;
import com.locadoraveiculos.model.Veiculo;

// Imports do JUnit 5, o framework de testes
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// Import estático para usar métodos de asserção diretamente (ex: assertEquals)
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe principal de testes para a aplicação Locadora de Veículos.
 * Contém alguns exemplos de testes de integração para as classes DAO.
 * Utiliza classes aninhadas (@Nested) para organizar os testes por funcionalidade.
 */
// @TestInstance define que uma única instância desta classe será usada para todos os testes,
// o que permite que métodos com @BeforeAll não precisem ser estáticos.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppTest {

    // Declaração dos DAOs que serão testados
    private ClienteDAO clienteDAO;
    private VeiculoDAO veiculoDAO;
    private CategoriaVeiculoDAO categoriaVeiculoDAO;
    private ReservaDAO reservaDAO;

    // O método com @BeforeAll é executado uma única vez antes de todos os testes da classe.
    @BeforeAll
    void setupGlobal() {
        // Instancia todos os DAOs uma vez, o que é eficiente.
        clienteDAO = new ClienteDAO();
        veiculoDAO = new VeiculoDAO();
        categoriaVeiculoDAO = new CategoriaVeiculoDAO();
        reservaDAO = new ReservaDAO();
        System.out.println("DAOs instanciados para os testes.");
    }

    // Este é um método auxiliar para limpar as tabelas do banco de dados.
    // É crucial para garantir que os testes sejam independentes (um não afeta o outro).
    private void limparTabelas(String... tabelas) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            // Desabilita a verificação de chaves estrangeiras temporariamente para permitir o DELETE.
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            for (String tabela : tabelas) {
                stmt.executeUpdate("DELETE FROM " + tabela);
            }
            // Reabilita a verificação de chaves estrangeiras.
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            // Se a limpeza falhar, o ambiente de teste está comprometido, então lançamos uma exceção.
            throw new RuntimeException("Falha ao limpar tabelas de teste.", e);
        }
    }

    // A anotação @Nested permite agrupar testes relacionados em uma classe interna.
    // @DisplayName define um nome legível para o grupo de testes.
    @Nested
    @DisplayName("Testes para ClienteDAO")
    class ClienteDAOTests {

        // O método com @BeforeEach é executado antes de CADA teste dentro deste grupo.
        @BeforeEach
        void setUp() {
            // Garante que a tabela cliente esteja vazia antes de cada teste de cliente.
            limparTabelas("cliente");
        }

        @Test
        @DisplayName("Deve salvar um cliente e depois buscá-lo com sucesso")
        void testSalvarEBuscarCliente() {
            // Arrange (Organizar): Prepara os dados de entrada.
            Cliente cliente = new Cliente("João da Silva", "12345678901", "CNH12345", "joao@teste.com");

            // Act (Agir): Executa a ação que queremos testar.
            clienteDAO.salvar(cliente);
            Cliente clienteBuscado = clienteDAO.buscarPorId(cliente.getIdCliente());

            // Assert (Verificar): Confere se o resultado foi o esperado.
            assertNotNull(clienteBuscado, "O cliente buscado não deveria ser nulo.");
            assertEquals("João da Silva", clienteBuscado.getNome(), "O nome do cliente não corresponde.");
        }
    }

    @Nested
    @DisplayName("Testes para VeiculoDAO")
    class VeiculoDAOTests {

        private CategoriaVeiculo categoriaTeste;

        @BeforeEach
        void setUp() {
            // Limpa as tabelas relevantes antes de cada teste de veículo.
            limparTabelas("veiculo", "categoria_veiculo");

            // Arrange: Cria uma Categoria de Veículo, pois é uma dependência obrigatória para criar um Veiculo.
            categoriaTeste = new CategoriaVeiculo("Popular Teste", new BigDecimal("99.90"));
            categoriaVeiculoDAO.salvar(categoriaTeste);
            assertTrue(categoriaTeste.getIdCategoriaVeiculo() > 0, "A categoria de teste deve ser salva e ter um ID.");
        }

        @Test
        @DisplayName("Deve salvar um veículo e depois buscá-lo com sucesso")
        void testSalvarEBuscarVeiculo() {
            // Arrange
            Veiculo veiculo = new Veiculo("TST0001", categoriaTeste.getIdCategoriaVeiculo(), "Modelo TST", "Marca TST", "disponivel");
            veiculo.setChassi("CHASSI_UNICO_123");
            
            // Act
            veiculoDAO.salvar(veiculo);
            Veiculo veiculoBuscado = veiculoDAO.buscarPorPlaca("TST0001");

            // Assert
            assertNotNull(veiculoBuscado, "O veículo buscado não deveria ser nulo.");
            assertEquals("Modelo TST", veiculoBuscado.getModelo());
            assertEquals("CHASSI_UNICO_123", veiculoBuscado.getChassi());
        }

        @Test
        @DisplayName("Deve lançar exceção ao salvar veículo com chave estrangeira de categoria inexistente")
        void testSalvarVeiculoComFKInexistenteLancaExcecao() {
            // Arrange
            int idCategoriaInexistente = 9999;
            Veiculo veiculo = new Veiculo("FKF9999", idCategoriaInexistente, "Modelo Fantasma", "Marca", "disponivel");

            // Act & Assert: Verifica se a exceção correta é lançada ao tentar uma operação inválida.
            assertThrows(PersistenceException.class, () -> {
                veiculoDAO.salvar(veiculo);
            }, "Deveria lançar PersistenceException por violação de chave estrangeira.");
        }
    }

    @Nested
    @DisplayName("Testes para ReservaDAO")
    class ReservaDAOTests {

        private Cliente clienteTeste;
        private CategoriaVeiculo categoriaTeste;

        @BeforeEach
        void setUp() {
            // Limpa todas as tabelas que uma reserva pode depender.
            limparTabelas("reserva", "locacao", "veiculo", "cliente", "categoria_veiculo");

            // Arrange: Cria todas as dependências necessárias para uma reserva.
            clienteTeste = new Cliente("Cliente Reserva", "RES987654321", "CNH_RES", "reserva@teste.com");
            clienteDAO.salvar(clienteTeste);
            assertTrue(clienteTeste.getIdCliente() > 0);

            categoriaTeste = new CategoriaVeiculo("SUV Reserva", new BigDecimal("250.00"));
            categoriaVeiculoDAO.salvar(categoriaTeste);
            assertTrue(categoriaTeste.getIdCategoriaVeiculo() > 0);
        }

        @Test
        @DisplayName("Deve salvar uma reserva e depois buscá-la com sucesso")
        void testSalvarEBuscarReserva() {
            // Arrange
            Date retirada = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2));
            Date devolucao = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5));
            Reserva reserva = new Reserva();
            reserva.setIdCliente(clienteTeste.getIdCliente());
            reserva.setIdCategoriaVeiculo(categoriaTeste.getIdCategoriaVeiculo());
            reserva.setDataPrevistaRetirada(retirada);
            reserva.setDataPrevistaDevolucao(devolucao);
            reserva.setStatusReserva("confirmada");

            // Act
            reservaDAO.salvar(reserva);
            Reserva reservaBuscada = reservaDAO.buscarPorId(reserva.getIdReserva());

            // Assert
            assertNotNull(reservaBuscada, "A reserva buscada não deveria ser nula.");
            assertEquals(clienteTeste.getIdCliente(), reservaBuscada.getIdCliente());
            assertEquals("confirmada", reservaBuscada.getStatusReserva());
        }
    }
}
