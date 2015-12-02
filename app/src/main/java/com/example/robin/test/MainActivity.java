package com.example.robin.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.robin.controller.NineMensMorrisGame;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NineMensMorrisView view = null;
        NineMensMorrisGame game = null;

        game = new NineMensMorrisGame(this, view);
        view = new NineMensMorrisView(this, game);

        setContentView(view);



    }
}
