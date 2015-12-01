package com.example.robin.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.robin.controller.NineMensMorrisGame;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NineMensMorrisView view = new NineMensMorrisView(this);
        setContentView(view);
        NineMensMorrisGame game = new NineMensMorrisGame(view);
    }


}
