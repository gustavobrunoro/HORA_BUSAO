package com.gustavobrunoro.horabusao.Model;

import androidx.room.TypeConverters;

import com.gustavobrunoro.horabusao.Database.HELP.DataConverterEstacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Itinerario implements Serializable {

    private int ItinerarioID;
    private int LinhaIDFk;
    private String Descricao;
    @TypeConverters(DataConverterEstacao.class)
    private List<Estacao> Estacoes = new ArrayList<>();

    public Itinerario () {
    }

    public int getItinerarioID () {
        return ItinerarioID;
    }

    public void setItinerarioID (int itinerarioID) {
        ItinerarioID = itinerarioID;
    }

    public int getLinhaIDFk () {
        return LinhaIDFk;
    }

    public void setLinhaIDFk (int linhaIDFk) {
        LinhaIDFk = linhaIDFk;
    }

    public String getDescricao () {
        return Descricao;
    }

    public void setDescricao (String descricao) {
        Descricao = descricao;
    }

    public List<Estacao> getEstacoes () {
        return Estacoes;
    }

    public void setEstacoes (List<Estacao> estacoes) {
        this.Estacoes = estacoes;
    }
}
