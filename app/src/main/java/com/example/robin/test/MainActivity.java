package com.example.robin.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.robin.controller.NineMensMorrisGame;
import com.example.robin.model.Checker;
import com.example.robin.model.Point;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NineMensMorrisView view = null;
    NineMensMorrisGame game = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("hello");

        setContentView(R.layout.activity_main);

        if (game == null) {
            game = new NineMensMorrisGame(this, view);
        }
        if (view == null) {
            view = new NineMensMorrisView(this, game);
        }

        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("STATE", "onResume");

        view.setPoints(new ArrayList<Point>());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i("STATE", "Loading state from model");

        ArrayList<Checker> checkers = savedInstanceState.getParcelableArrayList("checkerInformation");

        view.setCheckers(checkers);

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.i("STATE", "Saving state from model");

        ArrayList<Checker> checkers = view.getCheckers();

        savedInstanceState.putParcelableArrayList("checkerInformation" , checkers);
    }
}
