package com.example.robin.controller;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.robin.model.Checker;
import com.example.robin.model.Point;
import com.example.robin.test.NineMensMorrisView;

import com.example.robin.model.NineMenMorrisRules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peonsson on 2015-12-01.
 */

/**
 * This class handles the flow of the game.
 */
public class NineMensMorrisGame {

    private NineMensMorrisView view;
    private NineMenMorrisRules rules;
    private Context context;

    private Checker lastTouchedChecker;
    private List<Checker> checkers;
    private List <Point> points;

    public NineMensMorrisGame(Context context, NineMensMorrisView view) {
        this.context = context;
        this.view = view;
        this.rules = new NineMenMorrisRules();
    }

    public void init(Checker lastTouchedChecker, List<Checker> checkers,List <Point> points) {
        this.lastTouchedChecker = lastTouchedChecker;
        this.checkers = checkers;
        this.points = points;
    }

    public void newEvent(MotionEvent event) {

        // Get touch coordinates
        float x = event.getX();
        float y = event.getY();
//        Toast.makeText(context, "Coordinates: x: " + x + ", y: " + y, Toast.LENGTH_SHORT).show();

        // Add the first 9 checkers per player to the board
//        if(checkers.size() < 18) {
//
//        }

        // No checker previously selected
         if (lastTouchedChecker == null) {
            // Find if checker was clicked
            for (Checker current : checkers) {
                float currentX = current.getX();
                float currentY = current.getY();
                float radius = current.getRadius();

                if ((x >= currentX - radius) && (x <= currentX + radius) && (y >= currentY - radius) && (y <= currentY + radius)) {
                    Log.i("TOUCH", "Touched checker.");

                    // Register checker as touched, so it will move to new position on next touch
                    lastTouchedChecker = current;
                    lastTouchedChecker.setSelected(true);

                    break;
                }
            }
        }
        // A checker was previously selected
        else {
            float lastTouchedCheckerX = lastTouchedChecker.getX();
            float lastTouchedCheckerY = lastTouchedChecker.getY();
            float radius = lastTouchedChecker.getRadius();

            // Check if same checker was touched again
            if ((x >= lastTouchedCheckerX - radius) && (x <= lastTouchedCheckerX + radius) && (y >= lastTouchedCheckerY - radius) && (y <= lastTouchedCheckerY + radius)) {
                // Same checker was touched again, unselect it
                Log.i("TOUCH", "Touched same checker, unselect it.");
                lastTouchedChecker.setSelected(false);
                lastTouchedChecker = null;
            }
            else {
                // Move checker to point
                for (Point currentPoint : points) {

                    float currentX = currentPoint.getX();
                    float currentY = currentPoint.getY();
                    // * 2 to make it easier to touch the point
                    float currentPointRadius = currentPoint.getRadius() * 2;

                    if ((x >= currentX - currentPointRadius) && (x <= currentX + currentPointRadius) && (y >= currentY - currentPointRadius) && (y <= currentY + currentPointRadius)) {
                        Log.i("TOUCH", "Moved checker to new point");

                        lastTouchedChecker.setX(currentX);
                        lastTouchedChecker.setY(currentY);

                        break;
                    }
                }

                // Unselect checker
                lastTouchedChecker.setSelected(false);
                lastTouchedChecker = null;
            }
        }
    }

    private void createPoints() {
        //TODO: Remove hardcoding and base x, y on screen size instead
        int x = 500;
        int y = 500;

        int x1 = 150;
        int y1 = 150;

        for (int i = 0; i < 8; i++) {
            int factor = 1;
            for (int j = 0; j < 3; j++) {
                if (i == 0) {
                    points.add(new Point(x - x1 * factor, y - y1 * factor));
                }
                else if (i == 1) {
                    points.add(new Point(x, y - y1 * factor));
                }
                else if (i == 2) {
                    points.add(new Point(x + x1 * factor, y - y1 * factor));
                }
                else if (i == 3) {
                    points.add(new Point(x + x1 * factor, y));
                }
                else if (i == 4) {
                    points.add(new Point(x + x1 * factor, y + y1 * factor));
                }
                else if (i == 5) {
                    points.add(new Point(x, y + y1 * factor));
                }
                else if (i == 6) {
                    points.add(new Point(x - x1 * factor, y + y1 * factor));
                }
                // i == 7
                else {
                    points.add(new Point(x - x1 * factor, y));
                }

                factor += 1;
            }
        }
    }
}
