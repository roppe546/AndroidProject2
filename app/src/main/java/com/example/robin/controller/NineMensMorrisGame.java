package com.example.robin.controller;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.robin.test.Board;
import com.example.robin.test.Checker;
import com.example.robin.test.Point;
import com.example.robin.test.MainActivity;
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
    private MainActivity activity;
    private boolean isGameOver = false;

    public NineMensMorrisGame(NineMensMorrisView view, MainActivity activity) {
        this.view = view;
        this.rules = new NineMenMorrisRules();
        this.activity = activity;

        board = new Board();            // Initialize two lists (for checkers and points)
        view.initialize(board, this);   // Make view aware of the board and the controller (this)

        lastTouchedChecker = null;
        updateUI();
    }

    // TODO: make game.newEvent return true or false, if it returns true, that means something changed, so we can invalidate,
    // TODO: don't invalidate if false. this to not do uneccesary redraws on every click
    public void newEvent(MotionEvent event) {

        if(isGameOver)
            return;
        // Get touch coordinates
        float x = event.getX();
        float y = event.getY();

        // Phase 1: Placing pieces
        int turnsLeftFaceOne = rules.getBluemarker() + rules.getBluemarker();

        if(turnsLeftFaceOne >= 0) {
            addMarkerToBoard(x, y);
        }

        // Phase 2: Moving pieces
        // Removing if we got mill
        else if(removeChecker) {

            Point p = getPoint(x, y);
            if(p == null)
                return;

            boolean isLegalRemove = rules.remove(p.getNumber(), rules.getTurn() + 3);
            System.out.println("isLegalRemove: " + isLegalRemove);
            if(isLegalRemove) {
                int index = getCheckerOnPoint(p);
                board.getCheckers().remove(index);
                removeChecker = false;

                // Checking if we have a winner.
                if(rules.lose(NineMenMorrisRules.BLUE_MARKER)) {
                    System.out.println("Red have < 3 checker's. Blue wins!");
                    activity.updateUI("Blue wins!");
                    isGameOver = true;
                    Toast.makeText(activity.getApplicationContext(), "Red have < 3 checker's. Blue wins!", Toast.LENGTH_LONG).show();
                    return;
                } else if(rules.lose(NineMenMorrisRules.RED_MARKER)) {
                    System.out.println("Blue have < 3 checker's. Red wins!");
                    activity.updateUI("Red wins!");
                    isGameOver = true;
                    Toast.makeText(activity.getApplicationContext(), "Blue have < 3 checker's. Red wins!", Toast.LENGTH_LONG).show();
                    return;
                }

                updateUI();
                return;
            }
        }
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

                int nextMove = getMarkerTurn(pointFrom);

                boolean isLegal = rules.legalMove(pointTo.getNumber(), pointFrom.getNumber(), nextMove);
                if(isLegal) {
                    lastTouchedChecker.setX(pointTo.getX());
                    lastTouchedChecker.setY(pointTo.getY());

                    // Unselect checker
                    lastTouchedChecker.setSelected(false);
                    lastTouchedChecker.setOnPoint(pointTo.getNumber());
                    lastTouchedChecker = null;

                    boolean isRemove = rules.remove(pointTo.getNumber());
                    if(isRemove) {
                        removeChecker = true;
                    }
                }
            }
        }
        updateUI();
    }

    public void updateUI() {
        String str = "";

        if(rules.getTurn() == NineMenMorrisRules.RED_MOVES) {
            if(removeChecker == true) {
                str = view.getPlayer1color() + " (mill)";
            } else
                str = view.getPlayer2color() + "'s turn";
        }
        else {
            if(removeChecker == true) {
                str = view.getPlayer2color() + "'s turn (mill)";
            } else
                str = view.getPlayer1color() + "'s turn";
        }
        activity.updateUI(str);
    }

    private void addMarkerToBoard(float x, float y) {

        Point p = getPoint(x, y);
        if(p == null)
            return;

        // Removing if we got mill
        if(removeChecker) {
            boolean isLegalRemove = rules.remove(p.getNumber(), rules.getTurn() + 3);
            if(isLegalRemove) {
                int index = getCheckerOnPoint(p);
                board.getCheckers().remove(index);
                removeChecker = false;
                return;
            }
        }

        boolean isLegal = rules.legalMove(p.getNumber(), 0, rules.getTurn());
        if(isLegal) {
            Checker newChecker = new Checker(p.getX(), p.getY(), view.getPreferredRadius(), getTurn());
            newChecker.setOnPoint(p.getNumber());

            board.getCheckers().add(newChecker);
        }

        boolean isRemove = rules.remove(p.getNumber());
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
            Checker currentChecker = board.getCheckers().get(i);
            if (point.getX() == currentChecker.getX() && point.getY() == currentChecker.getY()) {
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

    private int getMarkerTurn(Point point) {

        int index = getCheckerOnPoint(point);
//        Paint paint = board.getCheckers().get(index).getPaint();

        if (board.getCheckers().get(index).getColor() == Color.RED) {
//        if(paint.getColor() == Color.RED) {
            return NineMenMorrisRules.BLUE_MOVES;
        }

        return NineMenMorrisRules.RED_MOVES;
    }

    public NineMenMorrisRules getRules() {
        return rules;
    }
}
