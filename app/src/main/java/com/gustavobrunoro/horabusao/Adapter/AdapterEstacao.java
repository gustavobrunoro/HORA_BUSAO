package com.gustavobrunoro.horabusao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.gustavobrunoro.horabusao.Activity.HorariosActivity;
import com.gustavobrunoro.horabusao.Database.ConfiguracaoDatabase;
import com.gustavobrunoro.horabusao.Database.HELP.AppExecutors;
import com.gustavobrunoro.horabusao.Helper.CustomGridViewActivity;
import com.gustavobrunoro.horabusao.Model.Estacao;
import com.gustavobrunoro.horabusao.Model.Horarios;
import com.gustavobrunoro.horabusao.Model.Linha;
import com.gustavobrunoro.horabusao.Model.LinhaFavorita;
import com.gustavobrunoro.horabusao.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdapterEstacao extends RecyclerView.Adapter<AdapterEstacao.ViewHolder> {

    private Context context;
    private static final int UNSELECTED = -1;
    private RecyclerView recyclerView;
    private int selectedItem = UNSELECTED;
    private ConfiguracaoDatabase configuracaoDatabase;
    private List<LinhaFavorita> linhaFavoritas = new ArrayList<>();

    private Linha linha = new Linha();
    private int itinerario;

    public AdapterEstacao (RecyclerView recyclerView, Linha linha, int itinerario ) {
        this.recyclerView = recyclerView;
        this.linha = linha;
        this.itinerario = itinerario;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        configuracaoDatabase = ConfiguracaoDatabase.getInstance( context );
        listLinhaFavorita();

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount () {
        return linha.getItinerarios().get(itinerario).getEstacaoList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {

        private ExpandableLayout expandableLayout;
        private ImageView estacao;
        private TextView descricao;
        private ToggleButton proximoHorario;
        private TextView grade;
        private LikeButton linhaFavorita;

        public ViewHolder(View itemView) {
            super(itemView);

            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandableLayout.setOnExpansionUpdateListener(this);

            estacao        = itemView.findViewById(R.id.iv_EstacaoID);
            descricao      = itemView.findViewById(R.id.tv_DescricaoPontoID);
            proximoHorario = itemView.findViewById(R.id.tv_ProximoHorarioID);
            grade          = itemView.findViewById(R.id.tv_GradeID);
            linhaFavorita  = itemView.findViewById(R.id.mfb_LinhaFavoritaID);

            linhaFavorita.setLiked(false);

            descricao.setOnClickListener(this);

            grade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    gradeHorarios(linha, getAdapterPosition() );
                }
            });

            linhaFavorita.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked (LikeButton likeButton) {
                    addLinhaFavorita( linha, itinerario, getAdapterPosition());
                }

                @Override
                public void unLiked (LikeButton likeButton) {
                    removeLinhaFavorita(linha, itinerario, getAdapterPosition(), likeButton);
                }
            });

            proximoHorario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                    if (b){
                       proximoHorario.setTextOn(proximoHorario( linha.getItinerarios().get(itinerario).getEstacaoList().get( getAdapterPosition() ).getEstacaoID() ));
                    }
                    else{
                       proximoHorario.setTextOff(tempoRestante( linha.getItinerarios().get(itinerario).getEstacaoList().get( getAdapterPosition() ).getEstacaoID() ));
                    }
                }
            });
        }

        public void bind() {

            int position = getAdapterPosition();
            boolean isSelected = position == selectedItem;

            proximoHorario.setText( proximoHorario( linha.getItinerarios().get(itinerario).getEstacaoList().get(position).getEstacaoID() ) );
            descricao.setText( linha.getItinerarios().get(itinerario).getEstacaoList().get(position).getDescricao() );
            descricao.setSelected(isSelected);
            expandableLayout.setExpanded(isSelected, false);
            linhaFavorita.setTag( linha.getItinerarios().get(itinerario).getEstacaoList().get(position).getEstacaoID() );

            if (checkLinhaFavoita(linha.getLinhaID(), itinerario, linha.getItinerarios().get(itinerario).getEstacaoList().get(position).getEstacaoID() )){
                linhaFavorita.setLiked(true);
            }

            // Expande o Primeiro item
            if ( position == 0 ){
                expandableLayout.expand(false);
                selectedItem = 0;
            }
        }

        @Override
        public void onClick(View view) {

            ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);

            if (holder != null) {
                holder.descricao.setSelected(false);
                holder.expandableLayout.collapse();
            }

            int position = getAdapterPosition();

            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            }
            else {
                descricao.setSelected(true);
                expandableLayout.expand();
                selectedItem = position;
            }
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
           if (state == ExpandableLayout.State.EXPANDING) {

              if ( getAdapterPosition() != -1 )
                recyclerView.smoothScrollToPosition(getAdapterPosition());
            }
        }

    }

    public boolean checkLinhaFavoita( int linhaIDFK, int itinerarioIDFK, int estacaoIDFK ){
        for( LinhaFavorita lf : linhaFavoritas ){
            if ( ( lf.getLinhaIDFK() == linhaIDFK ) && ( lf.getItinerarioIDFK() == itinerarioIDFK ) && ( lf.getEstacao().getEstacaoID() == estacaoIDFK )) {
                return  true;
            }
        }
        return false;
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

    public String proximoHorario( int estacao){

        String hora = " - ";
        List<Horarios> horariosList = new ArrayList<>();

        for (int e = 0 ; e  < linha.getItinerarios().get(itinerario).getEstacaoList().size() ; e ++) {

            if ( linha.getItinerarios().get(itinerario).getEstacaoList().get(e).getEstacaoID() == estacao) {

                horariosList = linha.getItinerarios().get(itinerario).getEstacaoList().get(e).getHorariosList();

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

    public String tempoRestante(int estacao){

        String hora = proximoHorario(estacao);

        if(!hora.equals(" - ")) {

            String Tempo = "";

            SimpleDateFormat sdfFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Calendar gcAgora = Calendar.getInstance();
            Calendar gcHora = Calendar.getInstance();
            Calendar gcTempo = Calendar.getInstance();
            Calendar duracao = Calendar.getInstance();

            int day = gcAgora.get(Calendar.DAY_OF_MONTH);
            int month = gcAgora.get(Calendar.MONTH) + 1;
            int year = gcAgora.get(Calendar.YEAR);

            String d = day + "/" + month + "/" + year + " " + hora + ":00";

            try {
                gcHora.setTime(sdfFormat2.parse(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            gcTempo.setTimeInMillis(gcAgora.getTimeInMillis() - gcHora.getTimeInMillis());
            duracao.setTimeInMillis(gcAgora.getTimeInMillis() - gcHora.getTimeInMillis());

            DateTime start = new DateTime(gcAgora.getTime());
            DateTime end = new DateTime(gcHora.getTime());
            Period period = new Period(start, end);

            Tempo = leftZero(String.valueOf(period.getHours()), 2) + ":" + leftZero(String.valueOf(period.getMinutes() + 1), 2);

            return Tempo;
        }
        else{
            return hora;
        }
    }

    public void listLinhaFavorita(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                linhaFavoritas = configuracaoDatabase.linhaFavoritaDAO().getLinhaList();
            }
        });

    }

    public void addLinhaFavorita(Linha linha, int itinerario, int estacao){

        final LinhaFavorita linhaFavorita = new LinhaFavorita();

        linhaFavorita.setLinhaIDFK( linha.getLinhaID() );
        linhaFavorita.setDescricaoLinha( linha.getDescricao() );

        linhaFavorita.setNumero(  linha.getNumero() );

        linhaFavorita.setItinerarioIDFK( itinerario );
        linhaFavorita.setDescricaoIntinerario( linha.getItinerarios().get(itinerario).getDescricao() );

        linhaFavorita.setEstacaoIDFK( linha.getItinerarios().get(itinerario).getEstacaoList().get(estacao).getEstacaoID() );
        //linhaFavorita.setDescricaoEstacao( linha.getItinerarios().get(itinerario).getEstacaoList().get(estacao).getDescricao() );

        //linhaFavorita.setHorariosList( linha.getItinerarios().get(itinerario).getEstacaoList().get(estacao).getHorariosList() );

        Estacao e = new Estacao();

        e.setEstacaoID( linha.getItinerarios().get(itinerario).getEstacaoList().get(estacao).getEstacaoID()  );
        e.setDescricao( linha.getItinerarios().get(itinerario).getEstacaoList().get(estacao).getDescricao() );
        e.setHorariosList( linha.getItinerarios().get(itinerario).getEstacaoList().get(estacao).getHorariosList() );

        linhaFavorita.setEstacao( e );

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                configuracaoDatabase.linhaFavoritaDAO().inserLinhaFavorita( linhaFavorita );
            }
        });

        AdapterEstacao.this.notifyDataSetChanged();
    }

    public void removeLinhaFavorita(Linha linha, int itinerario, int estacao,final LikeButton likeButton){

        final LinhaFavorita linhaFavorita = new LinhaFavorita();

        linhaFavorita.setLinhaIDFK(linha.getLinhaID());
        linhaFavorita.setItinerarioIDFK(itinerario);
        linhaFavorita.setEstacaoIDFK( linha.getItinerarios().get(itinerario).getEstacaoList().get(estacao).getEstacaoID() );

        Estacao e = new Estacao();

        e.setEstacaoID( linha.getItinerarios().get(itinerario).getEstacaoList().get(estacao).getEstacaoID()  );

        linhaFavorita.setEstacao( e );

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                configuracaoDatabase.linhaFavoritaDAO().removeLinhaFavorita( linhaFavorita );
                likeButton.setLiked(false);

            }
        });
    }

    public void gradeHorarios(Linha linha, int estacao){
        context.startActivity(new Intent(context.getApplicationContext(), HorariosActivity.class).putExtra("Linha", linha )
                                                                                                 .putExtra("Intinenario", itinerario)
                                                                                                 .putExtra("Estacao", estacao));
    }

    public Date convertDate (String date) {

        SimpleDateFormat sdfToDate = new SimpleDateFormat("HH:mm");

        Date hora = null;
        try {
            hora = sdfToDate.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(CustomGridViewActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

        Calendar gc = Calendar.getInstance();
        gc.setTime(hora);

        return gc.getTime();
    }

    public String leftZero( String texto, int tamanho ){

        String s = texto.trim();
        StringBuffer resp = new StringBuffer();

        int fim = tamanho - s.length();

        for (int x=0; x<fim; x++) {
            resp.append('0');
        }
        return  resp + s;
    }

}
