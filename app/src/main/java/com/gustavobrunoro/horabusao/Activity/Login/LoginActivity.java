package com.gustavobrunoro.horabusao.Activity.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.gustavobrunoro.horabusao.Database.SharedPreferences;
import com.gustavobrunoro.horabusao.Helper.Conexao;
import com.gustavobrunoro.horabusao.MainActivity;
import com.gustavobrunoro.horabusao.Model.Preferencias;
import com.gustavobrunoro.horabusao.Model.Usuario;
import com.gustavobrunoro.horabusao.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private TextView criarConta;
    private TextInputEditText edtit_email;
    private TextInputEditText edtit_senha;
    private Button entrar;
    private CardView cardView_LoginFacebook;
    private CardView cardView_LoginGoogle;
    private CardView cardView_LoginAnonimo;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializaComponentes ();

        servicosAutenticacaoEmail();
        servicosGoogle();
        servicosFacebook();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_CriarContaID:
                startActivity(new Intent(getBaseContext(),CadastroActivity.class));
                break;
            case R.id.bt_Login_EntraID:
                signInEmail();
                break;
            case R.id.cardView_LoginFacebook:
                signInFacebook();
                break;
            case R.id.cardView_LoginGoogle:
                signInGoogle();
                break;
            case R.id.cardView_LoginAnonimo:
                signInAnonimo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 555){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                adicionarContaGoogleaoFirebase(account);
            }catch (ApiException e){
                String resultado = e.getMessage();
                Conexao.opcoesErro(getBaseContext(),resultado);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (authStateListener != null){
            auth.removeAuthStateListener(authStateListener);
        }

    }

    public void inicializaComponentes () {

        imageView = findViewById(R.id.imageView2);
        criarConta = findViewById(R.id.tv_CriarContaID);
        edtit_email      = findViewById(R.id.tv_Login_EmailID);
        edtit_senha      = findViewById(R.id.tv_Login_SenhaID);
        entrar     = findViewById(R.id.bt_Login_EntraID);
        cardView_LoginFacebook = findViewById(R.id.cardView_LoginFacebook);
        cardView_LoginGoogle   = findViewById(R.id.cardView_LoginGoogle);
        cardView_LoginAnonimo  = findViewById(R.id.cardView_LoginAnonimo);

        criarConta.setOnClickListener(this);
        entrar.setOnClickListener(this);
        cardView_LoginFacebook.setOnClickListener(this);
        cardView_LoginGoogle.setOnClickListener(this);
        cardView_LoginAnonimo.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        sharedPreferences = new SharedPreferences(getApplicationContext());


    }

    //-------------------------------------------------SERVICOS LOGIN--------------------------------------------------

    private void servicosLoginEmail(String email, String senha){

        auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                    startActivity(new Intent(getBaseContext(),MainActivity.class));
                    finish();
                }
                else{
                    String resposta = task.getException().toString();
                    Conexao.opcoesErro(getBaseContext(),resposta);
                }
            }
        });
    }

    private void servicosFacebook(){
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                adicionaContaSharedPreferences(loginResult);
                adicionarContaFacebookaoFirebase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(),"Cancelado" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                String resultado = error.getMessage();
                Conexao.opcoesErro(getBaseContext(),resultado);
            }
        });
    }

    private void servicosGoogle(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void servicosAutenticacaoEmail(){

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if ( user!=null ){
                    Toast.makeText(getBaseContext(),"Usuario " + user.getEmail() + " está logado" , Toast.LENGTH_LONG).show();
                }
                else{
                }
            }
        };
    }

    //------------------------------------------METODOS DE LOGIN---------------------------------------------------------------

    private void signInEmail(){

        String email = edtit_email.getText().toString().trim();
        String senha = edtit_senha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()){
            Toast.makeText(getBaseContext(),"Insira os campos obrigatórios", Toast.LENGTH_LONG).show();
        }
        else{
            if (Conexao.verificarInternet(this)){
                ConnectivityManager conexao = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                servicosLoginEmail(email,senha);
            }
            else{
                Toast.makeText(getBaseContext(),"Erro - Verifique se sua Wifi ou 3G está funcionando", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void signInFacebook(){
        List permissionNeeds= Arrays.asList("public_profile,user_link","email");

        LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);

    }

    private void signInGoogle(){

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null){
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent,555);
        }else{
            //já existe alem conectado pelo google
            Toast.makeText(getBaseContext(),"Já logado",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getBaseContext(), MainActivity.class));
        }

    }

    private void signInAnonimo(){
        adicionarContaAnonimaaoFirebase();
    }

    //---------------------------------------AUTENTICACAO NO FIREBASE---------------------------------------------------------------

    private void adicionarContaFacebookaoFirebase(final AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getBaseContext(),MainActivity.class));
                        }
                        else {
                            String resultado = task.getException().toString();
                            Conexao.opcoesErro(getBaseContext(),resultado);
                        }
                    }
                });
    }

    private void adicionarContaGoogleaoFirebase(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getBaseContext(),MainActivity.class));
                        }
                        else {
                            String resultado = task.getException().toString();
                            Conexao.opcoesErro(getBaseContext(),resultado);
                        }

                    }
                });
    }

    private void adicionarContaAnonimaaoFirebase (){

        auth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getBaseContext(),MainActivity.class));
                        } else {
                            String resultado = task.getException().toString();
                            Conexao.opcoesErro(getBaseContext(),resultado);
                        }
                    }
                });

    }

    private void adicionaContaSharedPreferences(final LoginResult loginResult){


            GraphRequest request = GraphRequest.newMeRequest(

                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(  JSONObject object, GraphResponse response) {

                            try {

                                Usuario usuario = new Usuario();

                                usuario.setNome(object.getString("name"));
                                usuario.setEmail(object.getString("email"));
                                usuario.setFacebookID( object.getString("id"));
                                usuario.setFacebookFoto(object.getJSONObject("picture").getJSONObject("data").getString("url"));

                                sharedPreferences.atualizaUsuario(usuario);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, email, picture.width(120).height(120)");
            request.setParameters(parameters);
            request.executeAsync();

    }

}