package com.locadoraveiculos;

import com.locadoraveiculos.dao.LocacaoDAO;
import com.locadoraveiculos.model.Locacao;
import com.locadoraveiculos.service.LocacaoService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LocacaoServiceTest {

    LocacaoDAO dao;
    LocacaoService service;
    Locacao locacaoValida;

    @BeforeEach
    void setUp() {
        dao = Mockito.mock(LocacaoDAO.class);
        service = new LocacaoService(dao);
        
        
        locacaoValida = new Locacao(1, "ABC1234", 1, new Date(), new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)), new BigDecimal("150.0"));
    }

    @Test
    @DisplayName("Deve registrar uma locação com dados válidos")
    void testRegistrarLocacaoValida() {
        Assertions.assertTrue(service.registrarLocacao(locacaoValida));
        
        Mockito.verify(dao).salvar(locacaoValida);
    }

    @Test
    @DisplayName("Não deve registrar locação com placa de veículo nula")
    void testRegistrarLocacaoInvalida() {
        locacaoValida.setPlacaVeiculo(null); 
        Assertions.assertFalse(service.registrarLocacao(locacaoValida));
    }
    
    @Test
    @DisplayName("Não deve registrar locação com objeto nulo")
    void testRegistrarLocacaoNula() {
        Assertions.assertFalse(service.registrarLocacao(null));
    }
}