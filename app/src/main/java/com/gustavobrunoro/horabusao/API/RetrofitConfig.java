package com.gustavobrunoro.horabusao.API;

import com.gustavobrunoro.horabusao.Model.Usuario;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitConfig {

    /**Metodos Responsavel por Retorna Paramentro de Conexão com a API
     @return  Paramento Retrofit*/
    public static retrofit2.Retrofit getRetrofit(final Usuario usuario){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder().addHeader("Accept", "application/json")
//                                                              .addHeader( "Authorization", "Bearer " + usuario.getApi_token() ).build();
//                return chain.proceed(request);
//            }
//        });

        return new retrofit2.Retrofit.Builder()
                .baseUrl( API.URL_PRINCIPAL)
                .addConverterFactory( GsonConverterFactory.create() )
                .addConverterFactory( ScalarsConverterFactory.create() )
                //.client( httpClient.build() )
                .build() ;
    }

}

