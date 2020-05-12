package com.example.test_cont;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.FrameMetrics;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Container extends AppCompatActivity {
    private BottomNavigationView bot_nav;
    private FrameLayout frameLayout;

    private Home homefrag;
    private Calendar calendarfrag;
    private Files filesfrag;
    private Chat chatfrag;
    private Settings settingsfrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        bot_nav= findViewById(R.id.nav_bar_bot);
        frameLayout= findViewById(R.id.contenedor);

        homefrag=new Home();
        calendarfrag=new Calendar();
        filesfrag=new Files();
        chatfrag=new Chat();
        settingsfrag=new Settings();

        openFragment(homefrag);

        bot_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        openFragment(homefrag);
                        return true;
                    case R.id.calendar:
                        openFragment(calendarfrag);
                        return true;
                    case R.id.files:
                        openFragment(filesfrag);
                        return true;
                    case R.id.chat:
                        openFragment(chatfrag);
                        return true;
                    case R.id.settings:
                        openFragment(settingsfrag);
                        return true;

                    default:
                        return false;
                }


            }
        });

    }

    public void openFragment(Fragment frag){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedor, frag);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed(){

    }


}
