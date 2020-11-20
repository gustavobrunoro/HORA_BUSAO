package com.gustavobrunoro.horabusao.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gustavobrunoro.horabusao.Model.LinhaFavorita;

import java.util.List;

@Dao
public interface LinhaFavoritaDAO {

    @Query("SELECT * FROM LinhaFavorita")
    List<LinhaFavorita> getLinhaList ();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserLinhaFavorita (LinhaFavorita linhaFavorita);

    @Delete
    void removeLinhaFavorita (LinhaFavorita linhaFavorita);
}
