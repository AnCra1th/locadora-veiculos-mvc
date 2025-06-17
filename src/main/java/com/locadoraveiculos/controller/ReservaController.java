package com.locadoraveiculos.controller;

import com.locadoraveiculos.dao.ReservaDAO;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ReservaController {
    private ReservaDAO reservaDAO;

    public ReservaController() {
        this.reservaDAO = new ReservaDAO();
    }

    public boolean criarReserva(Reserva reserva) {
        try {
            if (reserva.getIdCliente() == 0 || reserva.getIdCategoriaVeiculo() == 0) {
                System.err.println("Erro de validação: Cliente e Categoria do Veículo são obrigatórios.");
                return false;
            }
            if (reserva.getDataPrevistaRetirada() == null || reserva.getDataPrevistaDevolucao() == null) {
                System.err.println("Erro de validação: Datas de retirada e devolução previstas são obrigatórias.");
                return false;
            }
            if (reserva.getDataPrevistaDevolucao().before(reserva.getDataPrevistaRetirada())) {
                System.err.println("Erro de validação: Data de devolução não pode ser anterior à data de retirada.");
                return false;
            }

            if (reserva.getStatusReserva() == null || reserva.getStatusReserva().isEmpty()) {
                reserva.setStatusReserva("confirmada");
            }

            reservaDAO.salvar(reserva);
            System.out.println("INFO: Reserva criada com sucesso! ID: " + reserva.getIdReserva());
            return true;
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao criar reserva: " + e.getMessage());
            return false;
        }
    }

    public Reserva buscarReservaPorId(int idReserva) {
        try {
            return reservaDAO.buscarPorId(idReserva);
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao buscar reserva: " + e.getMessage());
            return null;
        }
    }

    public List<Reserva> listarTodasReservas() {
        try {
            return reservaDAO.listarTodas();
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao listar reservas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean cancelarReserva(int idReserva) {
        try {
            Reserva reserva = reservaDAO.buscarPorId(idReserva);
            if (reserva == null) {
                System.err.println("ERRO: Reserva com ID " + idReserva + " não encontrada.");
                return false;
            }
            
            String statusAtual = reserva.getStatusReserva();
            if ("utilizada".equalsIgnoreCase(statusAtual) || "cancelada_cliente".equalsIgnoreCase(statusAtual)) {
                System.err.println("ERRO: Reserva não pode ser cancelada. Status atual: " + statusAtual);
                return false;
            }
            
            reserva.setStatusReserva("cancelada_cliente");
            reservaDAO.atualizar(reserva);
            System.out.println("INFO: Reserva ID " + idReserva + " cancelada com sucesso.");
            return true;
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao cancelar reserva: " + e.getMessage());
            return false;
        }
    }
    
    public boolean utilizarReserva(int idReserva) {
        try {
            Reserva reserva = reservaDAO.buscarPorId(idReserva);
            if (reserva == null) {
                return false;
            }
            reserva.setStatusReserva("utilizada");
            reservaDAO.atualizar(reserva);
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }
}