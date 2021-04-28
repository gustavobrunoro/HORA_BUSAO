package com.gustavobrunoro.horabusao.Activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gustavobrunoro.horabusao.MainActivity;
import com.gustavobrunoro.horabusao.R;

public class SplashActivity extends AppCompatActivity implements Runnable {

    private ProgressBar progressBar;
    private Thread thread;
    private Handler handler;
    private int i;

    private FirebaseAuth auth;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.progressBar_Abertura);

        handler = new Handler();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        i = 1;

        try{
            while(i<=100){
                Thread.sleep(50);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        i++;
                        progressBar.setProgress(i);
                    }
                });
            }

            //FirebaseAuth.getInstance().signOut();
            //LoginManager.getInstance().logOut();
            FirebaseUser user = auth.getCurrentUser();

            if ( user!=null ) {
                finish();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }else{
                finish();
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        }
        catch (InterruptedException e){
        }
    }
}