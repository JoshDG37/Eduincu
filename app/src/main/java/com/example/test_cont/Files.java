package com.example.test_cont;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Files extends Fragment  {

    private RecyclerView mrecyclerView;
    private ArrayList <directorio_link> listaficheros;
    private FicherosAdapter mficherosAdapter;



    DatabaseReference dataRef;

    public Files() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_files, container, false);



        mrecyclerView= view.findViewById(R.id.rec_view1);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaficheros = new ArrayList<directorio_link>();

        dataRef= FirebaseDatabase.getInstance().getReference().child("Carpetas ficheros");
        dataRef.addListenerForSingleValueEvent(valueEventListener);


        return view;
    }

    ValueEventListener valueEventListener;

    {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                String aux="";
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    directorio_link fichero = dataSnapshot1.getValue(directorio_link.class);
                    int empieza=fichero.getNombreCarpeta().indexOf("-");
                    int longitudNombre=fichero.getNombreCarpeta().length();
                    String asignatura=fichero.getNombreCarpeta().substring(0,empieza);

                    String nombreCarp=fichero.getNombreCarpeta().substring(empieza+1,longitudNombre);

                    if(!asignatura.equals(aux)){
                        aux=asignatura;
                        directorio_link injectar= new directorio_link("idfalso", asignatura, "linkfalso");
                        listaficheros.add(new directorio_link("","","-"));
                        listaficheros.add(new directorio_link("","","-"));
                        listaficheros.add(injectar);

                        fichero.setNombreCarpeta(nombreCarp);
                        listaficheros.add(fichero);
                    }else{
                        fichero.setNombreCarpeta(nombreCarp);
                        listaficheros.add(fichero);
                    }


                }
                mficherosAdapter = new FicherosAdapter(getContext(), listaficheros);
                mrecyclerView.setAdapter(mficherosAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
    }

}
