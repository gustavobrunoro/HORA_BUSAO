package com.gustavobrunoro.horabusao.Database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gustavobrunoro.horabusao.Helper.Base64Custom;

public class ConfiguracaoFirebase {

    private static FirebaseAuth firebaseAuth ;
    private static StorageReference storageReference;

    private static DatabaseReference databaseReference;
    private static FirebaseFirestore cloudStorange;

    //region Autenticação
    public static FirebaseAuth getFirebaseAutenticao(){
        if( firebaseAuth == null ){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }
    //endregion

    //region Storage
    public static StorageReference getStorageReference(){
        if( storageReference == null ){
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }
    //endregion

    //region Reatime DataBase
    public static DatabaseReference getDatabaseReference(){
        if( databaseReference == null ){
            FirebaseDatabase.getInstance().setPersistenceEnabled( true );
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    public static DatabaseReference IDUsuario (){
        String Email = firebaseAuth.getCurrentUser().getEmail();
        DatabaseReference usuario = ConfiguracaoFirebase.getDatabaseReference().child("Usuarios").child( Base64Custom.CodificarBase64(Email));
        return usuario;
    }
    //endregion

    //region Cloud DataBase
    public static FirebaseFirestore getCloudStorange(){

        if( cloudStorange == null ){

            cloudStorange = FirebaseFirestore.getInstance();
        }
        return cloudStorange;
    }
    //endregion

}
