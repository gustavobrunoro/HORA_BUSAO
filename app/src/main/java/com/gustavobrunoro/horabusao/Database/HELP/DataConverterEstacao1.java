package com.gustavobrunoro.horabusao.Database.HELP;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gustavobrunoro.horabusao.Model.Estacao;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverterEstacao1 {

    @TypeConverter
    public String fromList(Estacao estacaos) {
        if (estacaos == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Estacao>() {}.getType();
        String json = gson.toJson(estacaos, type);
        return json;
    }

    @TypeConverter
    public Estacao toList(String lista) {
        if (lista == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Estacao>() {}.getType();
        Estacao estacaos = gson.fromJson(lista, type);
        return estacaos;
    }

}
