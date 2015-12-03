package com.example.robin.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.robin.controller.NineMensMorrisGame;
import com.example.robin.model.Checker;
import com.example.robin.model.NineMenMorrisRules;
import com.example.robin.model.Point;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NineMensMorrisGame game;
    private NineMensMorrisView view;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.textView);
        view = (NineMensMorrisView) findViewById(R.id.nineMensMorrisView);
        game = new NineMensMorrisGame(view, this);
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

        // Set point radius
        view.setPreferredRadius(savedInstanceState.getFloat("pointRadius"));

        // Set game state to its previous state
        game.getRules().setGameplan(rules.getGameplan());
        game.getRules().setBluemarker(rules.getBluemarker());
        game.getRules().setRedmarker(rules.getRedmarker());
        game.getRules().setTurn(rules.getTurn());

        // Set checkers in correct position
        game.getBoard().setCheckers(checkers);
    }


    //TODO fix the bug when mill occurs.
    public void updateUI(String string) {
            info.setText(string);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.i("STATE", "Saving current state");

        ArrayList<Checker> checkers = game.getBoard().getCheckers();
        NineMenMorrisRules rules = game.getRules();

        savedInstanceState.putParcelableArrayList("checkerInformation", checkers);
        savedInstanceState.putParcelable("currentGameState", rules);
        savedInstanceState.putFloat("pointRadius", view.getPreferredRadius());
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
