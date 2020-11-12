package com.gustavobrunoro.horabusao.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gustavobrunoro.horabusao.Adapter.AdapterLinha;
import com.gustavobrunoro.horabusao.Database.ConfiguracaoDatabase;
import com.gustavobrunoro.horabusao.Database.HELP.AppExecutors;
import com.gustavobrunoro.horabusao.Helper.RecyclerItemClickListener;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.Model.LinhaFavorita;
import com.gustavobrunoro.horabusao.R;

import java.util.ArrayList;
import java.util.List;

public class LinhaFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Linha> linhas = new ArrayList<>();
    private AdapterLinha adapter;
    private Bundle bundle = new Bundle();

    private ConfiguracaoDatabase configuracaoDatabase;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            linhas = (List<Linha>) getArguments().getSerializable("Linhas");
        }
    }

    @Override
    public void onResume () {
        super.onResume();
        carregalinhas();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_linha, container, false);

        inicializaComponentes ();
        atualizaRecycleView(linhas);

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick (View view, int position) {
                startActivity(new Intent(getContext(), EstacaoActivity.class).putExtra("Linha",linhas.get(position)));
            }

            @Override
            public void onLongItemClick (View view, int position) {
            }

            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
            }
        }));

        return  view;
    }

    private void inicializaComponentes (){
        recyclerView  = view.findViewById( R.id.rv_Linhas );
        configuracaoDatabase = ConfiguracaoDatabase.getInstance( getContext() );
    }

    public void carregalinhas(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                linhas = configuracaoDatabase.linhaDAO().getLinhaList();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run () {
                        atualizaRecycleView(linhas);
                    }
                });
            }
        });
    }

    public void atualizaRecycleView (final List<Linha> linhas ){
        adapter = new AdapterLinha( linhas , getContext() );
        layoutManager = new LinearLayoutManager( getActivity().getApplicationContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter( adapter );
        adapter.notifyDataSetChanged();
    }

}