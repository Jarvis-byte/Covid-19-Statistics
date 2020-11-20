package com.example.covid_19;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GetStartedScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView btn_getstarted = findViewById(R.id.btn_getstarted);
        TextView txt_GetStarted = findViewById(R.id.txt_GetStarted);
        txt_GetStarted.setTextColor(Color.parseColor("#000000"));
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(800);
                    Intent intent = new Intent(GetStartedScreen.this, HomeDashboardScreen.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        btn_getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartedScreen.this, HomeDashboardScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }
}