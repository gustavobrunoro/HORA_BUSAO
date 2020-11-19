package com.gustavobrunoro.horabusao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gustavobrunoro.horabusao.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sobre);

        String descricao = "Aplicativo desenvolvido para auxiliar na buscar de horários de ônibus de Eunápolis, trazendo comodidade e agilidade para o usuário do sistema de transporte público.";

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo( getPackageName() , 0 );
        } catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();
        }

        Element versionElement = new Element();
        versionElement.setTitle("Versão " + pInfo.versionName);

        Element phoneElement = new Element();
        phoneElement.setTitle("(73) 3262-0491");
        phoneElement.setIconDrawable(  R.drawable.ic_baseline_call_24  );
        phoneElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String phone = "(73) 3262-0491";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        View sobre = new AboutPage(this).isRTL(false)
                .setImage(R.drawable.img_logo)
                .setDescription(descricao)
                .addGroup("Fale Conosco")
                .addEmail("email","Envie um Email")
                .addWebsite("http://www.gwgviacaoeturismo.com.br/index.html")
                .addFacebook("viacaogwgtranporteseturismo")
                .addInstagram("empresadetransportegwg")
                .addItem(phoneElement)
                .addItem(versionElement)
                .create();
        setContentView(sobre);

    }
}