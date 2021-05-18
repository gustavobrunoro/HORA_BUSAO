package com.gustavobrunoro.horabusao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileManager;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.gustavobrunoro.horabusao.API.RetrofitConfig;
import com.gustavobrunoro.horabusao.API.Rotas;
import com.gustavobrunoro.horabusao.Activity.LinhaFavoritasFragment;
import com.gustavobrunoro.horabusao.Activity.LinhaFragment;
import com.gustavobrunoro.horabusao.Activity.Login.LoginActivity;
import com.gustavobrunoro.horabusao.Activity.SobreActivity;
import com.gustavobrunoro.horabusao.Adapter.AdapterEstacao;
import com.gustavobrunoro.horabusao.Database.ConfiguracaoDatabase;
import com.gustavobrunoro.horabusao.Database.HELP.AppExecutors;
import com.gustavobrunoro.horabusao.Database.SharedPreferences;
import com.gustavobrunoro.horabusao.Helper.CommonUtils;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.Model.LinhaFavorita;
import com.gustavobrunoro.horabusao.Model.Preferencias;
import com.gustavobrunoro.horabusao.Model.Usuario;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String[] permissoes = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private View headerView;
    private AppBarConfiguration mAppBarConfiguration;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private FragmentPagerItemAdapter adapter;
    private ConfiguracaoDatabase configuracaoDatabase;
    private SharedPreferences sharedPreferences;
    private Bundle bundle = new Bundle();
    private MaterialSearchView searchView;
    private ImageView fotoUsuario;
    private TextView nomeUsuario;

    private List<Linha> linhas = new ArrayList<>();
    private List<LinhaFavorita> linhaFavoritas = new ArrayList<>();
    private Preferencias preferencias = new Preferencias();

    private Retrofit retrofit;
    private Rotas rotas;

    private GoogleSignInClient googleSignInClient;

    private static final String DATA_JSON_PATH = "data.json";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializaComponentes ();

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        loadPerfilUsuario();

        carregaJson();
        //downloadLinhas();
        carregaLinhasFavoritas ();

        //loadRecycle ();
    }

    @Override
    protected void onResume () {
        super.onResume();
        carregaLinhasFavoritas ();
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        carregaLinhasFavoritas ();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        final MenuItem item = menu.findItem(R.id.menu_search);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_Atualizar:
                SharedPreferences sharedPreferences = new SharedPreferences(getApplicationContext());
                sharedPreferences.clear();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Efetuar uma Varredura nas Permissões
        for (int permissaoResultado : grantResults) {

            // Verifica se a Permissão esta negada, para solicita permissão
            if ( permissaoResultado == PackageManager.PERMISSION_DENIED){

                // Solicita a permissão
                alertaPermissao();
            }

        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_InicioID:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
            case R.id.nav_PontosRecargaID:
                break;
            case R.id.nav_Sobre:
                startActivity(new Intent(getApplicationContext(), SobreActivity.class));
                break;
            case R.id.nav_Sair:
                sair();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void inicializaComponentes () {

        toolbar              = findViewById(R.id.toolbar);
        searchView           = findViewById(R.id.search_view);
        drawer               = findViewById(R.id.drawer_layout2);
        navigationView       = findViewById(R.id.nav_view);
        headerView           = navigationView.getHeaderView(0);
        viewPagerTab         = findViewById(R.id.viewPagerTab);
        viewPager            = findViewById(R.id.viewPager);
        fotoUsuario          = headerView.findViewById(R.id.cim_FotoUsuarioID);
        nomeUsuario          = headerView.findViewById(R.id.cim_NomeUsuarioID);

        configuracaoDatabase = ConfiguracaoDatabase.getInstance(this);
        sharedPreferences = new SharedPreferences(getApplicationContext());

        retrofit = RetrofitConfig.getRetrofit( );
        rotas  = retrofit.create( Rotas.class);
    }

    public void downloadLinhas(){

        final SharedPreferences sharedPreferences;

        sharedPreferences = new SharedPreferences(getApplicationContext());
        preferencias = sharedPreferences.recupraPreferencias();

        if ( preferencias == null || !preferencias.isDownloadLinhas() ) {

            rotas.getLinhas().enqueue(new Callback<List<Linha>>() {
                @Override
                public void onResponse (Call<List<Linha>> call, Response<List<Linha>> response) {
                    if (response.isSuccessful()) {
                        linhas = response.body();

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run () {
                                configuracaoDatabase.linhaDAO().insertLinhaList( linhas );
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run () {
                                loadRecycle();
                                preferencias = new Preferencias();
                                preferencias.setDownloadLinhas(true);
                                sharedPreferences.atualizaPreferencias( preferencias );
                            }
                        });

                    }
                }

                @Override
                public void onFailure (Call<List<Linha>> call, Throwable t) {
                }
            });
        }
        else{
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run () {
                        linhas = configuracaoDatabase.linhaDAO().getLinhaList();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run () {
                                loadRecycle();
                            }
                        });
                    }
                });


        }

    }

    private void  carregaJson(){

        String json =  CommonUtils.loadJSONFromAsset(getApplicationContext(), DATA_JSON_PATH);
        List<Linha> jsonModelList1 = new ArrayList<>();
        jsonModelList1 = new Gson().fromJson(json, new TypeToken<List<Linha>>(){}.getType());
        linhas.addAll(jsonModelList1);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                //configuracaoDatabase.linhaDAO().delete();
                //configuracaoDatabase.linhaFavoritaDAO().delete();
                configuracaoDatabase.linhaDAO().insertLinhaList(linhas);
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run () {
                loadRecycle();
                preferencias = new Preferencias();
                preferencias.setDownloadLinhas(true);
                sharedPreferences.atualizaPreferencias( preferencias );
            }
        });
    }

    public void carregaLinhasFavoritas (){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                linhaFavoritas = configuracaoDatabase.linhaFavoritaDAO().getLinhaList();
            }
        });
    }

    public void loadRecycle (){

        bundle.putSerializable("Linhas", (Serializable) linhas);
        bundle.putSerializable("LinhasFavoritas", (Serializable) linhaFavoritas);

        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Linhas", LinhaFragment.class, bundle )
                        .add("Favoritas", LinhaFavoritasFragment.class, bundle )
                        .create()
        );

        viewPager.setAdapter( adapter );
        viewPagerTab.setViewPager( viewPager );

    }

    public void sair(){

        // Logout Email e Senha
        FirebaseAuth.getInstance().signOut();

        // Logout Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();

        // Logout Facebook
        LoginManager.getInstance().logOut();

        finish();
        startActivity(new Intent(getBaseContext(), LoginActivity.class));

    }

    public void loadPerfilUsuario(){

        Usuario usuario = new Usuario();
        usuario = sharedPreferences.recupraUsuario();

        String foto = usuario == null ? "" : usuario.getFacebookFoto();

        Glide.with(MainActivity.this)
             .load(foto)
             .centerInside()
             // .error(R.drawable.erro)
             .into(fotoUsuario); // id do teu imageView.

        nomeUsuario.setText( usuario == null ? "Nome Usuario" : usuario.getNome() );

    }

    private void alertaPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Necessárias");
        builder.setMessage("Para Ultilizar este aplicativo é necessário aceitar as permissões");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {finish();}
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}