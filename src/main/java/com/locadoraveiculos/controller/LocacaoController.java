package com.locadoraveiculos.controller;

import com.locadoraveiculos.dao.LocacaoDAO;
import com.locadoraveiculos.model.Locacao;
import java.util.List;

public class LocacaoController {

    private LocacaoDAO locacaoDAO;

    public LocacaoController() {
        this.locacaoDAO = new LocacaoDAO();
    }

    public void createLocacao(Locacao locacao) {
        locacaoDAO.create(locacao);
    }

    public Locacao getLocacao(int id) {
        return locacaoDAO.read(id);
    }

    public List<Locacao> getAllLocacoes() {
        return locacaoDAO.readAll();
    }

    public void updateLocacao(Locacao locacao) {
        locacaoDAO.update(locacao);
    }

    public void deleteLocacao(int id) {
        locacaoDAO.delete(id);
    }
}