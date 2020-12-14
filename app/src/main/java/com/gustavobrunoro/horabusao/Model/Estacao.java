package com.gustavobrunoro.horabusao.Model;

import androidx.room.TypeConverters;

import com.gustavobrunoro.horabusao.Database.HELP.DataConverterHorario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Estacao implements Serializable {

    private int EstacaoID;
    private int LinhaIDFK;
    private int ItinerarioIDFK;
    private String Descricao;
    @TypeConverters(DataConverterHorario.class)
    private List<Horarios> Horarios = new ArrayList<>();

    public Estacao () {
    }

    public int getEstacaoID () {
        return EstacaoID;
    }

    public void setEstacaoID (int estacaoID) {
        EstacaoID = estacaoID;
    }

    public int getLinhaIDFK () {
        return LinhaIDFK;
    }

    public void setLinhaIDFK (int linhaIDFK) {
        LinhaIDFK = linhaIDFK;
    }

    public int getItinerarioIDFK () {
        return ItinerarioIDFK;
    }

    public void setItinerarioIDFK (int itinerarioIDFK) {
        ItinerarioIDFK = itinerarioIDFK;
    }

    public String getDescricao () {
        return Descricao;
    }

    public void setDescricao (String descricao) {
        Descricao = descricao;
    }

    public List<Horarios> getHorarios () {
        return Horarios;
    }

    public void setHorarios (List<Horarios> horarios) {
        Horarios = horarios;
    }
}
