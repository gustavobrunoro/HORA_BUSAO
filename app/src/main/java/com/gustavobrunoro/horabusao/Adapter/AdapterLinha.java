package com.gustavobrunoro.horabusao.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gustavobrunoro.horabusao.Database.ConfiguracaoDatabase;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterLinha extends RecyclerView.Adapter<AdapterLinha.MyviewHolder> implements Filterable {

    private Context context;
    private List<Linha> linhas = new ArrayList<>();
    private List<Linha> linhasFilter = new ArrayList<>();
    private Linha linha = new Linha();
    private ConfiguracaoDatabase configuracaoDatabase;

    public AdapterLinha (List<Linha> linhas, Context context1 ){
        this.linhas = linhas;
        this.linhasFilter = linhas;
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
        linha = linhasFilter.get(position);

        holder.NumeroID.setText( String.valueOf( linha.getNumero() ) );
        holder.DescricaoID.setText( linha.getDescricao() );
    }

    @Override
    public int getItemCount () {
        return linhasFilter.size();
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

    @Override
    public Filter getFilter () {

        return new Filter() {
            @Override
            protected FilterResults performFiltering (CharSequence charSequence) {
                String charString = charSequence.toString().toUpperCase();

                if (charString.isEmpty()) {
                    linhasFilter = linhas;
                } else {
                    List<Linha> filterList = new ArrayList<>();
                    for (Linha linha : linhas) {
                        if ( linha.getDescricao().toUpperCase().contains( charString ) || String.valueOf( linha.getNumero() ).contains( charString ) ) {
                            filterList.add( linha );
                        }
                    }
                    linhasFilter = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = linhasFilter;
                return filterResults;
            }

            @Override
            protected void publishResults (CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
    }
}
