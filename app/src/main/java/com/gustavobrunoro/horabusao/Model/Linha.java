package com.gustavobrunoro.horabusao.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.gustavobrunoro.horabusao.Database.HELP.DataConverterItinerario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Linha implements Serializable {

    @PrimaryKey
    private int LinhaID;
    private int Numero;
    private String Descricao;
    @TypeConverters(DataConverterItinerario.class)
    private List<Itinerario> Itinerarios = new ArrayList<>();
    private int Favorita;

    public Linha () {
    }

    public int getLinhaID () {
        return LinhaID;
    }

    public void setLinhaID (int linhaID) {
        LinhaID = linhaID;
    }

    public int getNumero () {
        return Numero;
    }

    public void setNumero (int numero) {
        Numero = numero;
    }

    public String getDescricao () {
        return Descricao;
    }

    public void setDescricao (String descricao) {
        Descricao = descricao;
    }

    public List<Itinerario> getItinerarios () {
        return Itinerarios;
    }

    public void setItinerarios (List<Itinerario> itinerarios) {
        this.Itinerarios = itinerarios;
    }

    public int getFavorita () {
        return Favorita;
    }

    public void setFavorita (int favorita) {
        Favorita = favorita;
    }
}
