package com.gustavobrunoro.horabusao.Helper;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.gustavobrunoro.horabusao.Model.Horarios;
import com.gustavobrunoro.horabusao.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomGridViewActivity extends BaseAdapter {

    private Context mContext;
    private List<Horarios> horarios = new ArrayList<>();
    private String linha;
    LayoutInflater layoutInflater;

    public CustomGridViewActivity (Context context, String Linha, List<Horarios> horarios) {
        mContext = context;
        this.linha = Linha;
        this.horarios = horarios;
    }

    @Override
    public int getCount () {
        return horarios.size();
    }

    @Override
    public Object getItem (int i) {
        return null;
    }

    @Override
    public long getItemId (int i) {
        return 0;
    }

    @Override
    public View getView (final int i, final View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Holder holder = new Holder();
        View rowView;

        rowView = layoutInflater.inflate(R.layout.gridview_horarios, null);
        holder.tv =( TextView) rowView.findViewById(R.id.android_gridview_text);

        holder.tv.setText( horarios.get(i).getHora() );

        if (i == 0) {
            if (verificaHorario(horarios.get( i ).getHora(), horarios.get( i ).getHora())) {
                holder.tv.setTextColor( ResourcesCompat.getColor( mContext.getResources(), android.R.color.white, null));
                //holder.tv.setBackgroundColor(  ResourcesCompat.getColor( mContext.getResources(), R.color.colorPrimaryDark, null));
                holder.tv.setBackgroundResource( R.drawable.cantos_arredondados_proximo );
            }
            else{
                holder.tv.setBackgroundResource(R.drawable.cantos_arredondados);
            }
        }
        else {
            if (verificaHorario(horarios.get( i - 1 ).getHora(), horarios.get( i ).getHora())) {
                holder.tv.setTextColor( ResourcesCompat.getColor( mContext.getResources(), android.R.color.white, null));
                //holder.tv.setBackgroundColor(  ResourcesCompat.getColor( mContext.getResources(), R.color.colorPrimaryDark, null));
                holder.tv.setBackgroundResource( R.drawable.cantos_arredondados_proximo );
            }
            else{
                holder.tv.setBackgroundResource(R.drawable.cantos_arredondados);
            }
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addCalendario( horarios.get(i).getHora() );
            }
        });

        return rowView;

    }

    public class Holder{
        TextView tv;
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

    public Date convertDate (String date) {

        SimpleDateFormat sdfToDate = new SimpleDateFormat("HH:mm");

        Date hora = null;
        try {
            hora = sdfToDate.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(CustomGridViewActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(hora);

        return gc.getTime();
    }

    public void addCalendario(String hora) {

        SimpleDateFormat sdfData = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfDataHora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Date data = new Date();

        String datahora  = sdfData.format(data ) + " " + hora + ":00";

        //Cria uma intent para abertura de uma nova activity.
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        //Configurações do evento.
        intent.putExtra(CalendarContract.Events.TITLE, "Linha: " + linha);
        //intent.putExtra(CalendarContract.Events.DESCRIPTION, "Teste de descrição");

        //Configuração de data do evento
        GregorianCalendar calDateInicio = new GregorianCalendar();
        try {
            calDateInicio.setTime(sdfDataHora.parse(datahora));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,calDateInicio.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,calDateInicio.getTimeInMillis());

        //Define se será repetido
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=DAILY;WKST=SU;BYDAY=TU,TH");

        //Marca como privado e como ocupado.
        intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        mContext.startActivity(intent);

    }

}