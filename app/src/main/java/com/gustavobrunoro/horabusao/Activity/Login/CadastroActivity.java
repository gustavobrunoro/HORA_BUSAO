package com.gustavobrunoro.horabusao.Activity.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gustavobrunoro.horabusao.Helper.Conexao;
import com.gustavobrunoro.horabusao.MainActivity;
import com.gustavobrunoro.horabusao.R;

public class CadastroActivity extends AppCompatActivity{

    private EditText editText_Email;
    private EditText editText_Senha;
    private EditText editText_SenhaRepetir;
    private FirebaseAuth auth;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializaComponentes ();

    }

    public void inicializaComponentes () {

        editText_Email        = findViewById(R.id.editText_EmailCadastro);
        editText_Senha        = findViewById(R.id.editText_SenhaCadastro);
        editText_SenhaRepetir = findViewById(R.id.editText_SenhaRepetirCadastro);

        auth = FirebaseAuth.getInstance();
    }

    public void cadastrar(View view){

        String email = editText_Email.getText().toString().trim();
        String senha = editText_Senha.getText().toString().trim();
        String confirmaSenha = editText_SenhaRepetir.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()){
            Toast.makeText(getBaseContext(),"Erro - Preencha os Campos",Toast.LENGTH_LONG).show();
        }
        else{
            if (senha.contentEquals(confirmaSenha)){

                if(Conexao.verificarInternet(this)){
                    Log.i("Controle", "adicionarEmailSenhaoaoFirebase");
                    adicionarEmailSenhaoaoFirebase(email,senha);
                }
                else{
                    Toast.makeText(getBaseContext(),"Erro - Verifique se sua Wiffi ou 3G está funcionando",Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getBaseContext(),"Erro - Senhas Diferentes",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void adicionarEmailSenhaoaoFirebase (String email, String senha){

        auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                    finish();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    Toast.makeText(getBaseContext(),"Cadastro efetuado com Sucesso",Toast.LENGTH_LONG).show();
                }
                else{
                    String resposta = task.getException().toString();
                    Conexao.opcoesErro(getBaseContext(),resposta);
                }
            }
        });
    }

}