package com.gustavobrunoro.horabusao.Model;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.gustavobrunoro.horabusao.Database.HELP.DataConverterEstacao1;

import java.io.Serializable;

@Entity(  primaryKeys = {"LinhaIDFK","ItinerarioIDFK","EstacaoIDFK"} )
public class LinhaFavorita implements Serializable {

    private int LinhaFavoritaID;
    private int LinhaIDFK;
    private int Numero;
    private String DescricaoLinha;
    private int ItinerarioIDFK;
    private String DescricaoIntinerario;
    @TypeConverters(DataConverterEstacao1.class)
    private int EstacaoIDFK;
    private Estacao estacao = new Estacao();

    public LinhaFavorita () {
    }

    public int getLinhaFavoritaID () {
        return LinhaFavoritaID;
    }

    public void setLinhaFavoritaID (int linhaFavoritaID) {
        LinhaFavoritaID = linhaFavoritaID;
    }

    public int getLinhaIDFK () {
        return LinhaIDFK;
    }

    public void setLinhaIDFK (int linhaIDFK) {
        LinhaIDFK = linhaIDFK;
    }

    public int getNumero () {
        return Numero;
    }

    public void setNumero (int numero) {
        Numero = numero;
    }

    public String getDescricaoLinha () {
        return DescricaoLinha;
    }

    public void setDescricaoLinha (String descricaoLinha) {
        DescricaoLinha = descricaoLinha;
    }

    public int getItinerarioIDFK () {
        return ItinerarioIDFK;
    }

    public void setItinerarioIDFK (int itinerarioIDFK) {
        ItinerarioIDFK = itinerarioIDFK;
    }

    public String getDescricaoIntinerario () {
        return DescricaoIntinerario;
    }

    public void setDescricaoIntinerario (String descricaoIntinerario) {
        DescricaoIntinerario = descricaoIntinerario;
    }

    public int getEstacaoIDFK () {
        return EstacaoIDFK;
    }

    public void setEstacaoIDFK (int estacaoIDFK) {
        this.EstacaoIDFK = estacaoIDFK;
    }

    public Estacao getEstacao () {
        return estacao;
    }

    public void setEstacao (Estacao estacao) {
        this.estacao = estacao;
    }
}
