package com.gustavobrunoro.horabusao;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gustavobrunoro.horabusao.API.RetrofitConfig;
import com.gustavobrunoro.horabusao.API.Rotas;
import com.gustavobrunoro.horabusao.Activity.LinhaFavoritasFragment;
import com.gustavobrunoro.horabusao.Activity.LinhaFragment;
import com.gustavobrunoro.horabusao.Activity.SobreActivity;
import com.gustavobrunoro.horabusao.Database.ConfiguracaoDatabase;
import com.gustavobrunoro.horabusao.Database.HELP.AppExecutors;
import com.gustavobrunoro.horabusao.Database.SharedPreferences;
import com.gustavobrunoro.horabusao.Helper.CommonUtils;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.Model.LinhaFavorita;
import com.gustavobrunoro.horabusao.Model.Preferencias;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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
    private Bundle bundle = new Bundle();
    private MaterialSearchView searchView;

    private List<Linha> linhas = new ArrayList<>();
    private List<LinhaFavorita> linhaFavoritas = new ArrayList<>();
    private Preferencias preferencias = new Preferencias();

    private Retrofit retrofit;
    private Rotas rotas;

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

        //carregaJson();
        downloadLinhas();
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
        configuracaoDatabase = ConfiguracaoDatabase.getInstance(this);

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
                configuracaoDatabase.linhaDAO().insertLinhaList(linhas);
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

}