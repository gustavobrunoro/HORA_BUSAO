package com.gustavobrunoro.horabusao.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gustavobrunoro.horabusao.Activity.HorariosActivity;
import com.gustavobrunoro.horabusao.Helper.CustomGridViewActivity;
import com.gustavobrunoro.horabusao.Model.Horarios;
import com.gustavobrunoro.horabusao.Model.LinhaFavorita;
import com.gustavobrunoro.horabusao.R;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdapterLinhaFavorita extends RecyclerView.Adapter<AdapterLinhaFavorita.MyviewHolder>  {

    private Context context;
    private List<LinhaFavorita> linhaFavoritas = new ArrayList<>();
    private LinhaFavorita linhaFavorita = new LinhaFavorita();

    public AdapterLinhaFavorita (List<LinhaFavorita> linhaFavoritas) {
        this.linhaFavoritas = linhaFavoritas;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context =  parent.getContext();
        View lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_linhas_favoritas,parent,false);

        return new MyviewHolder(lista);
    }

    @Override
    public void onBindViewHolder (@NonNull final MyviewHolder holder, final int position) {

        linhaFavorita = linhaFavoritas.get(position);

        holder.Numero.setText( String.valueOf( linhaFavorita.getNumero() ) );
        holder.Descricao.setText( linhaFavorita.getDescricaoLinha() );
        holder.Itinerario.setText( linhaFavorita.getDescricaoIntinerario() );
        holder.Estacao.setText( linhaFavorita.getEstacao().getDescricao() );

        holder.Horario.setText( proximoHorario( linhaFavorita.getEstacao().getEstacaoID(), linhaFavorita.getItinerarioIDFK() ) );

        holder.Horario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                if (b){
                    holder.Horario.setTextOn( proximoHorario( linhaFavoritas.get(position).getEstacao().getEstacaoID(), linhaFavoritas.get(position).getItinerarioIDFK() ));
                    holder.Horario.setTextColor( context.getResources().getColor(android.R.color.black) );
                }
                else{
                    holder.Horario.setTextOff( tempoRestante( linhaFavoritas.get(position).getEstacao().getEstacaoID(), linhaFavoritas.get(position).getItinerarioIDFK() ));
                    holder.Horario.setTextColor( context.getResources().getColor(android.R.color.holo_red_dark) );
                }
            }
        });

        holder.GradeHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                gradeHorarios( linhaFavoritas.get(position).getNumero(), linhaFavoritas.get(position).getDescricaoLinha(), linhaFavoritas.get(position).getEstacao().getHorariosList() );
            }
        });
    }

    @Override
    public int getItemCount () {
        return linhaFavoritas.size();
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        TextView Numero;
        TextView Descricao;
        TextView Itinerario;
        TextView Estacao;
        TextView GradeHorario;
        ToggleButton Horario;

        public MyviewHolder(final View itemView) {
            super(itemView);

            Numero       = itemView.findViewById(R.id.tv_NumeroID);
            Descricao    = itemView.findViewById(R.id.tv_DescricaoID);
            Itinerario   = itemView.findViewById(R.id.tv_ItininerarioID);
            Estacao      = itemView.findViewById(R.id.tv_EstacaoID);
            GradeHorario = itemView.findViewById(R.id.tv_GradeID);
            Horario      = itemView.findViewById(R.id.tv_ProximoHorarioID);

        }
    }

    public boolean verificaHorario (String hora1, String hora2) {

        boolean controle = false;

        if  ( ( hora1 != "" || !hora1.equals(null) ) && ( hora2 != "" || !hora2.equals(null) ) ) {

            SimpleDateFormat sdfFormat = new SimpleDateFormat("HH:mm");
            Date Agora = new Date();

            Agora = convertDate(sdfFormat.format(Agora));

            GregorianCalendar gcAgora  = new GregorianCalendar();
            GregorianCalendar gcAntes  = new GregorianCalendar();
            GregorianCalendar gcDepois = new GregorianCalendar();

            gcAgora.setTime(Agora);
            gcAntes.setTime(convertDate(hora1));
            gcDepois.setTime(convertDate(hora2));

            long tempAgora  = gcAgora.getTimeInMillis();
            long tempAntes  = gcAntes.getTimeInMillis();
            long tempDepois = gcDepois.getTimeInMillis();

            if ((tempAgora >= tempAntes) && (tempAgora < tempDepois)) {
                controle = true;
            }
        }

        return controle;
    }

    public String proximoHorario( int estacao, int itinerario ){

        String hora = " - ";
        List<Horarios> horariosList = new ArrayList<>();

        for (int e = 0 ; e  < linhaFavoritas.size() ; e ++) {
            if ( linhaFavoritas.get(e).getEstacao().getEstacaoID() == estacao) {
                horariosList = linhaFavoritas.get(e).getEstacao().getHorariosList();
                for (int h = 0; h < horariosList.size(); h++) {
                    if (horariosList.get(h).getPeriodo() == 0) {
                        if (h == 0) {
                            if (verificaHorario( horariosList.get(h).getHora(), horariosList.get(h).getHora() ) ) {
                                hora = horariosList.get(h).getHora();
                            }
                        } else {
                            if (verificaHorario( horariosList.get(h - 1).getHora(), horariosList.get(h).getHora() ) ) {
                                hora = horariosList.get(h).getHora();
                            }
                        }
                    }
                }
            }
        }

        return hora;
    }

    public String tempoRestante( int estacao, int itinerario ){

        String hora = proximoHorario( estacao, itinerario );

        if(!hora.equals(" - ")) {

            String Tempo = "";

            SimpleDateFormat sdfFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Calendar gcAgora = Calendar.getInstance();
            Calendar gcHora  = Calendar.getInstance();
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
            DateTime end   = new DateTime(gcHora.getTime());
            Period period  = new Period(start, end);

            Tempo = leftZero(String.valueOf(period.getHours()), 2) + ":" + leftZero(String.valueOf(period.getMinutes() + 1), 2);

            return Tempo;
        }
        else{
            return hora;
        }
    }

    public void gradeHorarios(int numero, String descricao, List<Horarios> horarios){
        context.startActivity(new Intent(context.getApplicationContext(), HorariosActivity.class)
               .putExtra("Numero", numero )
               .putExtra("Descricao", descricao )
               .putExtra("Horarios", (Serializable) horarios));
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
