package com.gustavobrunoro.horabusao.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

public class Conexao {

    public static boolean verificarInternet(Context context){

        boolean status = false;
        ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conexao != null){

            // PARA DISPOSTIVOS NOVOS
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                NetworkCapabilities recursosRede = conexao.getNetworkCapabilities(conexao.getActiveNetwork());

                if (recursosRede != null) {//VERIFICAMOS SE RECUPERAMOS ALGO

                    if (recursosRede.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        //VERIFICAMOS SE DISPOSITIVO TEM 3G
                        return true;
                    }
                    else if (recursosRede.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        //VERIFICAMOS SE DISPOSITIVO TEM WIFFI
                        return true;
                    }
                    //NÃO POSSUI UMA CONEXAO DE REDE VÁLIDA
                    return false;
                }

            }
            else {
                // PARA DISPOSTIVOS ANTIGOS  (PRECAUÇÃO)         MESMO CODIGO
                NetworkInfo informacao = conexao.getActiveNetworkInfo();

                if (informacao != null && informacao.isConnected()) {
                    status = true;
                }
                else {
                    status = false;
                }
                return status;
            }
        }
        return false;
    }

    public static void opcoesErro(Context context, String resposta){

        if (resposta.contains("least 6 characters")){
            Toast.makeText(context,"Digite uma senha maior que 5 characters",Toast.LENGTH_LONG).show();
        }
        else if(resposta.contains("address is badly")){
            Toast.makeText(context,"E-mail inválido",Toast.LENGTH_LONG).show();
        }
        else if(resposta.contains("interrupted connection")){
            Toast.makeText(context,"Sem conexão com o Firebase",Toast.LENGTH_LONG).show();
        }
        else if(resposta.contains("password is invalid")){
            Toast.makeText(context,"senha invalida",Toast.LENGTH_LONG).show();
        }
        else if(resposta.contains("There is no user")){
            Toast.makeText(context,"Este e-mail não está cadastrado",Toast.LENGTH_LONG).show();
        }
        else if(resposta.contains("address is already")){
            Toast.makeText(context,"E-mail já existe cadastrado",Toast.LENGTH_LONG).show();
        }
        else if(resposta.contains("INVALID_EMAIL")){
            Toast.makeText(context,"E-mail inválido",Toast.LENGTH_LONG).show();
        }
        else if(resposta.contains("EMAIL_NOT_FOUND")){
            Toast.makeText(context,"E-mail não cadastrado ainda",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context,resposta,Toast.LENGTH_LONG).show();
        }

    }

}
