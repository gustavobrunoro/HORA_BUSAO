package com.gustavobrunoro.horabusao.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gustavobrunoro.horabusao.API.Rotas;
import com.gustavobrunoro.horabusao.Database.SharedPreferences;
import com.gustavobrunoro.horabusao.Model.Usuario;
import com.gustavobrunoro.horabusao.R;

import java.util.Map;

import retrofit2.Retrofit;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Usuario usuario = new Usuario();
    private SharedPreferences sharedPreferences ;

    private Retrofit retrofit;
    private Rotas rotas;

    @Override
    public void onMessageReceived(RemoteMessage notificacao) {
        super.onMessageReceived( notificacao );

        inicializaComponentes ();

        if ( notificacao.getNotification() != null ){
            enviaNotificacao( notificacao );
        }

    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken( token );

        inicializaComponentes ();
        //atualizaToken();

    }

    /**Metodos responsavel por inicializar os Componentes da Activity
     */
    private void inicializaComponentes (){

        sharedPreferences = new SharedPreferences( getApplicationContext() );

//        usuario = sharedPreferences.recupraDadosPessoais();

        //retrofit = RetrofitConfig.getRetrofit( usuario );
        //resolve = retrofit.create(Resolve.class);
    }

    /**Metodos responsavel por enviar a notificação quando o APP estive em execução
     * @param notificacao - Parametros da Notificação a ser mostrada
     */
    public void enviaNotificacao (RemoteMessage notificacao){

        Intent intent = new Intent();
        Map<String, String> data = notificacao.getData();

        String button = "";

        switch ( data.get("Tipo") ){

            case "Atualizacao":
                intent = new Intent(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse( getResources().getString( R.string.DownloadAplicativo ) ));
                button = "Conferir";
                break;
            case "Informativo":
                button = "Ok";
                break;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0, intent, 0);
        //NotificationCompat.Action notificationCompatAtualiza = new NotificationCompat.Action( R.drawable.ic_confirma_24dp,button, pendingIntent);

        Uri uriSom = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );

        // Cria Notificação
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, getString( R.string.default_notification_channel_id ) )
                .setContentTitle( notificacao.getNotification().getTitle())
                .setContentText( notificacao.getNotification().getBody())
               // .setSmallIcon( R.drawable.notificacao )
                .setSound( uriSom );
                //.addAction( notificationCompatAtualiza );

        //Recupera NOtificação
        NotificationManager notificationManager  = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){

            NotificationChannel notificationChannel = new NotificationChannel( getString( R.string.default_notification_channel_id ),"canal", NotificationManager.IMPORTANCE_DEFAULT );
            notificationManager.createNotificationChannel( notificationChannel );
        }
        //Enviar Notificação
        notificationManager.notify( 0,notificationCompat.build() );

    }

    /**Metodos responsavel por atualiza o TokenMobile na Web
     */
    private void atualizaToken(){

//        resolve.atuzaliaToken( recuperaToken() ).enqueue( new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call , Response<String> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call , Throwable t) {
//
//            }
//        });

    }

    /**Metodos responsavel por Recupera o Token
     */
    public static String recuperaToken(){

        final String[] token = new String[1];

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token[0] = String.valueOf( instanceIdResult.getToken() );
            }
        } );
        return token[0];
    }

}
