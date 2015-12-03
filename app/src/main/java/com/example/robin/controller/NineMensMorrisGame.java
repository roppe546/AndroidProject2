package com.example.robin.controller;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.example.robin.model.Board;
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

    private Board board;
    private Checker lastTouchedChecker;

    private boolean removeChecker = false;

    public NineMensMorrisGame(NineMensMorrisView view) {
        this.view = view;
        this.rules = new NineMenMorrisRules();

        board = new Board();
        view.initialize(board, this);

        lastTouchedChecker = null;
    }


    // TODO: make game.newEvent return true or false, if it returns true, that means something changed, so we can invalidate,
    // TODO: don't invalidate if false. this to not do uneccesary redraws on every click
    public void newEvent(MotionEvent event) {

        // Get touch coordinates
        float x = event.getX();
        float y = event.getY();
//        Toast.makeText(context, "Coordinates: x: " + x + ", y: " + y, Toast.LENGTH_SHORT).show();

        // Phase 1: Placing pieces
        int turnsLeftFaceOne = rules.getBluemarker() + rules.getBluemarker();
        if(turnsLeftFaceOne > 0) {
            addMarkerToBoard(x, y);
        }

        // Phase 2: Moving pieces
        // No checker previously selected
         else if (lastTouchedChecker == null) {

            // Find if checker was clicked
            List<Checker> checks = board.getCheckers();
            for (Checker current : checks) {
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
//            for (Checker current : checkers) {
//                float currentX = current.getX();
//                float currentY = current.getY();
//                float radius = current.getRadius();
//
//                if ((x >= currentX - radius) && (x <= currentX + radius) && (y >= currentY - radius) && (y <= currentY + radius)) {
//                    Log.i("TOUCH", "Touched checker.");
//
//                    // Register checker as touched, so it will move to new position on next touch
//                    lastTouchedChecker = current;
//                    lastTouchedChecker.setSelected(true);
//
//                    break;
//                }
//            }
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

            // Move checker to point
            else {
                Point pointTo = getPoint(x, y);
                Point pointFrom = getPoint(lastTouchedCheckerX, lastTouchedCheckerY);

                if(pointFrom == null || pointTo == null)
                    return;

                boolean isLegal = rules.legalMove(pointTo.getNumber(), pointFrom.getNumber(), rules.getTurn());
                System.out.println("isLegal: " + isLegal);
                if(isLegal) {
                    lastTouchedChecker.setX(pointTo.getX());
                    lastTouchedChecker.setY(pointTo.getY());

                    // Unselect checker
                    lastTouchedChecker.setSelected(false);
                    lastTouchedChecker.setOnPoint(pointTo.getNumber());
                    lastTouchedChecker = null;

                    System.out.println("moved to: " + pointTo.getNumber());

                }
            }
        }
    }

    private void addMarkerToBoard(float x, float y) {
        Point p = getPoint(x, y);
        if(p == null)
            return;

        if(removeChecker) {

            boolean isLegalRemove = rules.remove(p.getNumber(), rules.getTurn() + 3);
            System.out.println("isLegalRemove: " + isLegalRemove);
            if(isLegalRemove) {
                System.out.println("GOT FUCKINGH ERE DIE");
                int index = getCheckerOnPoint(p);
                board.getCheckers().remove(index);
//                checkers.remove(index);
                removeChecker = false;
                return;
            }
        }

        boolean isLegal = rules.legalMove(p.getNumber(), 0, rules.getTurn());
        if(isLegal) {
            Checker newChecker = new Checker(p.getX(), p.getY(), 50, getTurn());
            newChecker.setOnPoint(p.getNumber());

            board.getCheckers().add(newChecker);
//            checkers.add(newChecker);
            System.out.println("Checkers size: " + board.getCheckers().size());
//            turn++;
        }

        //TODO implement mill logic, have to create states if we are adding a marker or removing a marker.
        boolean isRemove = rules.remove(p.getNumber());
        System.out.println("isRemove: " + isRemove);
        if(isRemove) {
            removeChecker = true;
        }
    }

    private Point getPoint(float x, float y) {

        Point returnPoint = null;

        List<Point> pointss = board.getPoints();
        for(Point p: pointss) {

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

    private int getCheckerOnPoint(Point point) {

        int returnIndex = 0;
        for (int i = 0; i < board.getCheckers().size(); i++) {
//        for (int i = 0; i < checkers.size(); i++) {
            Checker currentChecker = board.getCheckers().get(i);
            if (point.getX() == currentChecker.getX() && point.getY() == currentChecker.getY()) {
//            if(point.getX() == checkers.get(i).getX() && point.getY() == checkers.get(i).getY()) {
                System.out.println("getCheckerOnPoint found match!");
                returnIndex = i;
                break;
            }
        }

        return returnIndex;
    }

    private int getTurn() {

        int intTurn = rules.getTurn();

        if(intTurn == NineMenMorrisRules.BLUE_MOVES)
            return Color.BLUE;

        return Color.RED;
    }

    public Board getBoard() {
        return this.board;
    }

    public NineMenMorrisRules getRules() {
        return rules;
    }
}
