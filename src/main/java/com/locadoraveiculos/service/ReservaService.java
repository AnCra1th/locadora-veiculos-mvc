package com.locadoraveiculos.service;

import com.locadoraveiculos.dao.ReservaDAO;
import com.locadoraveiculos.model.Reserva;

public class ReservaService {

    private ReservaDAO reservaDAO;

    public ReservaService(ReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
    }

    /**
     * Valida e registra uma nova reserva.
     * @param reserva A reserva a ser registrada.
     * @return true se a reserva for válida e salva, false caso contrário.
     */
    public boolean registrarReserva(Reserva reserva) {
        // A validação deve ocorrer com os IDs, que é o que o model possui.
        if (reserva == null || reserva.getIdCliente() <= 0 || reserva.getIdCategoriaVeiculo() <= 0) {
            System.err.println("SERVIÇO ERRO: Cliente e Categoria de Veículo são obrigatórios para a reserva.");
            return false;
        }

        // Outras validações, como de datas, poderiam ser adicionadas aqui.
        if (reserva.getDataPrevistaDevolucao().before(reserva.getDataPrevistaRetirada())) {
            System.err.println("SERVIÇO ERRO: Data de devolução não pode ser anterior à data de retirada.");
            return false;
        }
        
        try {
            reservaDAO.salvar(reserva);
            return true;
        } catch (Exception e) {
            System.err.println("SERVIÇO ERRO: Falha ao salvar reserva: " + e.getMessage());
            return false;
        }
    }
}