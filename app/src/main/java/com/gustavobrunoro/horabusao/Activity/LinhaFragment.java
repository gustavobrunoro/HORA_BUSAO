package com.gustavobrunoro.horabusao.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
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
import android.widget.AdapterView;

import com.gustavobrunoro.horabusao.Adapter.AdapterLinha;
import com.gustavobrunoro.horabusao.Database.ConfiguracaoDatabase;
import com.gustavobrunoro.horabusao.Database.HELP.AppExecutors;
import com.gustavobrunoro.horabusao.Helper.RecyclerItemClickListener;
import com.gustavobrunoro.horabusao.MainActivity;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.Model.LinhaFavorita;
import com.gustavobrunoro.horabusao.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class LinhaFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Linha> linhas = new ArrayList<>();
    private AdapterLinha adapter;
    private MaterialSearchView searchView;

    private ConfiguracaoDatabase configuracaoDatabase;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            linhas = (List<Linha>) getArguments().getSerializable("Linhas");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume () {
        super.onResume();
        carregalinhas();
    }

    @Override
    public void onCreateOptionsMenu (@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_linha, container, false);

        inicializaComponentes ();
        atualizaRecycleView(linhas);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

       /* recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
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
        }));*/

        return view;
    }

    public void inicializaComponentes (){
        recyclerView         = view.findViewById( R.id.rv_Linhas );
        searchView           = getActivity().findViewById(R.id.search_view);
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