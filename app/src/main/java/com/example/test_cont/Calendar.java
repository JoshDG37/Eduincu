package com.example.test_cont;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.comparators.EventComparator;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.*;
import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class Calendar extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    CompactCalendarView compactCalendarView; //Calendario
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM-yyyy", Locale.getDefault()); //Formato para mostrar el mes


    public Calendar() {  /*Required empty public constructor */ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calendar,container,false);
        TextView textView = v.findViewById(R.id.Eventos);
        final TextView etMeses=v.findViewById(R.id.meses);
        compactCalendarView = v.findViewById(R.id.compactcalendar_view); //Calendario
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        //Añadir eventos
        ArrayList<Event> eventos = new ArrayList<>() ; // can also take a Date object
        String myDate = "26/5/2020 00:00:00";
        eventos.add(new Event(Color.GREEN, 1587945600000L,"Cumple Evita"));
        eventos.add(new Event(Color.WHITE, convertirMilli("4/5/2020 00:00:00"), "Videollamada con Raúl"));
        eventos.add(new Event(Color.GREEN, convertirMilli(myDate), "Cumple de alguien "));
        eventos.add(new Event(Color.GRAY, convertirMilli("26/5/2020 00:00:00"), "Dia de mi muerte"));
        eventos.add(new Event(Color.BLUE, convertirMilli("30/6/2020 10:00:00"), "Examen con franco"));

        //Aqui Va lo de ordenar fechas


        //ESTO ES PARA EL LISTVIEW
        ListView listaDias= v.findViewById(R.id.listViewEventos);
        ArrayList<String> listaNombres = new ArrayList<>();
        for(int i=0;i<eventos.size();i++){
            //Añadir al array list los eventos
            String infoDia= eventos.get(i).getData() +": "+convertirFecha(eventos.get(i).getTimeInMillis());
            listaNombres.add(infoDia);
            //Añadir los eventos para que se visualicen en el calendario
            compactCalendarView.addEvent(eventos.get(i));
        }

        //ArrayAdapter para el listView
        ArrayAdapter adaptador= new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,listaNombres);
        listaDias.setAdapter(adaptador);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {//Esto esta para ir actualizando el mes en el que esta
                etMeses.setText(dateFormat.format(firstDayOfNewMonth));
            }
        });
        return v;
    }

    //Clase para convertir una fecha a timelnMilli
    public static Long convertirMilli(String fecha){
        //Formato al que queremos pasar
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        //Convertimos el String fecha al formato elegido
        Date conversionFecha = null;
        try {
            conversionFecha = sdf.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Pasamos la fecha a Long
        long millis = conversionFecha.getTime();

        return millis;
    }

    //Clase para convertir el timelnMilli a una fecha String
    public static String convertirFecha(Long fechaMilli){
        Date fecha= new Date(fechaMilli);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaS = sdf.format(fecha);
        return fechaS;
    }
}