package com.gustavobrunoro.horabusao.Model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LocalOnibus implements Comparable<LocalOnibus>{

    private int EstacaoID;
    private String Hora;

    public LocalOnibus () {
    }

    public int getEstacaoID () {
        return EstacaoID;
    }

    public void setEstacaoID (int estacaoID) {
        EstacaoID = estacaoID;
    }

    public String getHora () {
        return Hora;
    }

    public void setHora (String hora) {
        Hora = hora;
    }

    @Override
    public int compareTo(LocalOnibus lo) {
        return this.getHora().compareTo(lo.getHora());
    }
}
