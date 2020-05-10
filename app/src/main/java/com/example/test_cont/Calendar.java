package com.example.test_cont;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.comparators.EventComparator;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.*;
import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class Calendar extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    CompactCalendarView compactCalendarView; //Calendario
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM-yyyy", Locale.getDefault()); //Formato para mostrar el mes
    private DatabaseReference mDatabase;


    public Calendar(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_calendar,container,false);
        final TextView etEventos = v.findViewById(R.id.Eventos);
        final TextView etMeses=v.findViewById(R.id.meses); //Muestra el mes de ese calendario
        compactCalendarView = v.findViewById(R.id.compactcalendar_view); //Calendario
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        //Esto es para la fecha actual
        String mesActual = new SimpleDateFormat("MMMM").format(new Date());
        etMeses.setText( mesActual );


        ArrayList<Event> eventos = new ArrayList<>() ; // can also take a Date object
        //Llamo al metodo para actualizar la BD con la info de ese  archivo
        eventos=scrollBaseDatos( v,mesActual );
        /*
        for (int i=0;i<eventos.size();i++)
            compactCalendarView.addEvent(eventos.get(i));

        ArrayList<Event> arrayEventos=new ArrayList<>();
        */
        final String meses [] ={"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
        /*
        for(int m=0;m<12;m++){
            arrayEventos=scrollBaseDatos( v, meses[m] );
        }

         */
        final ArrayList<Event> finalEventos=eventos;


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //List<Event> events = compactCalendarView.getEvents(dateClicked);
                for (int d = 0; d < finalEventos.size(); d++) {
                    Date fechaSelecionada = dateClicked;
                   if (finalEventos.get( d ).getTimeInMillis() == convertirDate( fechaSelecionada )) {
                        Toast.makeText(getActivity(),""+ finalEventos.get( d ).getData(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {//Esto esta para ir actualizando el mes en el que esta
                etMeses.setText(dateFormat.format(firstDayOfNewMonth)); //Muestra en el mes que se encuentra
                //Esto es para actualizar la BD al mes correspondiente
                String mesScroll = "";
                String etMesesSt=etMeses.getText().toString();
                for(int m=0;m<12;m++){
                    if(etMesesSt.contains(meses[m])){
                        mesScroll=meses[m];
                    }
                }
                scrollBaseDatos(v, mesScroll);
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
    //Clase para pasar de Date a timelnMilli a una Long timelnMilli
    public static Long convertirDate(Date fechaDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //Pasamos la fecha a Long
        long millis = fechaDate.getTime();

        return millis;
    }

    public ArrayList<Event> scrollBaseDatos(View v, String mes){
        final ArrayList<String> listaOrdena2=  new ArrayList<>();
        final ArrayAdapter adaptador = new ArrayAdapter( getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, listaOrdena2 );
        final ListView listaDias = v.findViewById( R.id.listViewEventos );
        final ArrayList<Event> eventos = new ArrayList<>() ;
        listaDias.setAdapter( adaptador );
        mDatabase = FirebaseDatabase.getInstance().getReference().child( "Fechas" ).child(mes);
        mDatabase.orderByChild( "fecha").addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String fecha = dataSnapshot.child( "fecha" ).getValue().toString();
                String evento = dataSnapshot.child( "evento" ).getValue().toString();
                eventos.add(new Event( Color.BLUE, convertirMilli( fecha ), evento ) );
                compactCalendarView.addEvent(new Event( Color.WHITE, convertirMilli( fecha ), evento )  );
                String infoDia =evento+": "+fecha;
                listaOrdena2.add( infoDia );
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        } );
        return eventos;
    }
}