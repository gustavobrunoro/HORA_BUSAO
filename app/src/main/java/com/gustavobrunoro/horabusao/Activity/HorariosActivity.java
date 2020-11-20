package com.gustavobrunoro.horabusao.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.islamkhsh.CardSliderViewPager;
import com.gustavobrunoro.horabusao.Adapter.AdapterHorarios;
import com.gustavobrunoro.horabusao.Model.Horarios;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.R;

import java.util.ArrayList;
import java.util.List;

public class HorariosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardSliderViewPager cardSliderViewPager;
    private String descricaoLinha;
    private TextView title;

    private Linha linha = new Linha();
    private List<Horarios> horariosList = new ArrayList<>();

    private Integer numero;
    private Integer itinerario;
    private Integer estacao;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        linha      = (Linha) getIntent().getSerializableExtra("Linha");
        itinerario = (Integer) getIntent().getSerializableExtra("Intinenario");
        estacao    = (Integer) getIntent().getSerializableExtra("Estacao");

        if (linha != null ){
            numero         = linha.getNumero();
            descricaoLinha = linha.getItinerarios().get(itinerario).getEstacaoList().get(estacao).getDescricao();
            horariosList   = linha.getItinerarios().get(itinerario).getEstacaoList().get(estacao).getHorariosList();
        }
        else {
            numero         = (Integer) getIntent().getIntExtra("Numero",0);
            descricaoLinha = (String)  getIntent().getStringExtra("Descricao");
            horariosList   = (List<Horarios>) getIntent().getSerializableExtra("Horarios");
        }

        inicializaComponentes();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title.setText( descricaoLinha );

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardSliderViewPager.setAdapter( new AdapterHorarios( descricaoLinha, horariosList ) );
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void inicializaComponentes () {
        toolbar             = findViewById(R.id.toolbar);
        title               = toolbar.findViewById(R.id.title);
        cardSliderViewPager = findViewById(R.id.viewPager);
    }

}