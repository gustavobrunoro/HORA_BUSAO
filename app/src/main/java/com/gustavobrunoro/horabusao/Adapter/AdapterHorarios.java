package com.gustavobrunoro.horabusao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.islamkhsh.CardSliderAdapter;
import com.gustavobrunoro.horabusao.Helper.CustomGridViewActivity;
import com.gustavobrunoro.horabusao.Model.Horarios;
import com.gustavobrunoro.horabusao.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterHorarios  extends CardSliderAdapter<AdapterHorarios.MyviewHolder> {

    private Context context;
    private List<Horarios> horariosList = new ArrayList<>();
    private String linha;

    public AdapterHorarios(String descricaoLinha, List<Horarios> horariosList){
        this.linha = descricaoLinha;
        this.horariosList = horariosList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_horarios, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void bindVH (MyviewHolder myviewHolder, int i) {

        if ( i == 0 ){
            myviewHolder.PeriodoID.setText( R.string.periodo_1);
            CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(context, linha, periodo(0) );
            myviewHolder.androidGridView.setAdapter(adapterViewAndroid);
        }
        else{
            myviewHolder.PeriodoID.setText( R.string.periodo_2);
            CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(context, linha, periodo(1) );
            myviewHolder.androidGridView.setAdapter(adapterViewAndroid);
        }
    }

    @Override
    public int getItemCount () {
        return 2;
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        TextView PeriodoID;
        GridView androidGridView;

        public MyviewHolder(final View itemView) {
            super(itemView);

            PeriodoID = itemView.findViewById(R.id.tv_PeriodoID);
            androidGridView = (GridView)itemView.findViewById(R.id.grid_view);
        }
    }

    public List<Horarios> periodo(int periodo ){

        List<Horarios> ho = new ArrayList<>();

        for (Horarios h : horariosList){
            if ( h.getPeriodo() == periodo ){
                 ho.add(h);
            }
        }

        return ho;
    }


}
