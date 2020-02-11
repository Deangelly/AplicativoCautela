package com.unasusam.appcautela.DAO;

import java.io.Serializable;
import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "cautelas_table")
@TypeConverters({Converters.class})
public class CautelasEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String materia;
    private String professor;
    private String data;
    private String monitores;
    private String local;
    private List<DadosEmprestimo> emprestimos;

    public CautelasEntity( String materia, String professor, String data, String monitores, String local, List<DadosEmprestimo> emprestimos) {
        this.materia = materia;
        this.professor = professor;
        this.data = data;
        this.monitores = monitores;
        this.local = local;
        this.emprestimos = emprestimos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMonitores() {
        return monitores;
    }

    public void setMonitores(String monitores) {
        this.monitores = monitores;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public List<DadosEmprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<DadosEmprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
}
