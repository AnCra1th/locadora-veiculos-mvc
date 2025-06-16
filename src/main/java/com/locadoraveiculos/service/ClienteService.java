package com.locadoraveiculos.service;

import com.locadoraveiculos.dao.ClienteDAO;
import com.locadoraveiculos.model.Cliente;
import com.locadoraveiculos.exception.PersistenceException;

public class ClienteService {
    private ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void cadastrarCliente(Cliente cliente) {
        if (cliente == null || cliente.getNome() == null) {
            throw new IllegalArgumentException("Cliente ou nome do cliente não pode ser nulo");
        }
        try {
            clienteDAO.salvar(cliente);
        } catch (Exception e) {
            throw new PersistenceException("Erro ao salvar cliente", e);
        }
    }

    // other methods...
}