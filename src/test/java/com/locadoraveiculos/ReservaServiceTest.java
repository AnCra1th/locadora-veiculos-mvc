package com.locadoraveiculos;
import com.locadoraveiculos.dao.ReservaDAO;
import com.locadoraveiculos.model.Reserva;
import com.locadoraveiculos.service.ReservaService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ReservaServiceTest {

    ReservaDAO dao;
    ReservaService service;
    Reserva reservaValida;

    @BeforeEach
    void setUp() {
        dao = Mockito.mock(ReservaDAO.class);
        service = new ReservaService(dao);

        // Cria uma reserva válida para os testes
        reservaValida = new Reserva();
        reservaValida.setIdCliente(1);
        reservaValida.setIdCategoriaVeiculo(1);
        reservaValida.setDataPrevistaRetirada(new Date());
        reservaValida.setDataPrevistaDevolucao(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));
    }

    @Test
    @DisplayName("Deve registrar uma reserva com dados válidos")
    void testRegistrarReservaValida() {
        Assertions.assertTrue(service.registrarReserva(reservaValida));
        // Verifica se o método 'salvar' do DAO foi chamado
        Mockito.verify(dao).salvar(reservaValida);
    }

    @Test
    @DisplayName("Não deve registrar reserva com ID de cliente inválido")
    void testRegistrarReservaInvalida() {
        reservaValida.setIdCliente(0); // Cliente inválido
        Assertions.assertFalse(service.registrarReserva(reservaValida));
    }
    
    @Test
    @DisplayName("Não deve registrar reserva com data de devolução anterior à retirada")
    void testRegistrarReservaComDataInvalida() {
        reservaValida.setDataPrevistaDevolucao(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))); // Data inválida
        Assertions.assertFalse(service.registrarReserva(reservaValida));
    }
}