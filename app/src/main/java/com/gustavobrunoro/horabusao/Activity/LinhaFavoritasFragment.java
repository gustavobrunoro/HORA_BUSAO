package com.gustavobrunoro.horabusao.Activity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gustavobrunoro.horabusao.Adapter.AdapterLinhaFavorita;
import com.gustavobrunoro.horabusao.Database.ConfiguracaoDatabase;
import com.gustavobrunoro.horabusao.Database.ConfiguracaoFirebase;
import com.gustavobrunoro.horabusao.Database.HELP.AppExecutors;
import com.gustavobrunoro.horabusao.Model.LinhaFavorita;
import com.gustavobrunoro.horabusao.R;

import java.util.ArrayList;
import java.util.List;

public class LinhaFavoritasFragment extends Fragment {

    private View view;
    private Toolbar toolbar;
    private TextView aviso;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ConfiguracaoDatabase configuracaoDatabase;
    private List<LinhaFavorita> linhaFavoritas = new ArrayList<>();
    private AdapterLinhaFavorita adapter;



    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume () {
        super.onResume();
        carregaLinhasFavoritas ();
    }

    @Override
    public void onCreateOptionsMenu (@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.menu_search);
        item.setVisible(false);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_linha_favoritas, container, false);

        inicializaComponentes ();
        carregaLinhasFavoritas ();
        atualizaRecycleView(linhaFavoritas);

        return view;
    }

    public void inicializaComponentes () {
        toolbar              = view.findViewById(R.id.toolbar);
        aviso                = view.findViewById(R.id.tv_AvisoLinhaFavoritas);
        recyclerView         = view.findViewById(R.id.rv_favoritas);
        configuracaoDatabase = ConfiguracaoDatabase.getInstance( getContext() );
    }

    public void carregaLinhasFavoritas (){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                linhaFavoritas = configuracaoDatabase.linhaFavoritaDAO().getLinhaList();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run () {
                        atualizaRecycleView(linhaFavoritas);
                    }
                });
            }
        });
    }

    public void atualizaRecycleView( final List<LinhaFavorita> linhaFavoritas){
            adapter = new AdapterLinhaFavorita(linhaFavoritas);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if (linhaFavoritas.size() > 0 ) {
                aviso.setVisibility(View.GONE);
            }
            else{
                aviso.setVisibility(View.VISIBLE);
            }
    }

}