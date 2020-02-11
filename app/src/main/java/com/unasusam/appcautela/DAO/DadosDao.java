package com.unasusam.appcautela.DAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DadosDao {

    @Insert
    void insert(DadosEmprestimo dadosEmprestimo);

    @Update
    void update(CautelasEntity cautelasEntity);

    @Delete
    void delete(CautelasEntity cautelasEntity);

    @Insert
    void insertAll(CautelasEntity cautelasEntity);

    @Query("SELECT * FROM cautelas_table ORDER BY id DESC")
    LiveData<List<CautelasEntity>> getAllDados();


    @Query("DELETE FROM dados_table WHERE cautela = :string")
    void deleteCautela(String string);

}
