package com.locadoraveiculos.controller;

import com.locadoraveiculos.dao.ClienteDAO;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    private ClienteDAO clienteDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }

    public boolean cadastrarCliente(Cliente cliente) {
        try {
            if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
                System.err.println("Erro de validação: Nome do cliente não pode ser vazio.");
                return false;
            }
            if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
                System.err.println("Erro de validação: CPF do cliente não pode ser vazio.");
                return false;
            }
            if (cliente.getCnh() == null || cliente.getCnh().trim().isEmpty()) {
                System.err.println("Erro de validação: CNH do cliente não pode ser vazia.");
                return false;
            }
            // Adicionar mais validações se necessário (ex: formato de CPF, CNH, email)

            clienteDAO.salvar(cliente);
            System.out.println("INFO: Cliente '" + cliente.getNome() + "' cadastrado com sucesso! ID: " + cliente.getIdCliente());
            return true;
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao cadastrar cliente: " + e.getMessage());
            // e.getCause().printStackTrace(); // Para ver o stack trace da SQLException original
            return false;
        }
    }

    public Cliente buscarClientePorId(int id) {
        try {
            return clienteDAO.buscarPorId(id);
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao buscar cliente por ID " + id + ": " + e.getMessage());
            return null;
        }
    }

    public List<Cliente> listarTodosClientes() {
        try {
            return clienteDAO.listarTodos();
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao listar clientes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean atualizarCliente(Cliente cliente) {
        try {
            if (cliente.getIdCliente() == 0) {
                 System.err.println("Erro de validação: ID do cliente inválido para atualização.");
                return false;
            }
            // Adicionar validações para os campos a serem atualizados, se necessário
            clienteDAO.atualizar(cliente);
            System.out.println("INFO: Cliente ID " + cliente.getIdCliente() + " atualizado com sucesso!");
            return true;
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao atualizar cliente ID " + cliente.getIdCliente() + ": " + e.getMessage());
            return false;
        }
    }

    public boolean excluirCliente(int id) {
        try {
            if (id == 0) {
                System.err.println("Erro de validação: ID do cliente inválido para exclusão.");
                return false;
            }
            clienteDAO.excluir(id);
            System.out.println("INFO: Cliente ID " + id + " excluído (ou tentativa de exclusão registrada).");
            return true;
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: " + e.getMessage()); // A mensagem já vem formatada do DAO
            return false;
        }
    }
}