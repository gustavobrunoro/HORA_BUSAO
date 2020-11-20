package com.gustavobrunoro.horabusao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.gustavobrunoro.horabusao.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String descricao = getResources().getString(R.string.sobre_descricao) ;

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo( getPackageName() , 0 );
        } catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();
        }

        Element versionElement = new Element();
        versionElement.setTitle( getResources().getString( R.string.sobre_version, pInfo.versionName ) );

        Element phoneElement = new Element();
        phoneElement.setTitle( getResources().getString(R.string.sobre_telefone) );
        phoneElement.setIconDrawable(  R.drawable.ic_baseline_call_24  );
        phoneElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String phone = getResources().getString(R.string.sobre_telefone);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        View sobre = new AboutPage(this).isRTL(false)
                .setImage( R.drawable.img_logo )
                .setDescription( descricao )
                .addGroup( "Fale Conosco" )
                .addEmail( getResources().getString(R.string.sobre_email) )
                .addWebsite( getResources().getString(R.string.sobre_website) )
                .addFacebook( getResources().getString(R.string.sobre_facebook)  )
                .addInstagram( getResources().getString(R.string.sobre_instagram)  )
                .addItem( phoneElement )
                .addItem( versionElement )
                .create();
        setContentView(sobre);
    }
}