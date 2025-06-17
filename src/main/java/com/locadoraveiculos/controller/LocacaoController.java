package com.locadoraveiculos.controller;

import com.locadoraveiculos.dao.LocacaoDAO;
import com.locadoraveiculos.dao.VeiculoDAO;
import com.locadoraveiculos.dao.ClienteDAO;
import com.locadoraveiculos.exception.PersistenceException;
import com.locadoraveiculos.model.Locacao;
import com.locadoraveiculos.model.Veiculo;
import com.locadoraveiculos.model.Cliente;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LocacaoController {
    private LocacaoDAO locacaoDAO;
    private VeiculoDAO veiculoDAO;
    private ClienteDAO clienteDAO;

    public LocacaoController() {
        this.locacaoDAO = new LocacaoDAO();
        this.veiculoDAO = new VeiculoDAO();
        this.clienteDAO = new ClienteDAO();
    }

    public boolean registrarLocacao(Locacao locacao) {
        try {
            Cliente cliente = clienteDAO.buscarPorId(locacao.getIdCliente());
            if (cliente == null) {
                System.err.println("Erro ao registrar locação: Cliente com ID " + locacao.getIdCliente() + " não encontrado.");
                return false;
            }

            Veiculo veiculo = veiculoDAO.buscarPorPlaca(locacao.getPlacaVeiculo());
            if (veiculo == null) {
                System.err.println("Erro ao registrar locação: Veículo com placa " + locacao.getPlacaVeiculo() + " não encontrado.");
                return false;
            }
            if (!"disponivel".equalsIgnoreCase(veiculo.getStatusVeiculo())) {
                System.err.println("Erro ao registrar locação: Veículo " + veiculo.getPlaca() + " não está disponível. Status: " + veiculo.getStatusVeiculo());
                return false;
            }

            if (locacao.getDataRetirada() == null || locacao.getDataPrevistaDevolucao() == null) {
                System.err.println("Erro ao registrar locação: Datas de retirada e devolução prevista são obrigatórias.");
                return false;
            }

            if (locacao.getDataPrevistaDevolucao().before(locacao.getDataRetirada())) {
                 System.err.println("Erro ao registrar locação: Data de devolução prevista não pode ser anterior à data de retirada.");
                return false;
            }
            
            if (locacao.getValorDiariaLocacao() == null || locacao.getValorDiariaLocacao().compareTo(BigDecimal.ZERO) <= 0) {
                 System.err.println("Erro ao registrar locação: Valor da diária deve ser maior que zero.");
                return false;
            }

            long diffInMillies = Math.abs(locacao.getDataPrevistaDevolucao().getTime() - locacao.getDataRetirada().getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diffInDays == 0) diffInDays = 1;
            
            BigDecimal dias = new BigDecimal(diffInDays);
            locacao.setValorTotalPrevisto(locacao.getValorDiariaLocacao().multiply(dias));

            locacao.setStatusLocacao("ativa");
            locacaoDAO.salvar(locacao);

            veiculo.setStatusVeiculo("locado");
            veiculoDAO.atualizar(veiculo);

            System.out.println("INFO: Locação registrada com sucesso! ID: " + locacao.getIdLocacao() + 
                               " para o cliente " + cliente.getNome() + " com o veículo " + veiculo.getMarca() + " " + veiculo.getModelo());
            return true;

        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao registrar locação: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("ERRO Controller: Erro inesperado ao registrar locação: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Locacao buscarLocacaoPorId(int id) {
        try {
            return locacaoDAO.buscarPorId(id);
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: " + e.getMessage());
            return null;
        }
    }

    public List<Locacao> listarTodasLocacoes() {
        try {
            return locacaoDAO.listarTodas();
        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao listar locações: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean finalizarLocacao(int idLocacao, Date dataEfetivaDevolucao, String observacoesDevolucao) {
        try {
            Locacao locacao = locacaoDAO.buscarPorId(idLocacao);
            if (locacao == null) {
                System.err.println("Erro: Locação ID " + idLocacao + " não encontrada.");
                return false;
            }
            if (!"ativa".equalsIgnoreCase(locacao.getStatusLocacao())) {
                 System.err.println("Erro: Locação ID " + idLocacao + " não está ativa. Status atual: " + locacao.getStatusLocacao());
                return false;
            }
            if (dataEfetivaDevolucao == null) {
                System.err.println("Erro: Data efetiva de devolução é obrigatória.");
                return false;
            }
            if (dataEfetivaDevolucao.before(locacao.getDataRetirada())) {
                System.err.println("Erro: Data efetiva de devolução não pode ser anterior à data de retirada.");
                return false;
            }

            locacao.setDataEfetivaDevolucao(dataEfetivaDevolucao);
            locacao.setObservacoesDevolucao(observacoesDevolucao);
            locacao.setStatusLocacao("finalizada");

            long diffPrevistaMillis = Math.abs(locacao.getDataPrevistaDevolucao().getTime() - locacao.getDataRetirada().getTime());
            long diasPrevistos = TimeUnit.DAYS.convert(diffPrevistaMillis, TimeUnit.MILLISECONDS);
            if(diasPrevistos == 0) diasPrevistos = 1;

            long diffEfetivaMillis = Math.abs(dataEfetivaDevolucao.getTime() - locacao.getDataRetirada().getTime());
            long diasEfetivos = TimeUnit.DAYS.convert(diffEfetivaMillis, TimeUnit.MILLISECONDS);
            if(diasEfetivos == 0) diasEfetivos = 1;
            
            BigDecimal valorBase = locacao.getValorDiariaLocacao().multiply(new BigDecimal(diasEfetivos));
            
            if (dataEfetivaDevolucao.after(locacao.getDataPrevistaDevolucao())) {
                long diasAtrasoMillis = Math.abs(dataEfetivaDevolucao.getTime() - locacao.getDataPrevistaDevolucao().getTime());
                long diasAtraso = TimeUnit.DAYS.convert(diasAtrasoMillis, TimeUnit.MILLISECONDS);
                BigDecimal multaPorDia = locacao.getValorDiariaLocacao().multiply(new BigDecimal("0.20"));
                locacao.setValorMultaAtraso(multaPorDia.multiply(new BigDecimal(diasAtraso)));
            } else {
                locacao.setValorMultaAtraso(BigDecimal.ZERO);
            }
            
            locacao.setValorTotalFinal(valorBase.add(locacao.getValorMultaAtraso()).add(locacao.getValorSeguro()));

            locacaoDAO.atualizar(locacao);

            Veiculo veiculo = veiculoDAO.buscarPorPlaca(locacao.getPlacaVeiculo());
            if (veiculo != null) {
                veiculo.setStatusVeiculo("disponivel");
                veiculoDAO.atualizar(veiculo);
            }
            System.out.println("INFO: Locação ID " + idLocacao + " finalizada. Valor final: " + locacao.getValorTotalFinal());
            return true;

        } catch (PersistenceException e) {
            System.err.println("ERRO Controller: Erro ao finalizar locação: " + e.getMessage());
            return false;
        }
    }
}