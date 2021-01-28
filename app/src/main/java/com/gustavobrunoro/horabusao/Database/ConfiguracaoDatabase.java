package com.gustavobrunoro.horabusao.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.gustavobrunoro.horabusao.Database.DAO.LinhaDAO;
import com.gustavobrunoro.horabusao.Database.DAO.LinhaFavoritaDAO;
import com.gustavobrunoro.horabusao.Database.HELP.DataConverterEstacao1;
import com.gustavobrunoro.horabusao.Database.HELP.DataConverterHorario;
import com.gustavobrunoro.horabusao.Database.HELP.DataConverterItinerario;
import com.gustavobrunoro.horabusao.Database.HELP.DataConverterEstacao;
import com.gustavobrunoro.horabusao.Database.HELP.RoomTypeConverters;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.Model.LinhaFavorita;

@TypeConverters({RoomTypeConverters.class, DataConverterItinerario.class, DataConverterHorario.class, DataConverterEstacao.class, DataConverterEstacao1.class})
@Database(entities = {Linha.class, LinhaFavorita.class}, version = 4)
public abstract class ConfiguracaoDatabase extends RoomDatabase {

    private static final  String DB_NAME = "HoraOnibus";
    private static ConfiguracaoDatabase instance;

    public static synchronized ConfiguracaoDatabase getInstance(Context context ){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ConfiguracaoDatabase.class,DB_NAME)
                           .fallbackToDestructiveMigration()
                           .build();
        }
        return instance;
    }

    public abstract LinhaDAO linhaDAO();
    public abstract LinhaFavoritaDAO linhaFavoritaDAO();

}
