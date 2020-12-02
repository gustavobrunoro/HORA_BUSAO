package com.gustavobrunoro.horabusao.Model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gustavobrunoro.horabusao.Database.ConfiguracaoFirebase;
import com.gustavobrunoro.horabusao.Database.HELP.DataConverterEstacao1;
import com.gustavobrunoro.horabusao.Helper.Base64Custom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(  primaryKeys = {"LinhaIDFK","ItinerarioIDFK","EstacaoIDFK"} )
public class LinhaFavorita implements Serializable {

    private String ID;
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

//    public LinhaFavorita (String ID, int linhaFavoritaID, int linhaIDFK, int numero, String descricaoLinha, int itinerarioIDFK, String descricaoIntinerario, int estacaoIDFK, Estacao estacao) {
//        this.ID = ID;
//        LinhaFavoritaID = linhaFavoritaID;
//        LinhaIDFK = linhaIDFK;
//        Numero = numero;
//        DescricaoLinha = descricaoLinha;
//        ItinerarioIDFK = itinerarioIDFK;
//        DescricaoIntinerario = descricaoIntinerario;
//        EstacaoIDFK = estacaoIDFK;
//        this.estacao = estacao;
//    }

    public String getID () {
        return ID;
    }

    public void setID (String ID) {
        this.ID = ID;
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

    @Exclude
    public Map<String,Object> ConverteMap(){

        HashMap<String,Object> map = new HashMap<>();

        map.put("LinhaFavoritaID",getLinhaFavoritaID());
        map.put("LinhaIDFK",getLinhaIDFK());
        map.put("Numero",getNumero());
        map.put("DescricaoLinha",getDescricaoLinha());
        map.put("ItinerarioIDFK",getItinerarioIDFK());

        map.put("EstacaoIDFK",getEstacaoIDFK());
        map.put("estacao",getEstacao());

        return  map;

    }

    public void salvaLinhaFavoritaFirebaseRealtime(){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticao();

        //String usuario = Base64Custom.CodificarBase64(firebaseAuth.getCurrentUser().getEmail());
        String usuario = Base64Custom.CodificarBase64("gustavobrunoro@hotmail.com");

        if (getID() == null ) {
            databaseReference.child( "ClienteID" ).child( usuario ).push().setValue( this );
        }else{
            databaseReference.child( "ClienteID" ).child( usuario );
            databaseReference.updateChildren( ConverteMap() );
        }
    }

    public void salvaLinhaFavoritaFirebaseCloud(LinhaFavorita linhaFavorita){

        FirebaseFirestore db = ConfiguracaoFirebase.getCloudStorange();

        //String usuario = Base64Custom.CodificarBase64("gustavobrunoro@hotmail.com");

        db.collection("Clientes")
                .document("123456789")
                .collection("Usuarios")
                .document("02069375552")
                .collection("LinhasFavoritas")
                .document()
                .set(linhaFavorita)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("Controle" , "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Controle" , "Error writing document", e);
                    }
                });

    }
}
