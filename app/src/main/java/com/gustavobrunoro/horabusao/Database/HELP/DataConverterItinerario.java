package com.gustavobrunoro.horabusao.Database.HELP;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gustavobrunoro.horabusao.Model.Itinerario;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverterItinerario {

    @TypeConverter
    public String fromList(List<Itinerario> itinerarios) {
        if (itinerarios == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Itinerario>>() {}.getType();
        String json = gson.toJson(itinerarios, type);
        return json;
    }

    @TypeConverter
    public List<Itinerario> toList(String lista) {
        if (lista == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Itinerario>>() {}.getType();
        List<Itinerario> itinerarios = gson.fromJson(lista, type);
        return itinerarios;
    }

}
