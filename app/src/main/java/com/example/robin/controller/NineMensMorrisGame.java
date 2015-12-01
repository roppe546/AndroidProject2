package com.example.robin.controller;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.example.robin.model.Checker;
import com.example.robin.model.Point;
import com.example.robin.test.NineMensMorrisView;

import com.example.robin.model.NineMenMorrisRules;

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

    private int turn = 0;

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
        if(turn < 18) {
            addMarkerToBoard(x, y);
        }

        // No checker previously selected
         else if (lastTouchedChecker == null) {

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

    private void addMarkerToBoard(float x, float y) {

        Point p = getPoint(x, y);
        if(p == null)
            return;


        boolean isLegal = rules.legalMove(p.getNumber(), -1, rules.getTurn());
        if(isLegal) {
            checkers.add(new Checker(p.getX(), p.getY(), 50, getTurn()));
            turn++;
        }

        //TODO implement mill logic, have to create states if we are adding a marker or removing a marker.
        boolean isRemove = rules.remove(p.getNumber());
        if(isRemove) {
//            rules.remove(getPoint().getNumber(), getTurn());
        }
        System.out.println("isRemove: " + isRemove);
    }

    private Point getPoint(float x, float y) {

        Point returnPoint = null;

        for(Point p: points) {

            float currentPointRadius = p.getRadius() * 2;
            float currentX = p.getX();
            float currentY = p.getY();

            if ((x >= currentX - currentPointRadius) && (x <= currentX + currentPointRadius) && (y >= currentY - currentPointRadius) && (y <= currentY + currentPointRadius)) {
                returnPoint = p;
                break;
            }
        }

        return returnPoint;
    }

    private int getTurn() {

        int intTurn = rules.getTurn();

        if(intTurn == NineMenMorrisRules.BLUE_MOVES)
            return Color.BLUE;
        else if(intTurn == NineMenMorrisRules.RED_MOVES)
            return Color.RED;

        return Color.YELLOW;
    }
}
