package com.gustavobrunoro.horabusao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.gustavobrunoro.horabusao.Activity.EstacaoActivity;
import com.gustavobrunoro.horabusao.Activity.LocalizacaoActivity;
import com.gustavobrunoro.horabusao.Database.ConfiguracaoDatabase;
import com.gustavobrunoro.horabusao.Helper.CustomGridViewActivity;
import com.gustavobrunoro.horabusao.Model.Horarios;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.Model.LocalOnibus;
import com.gustavobrunoro.horabusao.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdapterLinha extends RecyclerView.Adapter<AdapterLinha.MyviewHolder> implements Filterable {

    private Context context;
    private List<Linha> linhas = new ArrayList<>();
    private List<Linha> linhasFilter = new ArrayList<>();
    private Linha linha = new Linha();
    private ConfiguracaoDatabase configuracaoDatabase;
    private AdapterEstacao adapterEstacao;

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
        holder.ProximoHorario.setText( "Próxima Parada: " + proximoHorario( linha.getLinhaID() ));

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                context.startActivity(new Intent(context, EstacaoActivity.class).putExtra("Linha",linhasFilter.get(position)));
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                context.startActivity(new Intent(context, LocalizacaoActivity.class));
            }
        });

    }

    @Override
    public int getItemCount () {
        return linhasFilter.size();
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        TextView NumeroID;
        TextView DescricaoID;
        TextView ProximoHorario;
        ImageView imageView;
        ConstraintLayout constraintLayout;

        public MyviewHolder(final View itemView) {
            super(itemView);

            NumeroID    = itemView.findViewById(R.id.tv_NumeroID);
            DescricaoID = itemView.findViewById(R.id.tv_DescricaoID);
            ProximoHorario = itemView.findViewById(R.id.tv_ProximoID);
            imageView = itemView.findViewById(R.id.imageView2);
            constraintLayout = itemView.findViewById(R.id.cl_Descricao);
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

    public int itinerario (){

        Calendar cal = Calendar.getInstance();
        Date date = new Date();

        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        return  day == 1 || day == 7 ? 1 : 0 ;

    }

    public Date convertDate (String date) {

        SimpleDateFormat sdfToDate = new SimpleDateFormat("HH:mm");

        Date hora = null;
        try {
            hora = sdfToDate.parse(date);
        }
        catch (ParseException ex) {
            Logger.getLogger(CustomGridViewActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

        Calendar gc = Calendar.getInstance();
        gc.setTime(hora);

        return gc.getTime();
    }

    public boolean verificaHorario (String hora1, String hora2) {

        boolean controle = false;

        if  ( ( hora1 != "" || !hora1.equals(null) ) && ( hora2 != "" || !hora2.equals(null) ) ) {

            SimpleDateFormat sdfFormat = new SimpleDateFormat("HH:mm");
            Date Agora = new Date();

            Agora = convertDate(sdfFormat.format(Agora));

            GregorianCalendar gcAgora = new GregorianCalendar();
            GregorianCalendar gcAntes = new GregorianCalendar();
            GregorianCalendar gcDepois = new GregorianCalendar();

            gcAgora.setTime(Agora);
            gcAntes.setTime(convertDate(hora1));
            gcDepois.setTime(convertDate(hora2));

            long tempAgora = gcAgora.getTimeInMillis();
            long tempAntes = gcAntes.getTimeInMillis();
            long tempDepois = gcDepois.getTimeInMillis();

            if ((tempAgora >= tempAntes) && (tempAgora < tempDepois)) {
                controle = true;
            }
        }

        return controle;

    }

    public int LocalOnibus(Linha linha){

        int proximaParada = 0;

        List<LocalOnibus> localOnibusList = new ArrayList<>();
        List<Horarios> horariosList = new ArrayList<>();
        LocalOnibus localOnibus;
        int controle = 0;

        if ( linha.getItinerarios().get( 0 ).getEstacoes().size() > 0 ) {

            for (int e = 0; e < linha.getItinerarios().get(0).getEstacoes().size(); e++) {

                horariosList = linha.getItinerarios().get(itinerario()).getEstacoes().get(e).getHorarios();

                for (int i = 0; i < horariosList.size(); i++) {
                    localOnibus = new LocalOnibus();
                    localOnibus.setEstacaoID(e);
                    localOnibus.setHora(horariosList.get(i).getHora());
                    localOnibusList.add(localOnibus);
                }

            }

            Collections.sort(localOnibusList);

            for (int i = 0; i < localOnibusList.size(); i++) {
                controle = i + 1 == localOnibusList.size() ? 0 : i + 1;

                if (verificaHorario(localOnibusList.get(i).getHora(), localOnibusList.get(controle).getHora())) {
                    proximaParada = linha.getItinerarios().get(itinerario()).getEstacoes().get(localOnibusList.get(controle).getEstacaoID()).getEstacaoID();
                }
            }
        }

        return  proximaParada;

    }

    public String proximoHorario( int Linha){

        String hora = "-";
        List<Horarios> horariosList = new ArrayList<>();

        for (int e = 0; e  < linha.getItinerarios().get( itinerario() ).getEstacoes().size() ; e ++) {
            if ( linha.getItinerarios().get( itinerario() ).getEstacoes().get(e).getEstacaoID() == LocalOnibus( linha )) {
                horariosList = linha.getItinerarios().get( itinerario() ).getEstacoes().get(e).getHorarios();
                for (int i = 0; i < horariosList.size(); i++) {
                    if (horariosList.get(i).getPeriodo() == 0) {
                        if (i == 0) {
                            if (verificaHorario(horariosList.get(i).getHora(), horariosList.get(i).getHora())) {
                                hora = horariosList.get(i).getHora();
                            }
                        } else {
                            if (verificaHorario(horariosList.get(i - 1).getHora(), horariosList.get(i).getHora())) {
                                hora = horariosList.get(i).getHora();
                            }
                        }
                    }
                }
            }
        }

        return hora;

    }

}
