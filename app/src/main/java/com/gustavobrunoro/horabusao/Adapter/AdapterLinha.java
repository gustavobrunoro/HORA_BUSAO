package com.gustavobrunoro.horabusao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gustavobrunoro.horabusao.Database.ConfiguracaoDatabase;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterLinha extends RecyclerView.Adapter<AdapterLinha.MyviewHolder> {

    private Context context;
    private List<Linha> linhas = new ArrayList<>();
    private Linha linha = new Linha();
    private ConfiguracaoDatabase configuracaoDatabase;

    public AdapterLinha (List<Linha> linhas, Context context1 ){
        this.linhas = linhas;
        configuracaoDatabase = ConfiguracaoDatabase.getInstance(context1);
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context =  parent.getContext();
        View lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_linha,parent,false);

        return new AdapterLinha.MyviewHolder(lista);
    }

    @Override
    public void onBindViewHolder (@NonNull MyviewHolder holder, final int position) {
        linha = linhas.get(position);

        holder.NumeroID.setText( String.valueOf( linha.getNumero() ) );
        holder.DescricaoID.setText( linha.getDescricao() );
    }

    @Override
    public int getItemCount () {
        return linhas.size();
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        TextView NumeroID;
        TextView DescricaoID;

        public MyviewHolder(final View itemView) {
            super(itemView);

            NumeroID    = itemView.findViewById(R.id.tv_NumeroID);
            DescricaoID = itemView.findViewById(R.id.tv_DescricaoID);

        }
    }
}
