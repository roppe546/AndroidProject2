package com.example.robin.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.robin.controller.NineMensMorrisGame;
import com.example.robin.model.Checker;
import com.example.robin.model.NineMenMorrisRules;
import com.example.robin.model.Point;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private NineMensMorrisGame game;
    private NineMensMorrisView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (NineMensMorrisView) findViewById(R.id.nineMensMorrisView);

        game = new NineMensMorrisGame(view);
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

        Log.i("STATE", "Loading saved state");

        ArrayList<Checker> checkers = savedInstanceState.getParcelableArrayList("checkerInformation");
        NineMenMorrisRules rules = savedInstanceState.getParcelable("currentGameState");

        // Set game state to its previous state
        game.getRules().setGameplan(rules.getGameplan());
        game.getRules().setBluemarker(rules.getBluemarker());
        game.getRules().setRedmarker(rules.getRedmarker());
        game.getRules().setTurn(rules.getTurn());

        // Set checkers in correct position
        game.getBoard().setCheckers(checkers);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.i("STATE", "Saving current state");

        ArrayList<Checker> checkers = game.getBoard().getCheckers();
        NineMenMorrisRules rules = game.getRules();

        savedInstanceState.putParcelableArrayList("checkerInformation", checkers);
        savedInstanceState.putParcelable("currentGameState", rules);
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
