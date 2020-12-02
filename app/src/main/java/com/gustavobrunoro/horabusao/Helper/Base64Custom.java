package com.gustavobrunoro.horabusao.Helper;

import android.util.Base64;

public class Base64Custom {

    public static String CodificarBase64(String base){

        return Base64.encodeToString(base.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");

    }

    public static String DecodificarBase64(String base){

        return new String( Base64.decode(base, Base64.DEFAULT));

    }

}
