package com.locadoraveiculos;


import com.locadoraveiculos.dao.ClienteDAO;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Cliente;
import com.locadoraveiculos.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteDAO clienteDAO;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteValido;

    @BeforeEach
    void setUp() {
        clienteValido = new Cliente("John Doe", "123.456.789-00", "123456789", "john.doe@example.com");
        clienteValido.setIdCliente(1);
    }

    @Test
    @DisplayName("Deve cadastrar um cliente com sucesso")
    void testeCadastrarClienteComSucesso() {
        assertDoesNotThrow(() -> clienteService.cadastrarCliente(clienteValido));

        verify(clienteDAO, times(1)).salvar(clienteValido);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar cliente com nome nulo")
    void testeCadastrarClienteComNomeNulo() {
        clienteValido.setNome(null);

        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.cadastrarCliente(clienteValido);
        });

        verify(clienteDAO, never()).salvar(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar PersistenceException quando o DAO falhar")
    void testeFalhaDoDAOAoSalvar() {
        doThrow(new PersistenceException("Erro de Teste no DB")).when(clienteDAO).salvar(clienteValido);

        assertThrows(PersistenceException.class, () -> {
            clienteService.cadastrarCliente(clienteValido);
        });

        verify(clienteDAO, times(1)).salvar(clienteValido);
    }
}