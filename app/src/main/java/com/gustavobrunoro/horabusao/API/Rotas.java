package com.gustavobrunoro.horabusao.API;

import com.gustavobrunoro.horabusao.Model.Linha;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Rotas {

    @GET("Linhas")
    Call<List<Linha>> getLinhas ();
}
