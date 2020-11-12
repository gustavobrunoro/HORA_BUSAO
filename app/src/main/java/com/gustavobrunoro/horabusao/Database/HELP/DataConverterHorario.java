package com.gustavobrunoro.horabusao.Database.HELP;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gustavobrunoro.horabusao.Model.Horarios;
import com.gustavobrunoro.horabusao.Model.Itinerario;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverterHorario {

    @TypeConverter
    public String fromList(List<Horarios> horarios) {
        if (horarios == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Horarios>>() {}.getType();
        String json = gson.toJson(horarios, type);
        return json;
    }

    @TypeConverter
    public List<Horarios> toList(String lista) {
        if (lista == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Horarios>>() {}.getType();
        List<Horarios> horariosList = gson.fromJson(lista, type);
        return horariosList;
    }

}
