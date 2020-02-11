package com.unasusam.appcautela;

import com.unasusam.appcautela.DAO.DadosEmprestimo;

import java.util.List;

public class ConverterDados {

    List<DadosEmprestimo> dadosEmprestimoList;

    ConverterDados(){}
    ConverterDados(List<DadosEmprestimo> dadosEmprestimos){
        this.dadosEmprestimoList = dadosEmprestimos;
    }

    public List<DadosEmprestimo> getDadosEmprestimoList() {
        return dadosEmprestimoList;
    }

    public void setDadosEmprestimoList(List<DadosEmprestimo> dadosEmprestimoList) {
        this.dadosEmprestimoList = dadosEmprestimoList;
    }
}
