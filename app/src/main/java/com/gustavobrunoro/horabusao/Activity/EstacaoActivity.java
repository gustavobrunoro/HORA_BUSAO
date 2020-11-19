package com.gustavobrunoro.horabusao.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.gustavobrunoro.horabusao.Adapter.AdapterEstacao;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.R;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;

import static com.gustavobrunoro.horabusao.R.drawable.ic_baseline_autorenew_24;

public class EstacaoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView title;
    private ToggleButton trajeto;
    private RecyclerView recyclerView;
    private AdapterEstacao adapter;

    private Linha linha = new Linha();
    private int itinerario = 0;

    private View view;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacao);

        linha = (Linha) getIntent().getSerializableExtra("Linha");

        inicializaComponentes();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //title.setText( getString(R.string.estacao, String.valueOf( linha.getNumero() ) ) );
        title.setText( linha.getDescricao() );

        trajeto.setChecked(true);
        trajeto.setText( linha.getItinerarios().get(0).getDescricao() );

        carregaRecycle();

        trajeto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                if (b){
                    itinerario = 0;
                    trajeto.setTextOn( linha.getItinerarios().get(0).getDescricao() );
                }
                else{
                    itinerario = 1;
                    trajeto.setTextOff( linha.getItinerarios().get(1).getDescricao() );
                }
                carregaRecycle();
            }
        });
    }

    public void inicializaComponentes () {
        toolbar      = findViewById(R.id.toolbar);
        title        = (TextView) toolbar.findViewById(R.id.title);
        trajeto      = findViewById(R.id.tg_TrajetoID);
        recyclerView = findViewById(R.id.recycler_view);

        view         = findViewById(R.id.tg_TrajetoID);
    }

    public void carregaRecycle(){
        adapter = new AdapterEstacao(recyclerView, linha, itinerario, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}