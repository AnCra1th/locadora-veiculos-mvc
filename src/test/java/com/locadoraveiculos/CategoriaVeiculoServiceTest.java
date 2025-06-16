package com.locadoraveiculos;
import com.locadoraveiculos.dao.CategoriaVeiculoDAO;
import com.locadoraveiculos.model.CategoriaVeiculo;
import com.locadoraveiculos.service.CategoriaVeiculoService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.math.BigDecimal;

public class CategoriaVeiculoServiceTest {

    CategoriaVeiculoDAO dao;
    CategoriaVeiculoService service;
    CategoriaVeiculo categoriaValida;

    @BeforeEach
    void setUp() {
        dao = Mockito.mock(CategoriaVeiculoDAO.class);
        service = new CategoriaVeiculoService(dao);
        
        categoriaValida = new CategoriaVeiculo();
        categoriaValida.setNomeCategoria("SUV");
        categoriaValida.setValorDiariaBase(new BigDecimal("200.00"));
    }

    @Test
    @DisplayName("Deve adicionar uma categoria válida")
    void testAdicionarCategoriaValida() {
        Assertions.assertTrue(service.adicionarCategoria(categoriaValida));
        Mockito.verify(dao).salvar(categoriaValida);
    }

    @Test
    @DisplayName("Não deve adicionar categoria com nome inválido")
    void testAdicionarCategoriaInvalida() {
        categoriaValida.setNomeCategoria(""); // Nome inválido
        Assertions.assertFalse(service.adicionarCategoria(categoriaValida));
    }
    
    @Test
    @DisplayName("Não deve adicionar categoria com valor da diária inválido")
    void testAdicionarCategoriaComDiariaInvalida() {
        categoriaValida.setValorDiariaBase(BigDecimal.ZERO); // Valor inválido
        Assertions.assertFalse(service.adicionarCategoria(categoriaValida));
    }
}