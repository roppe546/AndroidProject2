package com.example.robin.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.robin.controller.NineMensMorrisGame;
import com.example.robin.model.Checker;
import com.example.robin.model.Point;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NineMensMorrisGame game;
    private NineMensMorrisView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("hello");

        setContentView(R.layout.activity_main);

        if (game == null) {
            Log.i("ACT", "Game was null, creating new game.");
            game = new NineMensMorrisGame(this, view);
        }
        if (view == null) {
            Log.i("ACT", "View was null, creating new view.");
            view = new NineMensMorrisView(this, game);
        }

        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("STATE", "onResume");

        game.getBoard().setPoints(new ArrayList<Point>());

//        view.setPoints(new ArrayList<Point>());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i("STATE", "Loading state from model");

        ArrayList<Checker> checkers = savedInstanceState.getParcelableArrayList("checkerInformation");

        game.getBoard().setCheckers(checkers);
//        view.setCheckers(checkers);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.i("STATE", "Saving state from model");

        ArrayList<Checker> checkers = game.getBoard().getCheckers();
//        ArrayList<Checker> checkers = view.getCheckers();

        savedInstanceState.putParcelableArrayList("checkerInformation", checkers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.settingsButton) {
            Log.i("ActionMenu", "Selected Settings in menu");
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
