package com.example.robin.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.robin.controller.NineMensMorrisGame;
import com.example.robin.model.NineMenMorrisRules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NineMensMorrisGame game;
    private NineMensMorrisView view;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("ASD : after set Content view");

        info = (TextView) findViewById(R.id.textView);
        view = (NineMensMorrisView) findViewById(R.id.nineMensMorrisView);
        game = new NineMensMorrisGame(view, this);  // Create controller, initialize board, make view aware of controller
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("STATE", "onResume");

        game.getBoard().setPoints(new ArrayList<Point>());

        saveStateToFile();

//        view.setPoints(new ArrayList<Point>());
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadStateFromFile();

        view.invalidate();
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
            startActivityForResult(new Intent(this, SettingsActivity.class), 200);
        }

        return super.onOptionsItemSelected(item);
    }

    private FileOutputStream getFileOutput(String filename) {
        try {
            return this.openFileOutput(filename, Activity.MODE_PRIVATE);
        }
        catch (FileNotFoundException e) {
            Log.i("FILE", "Failed getting file output stream");
            return null;
        }
    }

    private void saveStateToFile() {
        // Create string to write to file
        StringBuilder sb = new StringBuilder("");

        // Save game state information
        sb.append(game.getRules().returnGamePlanString() + "\n");
        sb.append(game.getRules().getBluemarker() + "\n");
        sb.append(game.getRules().getRedmarker() + "\n");
        sb.append(game.getRules().getTurn() + "\n");

        ArrayList<Checker> checkers = game.getBoard().getCheckers();

        // Save positions and colors of checkers
        for (int i = 0; i < checkers.size(); i++) {
            Checker current = checkers.get(i);

            int color = current.getColor();
//            int color = current.getPaint().getColor();
            int onPoint = current.getOnPoint();

            System.out.println(color + " " + onPoint);

            sb.append(color + " " + onPoint + "\n");
        }

        // Save arraylist for view
        try {
            FileOutputStream fos = getFileOutput("view_state");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(game.getBoard().getCheckers());
            os.close();
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Write to game state data to file
        FileOutputStream fos = getFileOutput("game_state");
        PrintWriter pw = new PrintWriter(fos);
        pw.print(sb.toString());
        pw.close();
    }

    private void loadStateFromFile() {
        FileInputStream inputStream = null;
        BufferedReader reader;

        // Read view state
        ArrayList<Checker> checkers;

        ObjectInputStream is = null;
        FileInputStream fis = null;
        try {
            fis = openFileInput("view_state");
            is = new ObjectInputStream(fis);
            checkers = (ArrayList<Checker>) is.readObject();

            is.close();
            fis.close();

            game.getBoard().setCheckers(checkers);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        // Read game state
        try {
            inputStream = openFileInput("game_state");
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String current = reader.readLine();

            // Gameplan
            String[] split = current.split(" ");
            int[] arr = new int[25];
            for (int i = 0; i < split.length; i++) {
                arr[i] = Integer.parseInt(split[i]);
            }
            game.getRules().setGameplan(arr);
            System.out.println(game.getRules().returnGamePlanString());

            // Blue markers
            current = reader.readLine();
            game.getRules().setBluemarker(Integer.parseInt(current));
            System.out.println(game.getRules().getBluemarker());

            // Red markers
            current = reader.readLine();
            game.getRules().setRedmarker(Integer.parseInt(current));
            System.out.println(game.getRules().getRedmarker());

            // Turn
            current = reader.readLine();
            game.getRules().setTurn(Integer.parseInt(current));
            System.out.println(game.getRules().getTurn());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("123", "got here");

        if (resultCode == RESULT_OK && requestCode == 200) {
            int option = data.getExtras().getInt("option");
            if(option == 1) {
                System.out.println("opt 1");
                Log.i("123", "in option 1");
                // Delete files holding state information
                File dir = getFilesDir();
                Log.i("123", dir.toString());
                File game_state = new File(dir, "game_state");
                File view_state = new File(dir, "view_state");
                boolean s = game_state.delete();
                Log.i("123", "deleting file game_state: " + s);
                s = view_state.delete();
                Log.i("123", "deleting file view_state: " + s);

                restart();
            }
            else if(option == 2) {
                System.out.println("opt 2");
            } else if(option == 3) {
                System.out.println("opt 3");
            }
        }

    }

    private void restart() {
        info = (TextView) findViewById(R.id.textView);
        view = (NineMensMorrisView) findViewById(R.id.nineMensMorrisView);
        game = new NineMensMorrisGame(view, this);  // Create controller, initialize board, make view aware of controller
        view.invalidate();
    }
}
