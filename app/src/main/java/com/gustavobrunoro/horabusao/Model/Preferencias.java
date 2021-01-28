package com.gustavobrunoro.horabusao.Model;

public class Preferencias {

    boolean DownloadLinhas;
    boolean Tutorial;

    public Preferencias () {
    }

    public boolean isPrimeiroAcesso () {
        return Tutorial;
    }

    public void setPrimeiroAcesso (boolean primeiroAcesso) {
        this.Tutorial = primeiroAcesso;
    }

    public boolean isDownloadLinhas () {
        return DownloadLinhas;
    }

    public void setDownloadLinhas (boolean downloadLinhas) {
        DownloadLinhas = downloadLinhas;
    }
}
