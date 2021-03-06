package com.gustavobrunoro.horabusao.Database;

import android.content.Context;

import com.google.gson.Gson;
import com.gustavobrunoro.horabusao.Model.Preferencias;
import com.gustavobrunoro.horabusao.Model.Usuario;

public class SharedPreferences {

    public Context context;

    // Nome do Arquivo
    public static final String ARQUIVO = "ARQUIVO";

    // Nome das Chaves
    public static final String PREFERENCIAS = "PREFERENCIAS";
    public static final String USUARIO = "USUARIO";

    private android.content.SharedPreferences sharedPreferences;
    private android.content.SharedPreferences.Editor editor ;

    /**Metodos responsavel retorna configuração SharedPreferences*/
    public SharedPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences( ARQUIVO , Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    /**Metodos responsavel atualizar preferencias no SharedPreferences*/
    public void atualizaPreferencias(Preferencias preferencias){
        Gson gson = new Gson();
        String json = gson.toJson(preferencias);
        editor.putString(PREFERENCIAS, json);
        editor.commit();
    }

    /**Metodos responsavel por recupera os dados pessoais no SharedPreferences
     @return  Dados do Usuario*/
    public Preferencias recupraPreferencias(){
        Preferencias preferencias = new Preferencias();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PREFERENCIAS, "");
        preferencias = gson.fromJson(json, Preferencias.class);
        return preferencias ;
    }

    /**Metodos responsavel atualizar preferencias no SharedPreferences*/
    public void atualizaUsuario(Usuario usuario){
        Gson gson = new Gson();
        String json = gson.toJson(usuario);
        editor.putString(USUARIO, json);
        editor.commit();
    }

    /**Metodos responsavel por recupera os dados pessoais no SharedPreferences
     @return  Dados do Usuario*/
    public Usuario recupraUsuario(){
        Usuario usuario = new Usuario();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(USUARIO, "");
        usuario = gson.fromJson(json, Usuario.class);
        return usuario ;
    }

    public void clear (){
        editor.clear().commit();
    }

}

