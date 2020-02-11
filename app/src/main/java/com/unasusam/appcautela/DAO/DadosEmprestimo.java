package com.unasusam.appcautela.DAO;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "dados_table")
public class DadosEmprestimo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public String matricula;

    private String nome;

    private String email;

    private String unidade;

    private String codCurso;

    private String cautela;

    private String nomeCurso;

    private String idUnico;

    @Ignore
    public DadosEmprestimo(){

    }
    public DadosEmprestimo(String matricula, String nome, String email, String unidade, String codCurso, String nomeCurso, String tablet, String descricao, String horaDoEmprestimo, String horaDaDevolucao, String cautela, String idUnico) {

        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.unidade = unidade;
        this.codCurso = codCurso;
        this.nomeCurso = nomeCurso;
        this.tablet = tablet;
        this.descricao = descricao;
        this.horaDoEmprestimo = horaDoEmprestimo;
        this.horaDaDevolucao = horaDaDevolucao;
        this.cautela = cautela;
        this.idUnico = idUnico;
    }

    private String tablet;
    private String descricao;
    private String horaDoEmprestimo;
    private String horaDaDevolucao;

    public String getHoraDoEmprestimo() {
        return horaDoEmprestimo;
    }

    public void setHoraDoEmprestimo(String horaDoEmprestimo) {
        this.horaDoEmprestimo = horaDoEmprestimo;
    }

    public void setHoraDaDevolucao(String horaDaDevolucao) {
        this.horaDaDevolucao = horaDaDevolucao;
    }

    public String getHoraDaDevolucao(){
        return horaDaDevolucao;
    }

    public String getTablet() {
        return tablet;
    }

    public void setTablet(String tablet) {
        this.tablet = tablet;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getCodCurso() {
        return codCurso;
    }

    public void setCodCurso(String codCurso) {
        this.codCurso = codCurso;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCautela() {
        return cautela;
    }

    public void setCautela(String cautela) {
        this.cautela = cautela;
    }

    public String getIdUnico() {
        return idUnico;
    }

    public void setIdUnico(String idUnico) {
        this.idUnico = idUnico;
    }
}
