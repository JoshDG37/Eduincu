package com.example.test_cont;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {
    TextView tv;
    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_settings, container, false);

        Button butlogout= (Button) rootView.findViewById(R.id.but_logout);

        tv=(TextView)rootView.findViewById(R.id.tv1);

        butlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closefragment();
                getActivity().finish();
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();

                //----------------------------------------------------------------hacer para importar metodos del padre o alguna forma para poder hacer el logout

            }
        });


        return rootView;
    }

    private void closefragment() {
        getFragmentManager().beginTransaction().
                remove(getFragmentManager().findFragmentById(R.id.settings)).commit();
    }
}
