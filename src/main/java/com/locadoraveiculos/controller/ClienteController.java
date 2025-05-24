package com.locadoraveiculos.controller;

import com.locadoraveiculos.dao.ClienteDAO;
import com.locadoraveiculos.model.Cliente;
import java.util.List;

public class ClienteController {

    private ClienteDAO clienteDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }

    public void createCliente(Cliente cliente) {
        clienteDAO.save(cliente);
    }

    public Cliente readCliente(int id) {
        return clienteDAO.findById(id);
    }

    public List<Cliente> readAllClientes() {
        return clienteDAO.findAll();
    }

    public void updateCliente(Cliente cliente) {
        clienteDAO.update(cliente);
    }

    public void deleteCliente(int id) {
        clienteDAO.delete(id);
    }
}