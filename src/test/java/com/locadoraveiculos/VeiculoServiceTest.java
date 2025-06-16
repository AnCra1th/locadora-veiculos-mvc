package com.locadoraveiculos;
import com.locadoraveiculos.dao.VeiculoDAO;
import com.locadoraveiculos.model.Veiculo;
import com.locadoraveiculos.service.VeiculoService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

public class VeiculoServiceTest {

    VeiculoDAO veiculoDAO;
    VeiculoService veiculoService;

    @BeforeEach
    void setUp() {
        veiculoDAO = Mockito.mock(VeiculoDAO.class);
        veiculoService = new VeiculoService(veiculoDAO);
    }

    @Test
    @DisplayName("Teste: Adicionar Veículo Válido")
    void testAdicionarVeiculoValido() {
        Veiculo v = new Veiculo();
        v.setModelo("Civic");
        Assertions.assertTrue(veiculoService.adicionarVeiculo(v));
    }

    @Test
    @DisplayName("Teste: Adicionar Veículo Inválido (modelo vazio)")
    void testAdicionarVeiculoInvalido() {
        Veiculo v = new Veiculo();
        v.setModelo("");
        Assertions.assertFalse(veiculoService.adicionarVeiculo(v));
    }
}
