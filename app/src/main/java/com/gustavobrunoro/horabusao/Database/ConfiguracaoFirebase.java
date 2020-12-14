package com.gustavobrunoro.horabusao.Database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static FirebaseAuth firebaseAuth ;
    private static StorageReference storageReference;

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

}
