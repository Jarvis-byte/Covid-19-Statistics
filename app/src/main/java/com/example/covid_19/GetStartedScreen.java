package com.example.covid_19;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class GetStartedScreen extends AppCompatActivity {
    ProgressBar spin_kit_splash_Screen;
    RelativeLayout MainRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spin_kit_splash_Screen = findViewById(R.id.spin_kit_splash_Screen);
        MainRelativeLayout = findViewById(R.id.MainRelativeLayout);
        ImageView btn_getstarted = findViewById(R.id.btn_getstarted);
        TextView txt_GetStarted = findViewById(R.id.txt_GetStarted);
        txt_GetStarted.setTextColor(Color.parseColor("#000000"));
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //
                    sleep(1000);
                    spin_kit_splash_Screen.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        Thread thread2 = new Thread() {
            @Override
            public void run() {
                try {
                    //
                    sleep(1000);
                    Intent intent = new Intent(GetStartedScreen.this, HomeDashboardScreen.class);
                    startActivity(intent);
                    //Animatoo.animateZoom(GetStartedScreen.this);
                    Animatoo.animateZoom(GetStartedScreen.this);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread2.start();
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