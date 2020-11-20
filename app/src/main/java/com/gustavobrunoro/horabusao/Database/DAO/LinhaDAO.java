package com.gustavobrunoro.horabusao.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gustavobrunoro.horabusao.Model.Linha;

import java.util.List;

@Dao
public interface LinhaDAO {

    @Query("SELECT * FROM Linha")
    List<Linha> getLinhaList ();

    @Query("SELECT * FROM Linha WHERE LinhaID  = :LinhaID ")
    Linha getLinha (int LinhaID);

    @Query("SELECT * FROM Linha WHERE Favorita = 1 ")
    List<Linha> getLinhaFavoritasList ();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLinha (Linha linha);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLinhaList (List<Linha> linhas);

    @Update
    void updateLinha (Linha linha);
}
