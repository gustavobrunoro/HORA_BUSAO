package com.gustavobrunoro.horabusao.Database.HELP;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gustavobrunoro.horabusao.Model.Estacao;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverterEstacao {

    @TypeConverter
    public String fromList(List<Estacao> estacaos) {
        if (estacaos == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Estacao>>() {}.getType();
        String json = gson.toJson(estacaos, type);
        return json;
    }

    @TypeConverter
    public List<Estacao> toList(String lista) {
        if (lista == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Estacao>>() {}.getType();
        List<Estacao> estacaos = gson.fromJson(lista, type);
        return estacaos;
    }

}
