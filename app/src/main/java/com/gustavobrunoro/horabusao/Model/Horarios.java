package com.gustavobrunoro.horabusao.Model;

import java.io.Serializable;

public class Horarios implements Serializable {

    private int HorariosID;
    private int LinhaIDFK;
    private int ItinerarioIDFK;
    private String Hora;
    private int Periodo;

    public Horarios () {
    }

    public int getHorariosID () {
        return HorariosID;
    }

    public void setHorariosID (int horariosID) {
        HorariosID = horariosID;
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

    public String getHora () {
        return Hora;
    }

    public void setHora (String hora) {
        Hora = hora;
    }

    public int getPeriodo () {
        return Periodo;
    }

    public void setPeriodo (int periodo) {
        Periodo = periodo;
    }

}
