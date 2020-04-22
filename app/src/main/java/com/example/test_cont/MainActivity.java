package com.example.test_cont;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button but1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        but1 = findViewById(R.id.bEntrar);

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContainer();
            }
        });
    }

    public void openContainer(){
        Intent intent=new Intent(this, Container.class);
        startActivity(intent);
    }
}
