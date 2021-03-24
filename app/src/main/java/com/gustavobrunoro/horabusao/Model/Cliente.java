package com.gustavobrunoro.horabusao.Model;

public class Cliente {

    private int ClienteID;
    private String CNPJ;
    private String RazaoSocial;
    private String Telefone;

    public Cliente () {
    }

    public int getClienteID () {
        return ClienteID;
    }

    public void setClienteID (int clienteID) {
        ClienteID = clienteID;
    }

    public String getCNPJ () {
        return CNPJ;
    }

    public void setCNPJ (String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getRazaoSocial () {
        return RazaoSocial;
    }

    public void setRazaoSocial (String razaoSocial) {
        RazaoSocial = razaoSocial;
    }

    public String getTelefone () {
        return Telefone;
    }

    public void setTelefone (String telefone) {
        Telefone = telefone;
    }

}
