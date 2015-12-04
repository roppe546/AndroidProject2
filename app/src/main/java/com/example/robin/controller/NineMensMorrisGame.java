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
    public void newEvent(float x, float y) {

        if(isGameOver)
            return;
        // Get touch coordinates
//        float x = event.getX();
//        float y = event.getY();

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
//         else if (lastTouchedChecker == null) {
//
//            // Find if checker was clicked
//            List<Checker> checks = board.getCheckers();
//            for (Checker current : checks) {
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
//        }
//
//        // A checker was previously selected
//        else {
//            float lastTouchedCheckerX = lastTouchedChecker.getX();
//            float lastTouchedCheckerY = lastTouchedChecker.getY();
//            float radius = lastTouchedChecker.getRadius();
//
//            // Check if same checker was touched again
//            if ((x >= lastTouchedCheckerX - radius) && (x <= lastTouchedCheckerX + radius) && (y >= lastTouchedCheckerY - radius) && (y <= lastTouchedCheckerY + radius)) {
//
//                // Same checker was touched again, unselect it
//                Log.i("TOUCH", "Touched same checker, unselect it.");
//                lastTouchedChecker.setSelected(false);
//                lastTouchedChecker = null;
//            }
//
//            // Move checker to point
//            else {
//                Point pointTo = getPoint(x, y);
//                Point pointFrom = getPoint(lastTouchedCheckerX, lastTouchedCheckerY);
//
//                if(pointFrom == null || pointTo == null)
//                    return;
//
//                int nextMove = getMarkerTurn(pointFrom);
//
//                boolean isLegal = rules.legalMove(pointTo.getNumber(), pointFrom.getNumber(), nextMove);
//                if(isLegal) {
//                    lastTouchedChecker.setX(pointTo.getX());
//                    lastTouchedChecker.setY(pointTo.getY());
//
//                    // Unselect checker
//                    lastTouchedChecker.setSelected(false);
//                    lastTouchedChecker.setOnPoint(pointTo.getNumber());
//                    lastTouchedChecker = null;
//
//                    boolean isRemove = rules.remove(pointTo.getNumber());
//                    if(isRemove) {
//                        removeChecker = true;
//                    }
//                }
//            }
//        }
        updateUI();
    }

    public boolean moveTo(float x, float y) {

        if(isGameOver)
            return false;

        Point pointTo = getPoint(x, y);
        Point pointFrom = getPoint(lastTouchedChecker.getLastPointX(), lastTouchedChecker.getLastPointY());

        if(pointFrom == null || pointTo == null)
            return false;

        System.out.println("from point number: " + pointFrom.getNumber());
        System.out.println("to point number: " + pointTo.getNumber());

        if (removeChecker) {
            boolean isLegalRemove = rules.remove(pointTo.getNumber(), rules.getTurn() + 3);
            if (isLegalRemove) {
                int index = getCheckerOnPoint(pointTo);
                board.getCheckers().remove(index);
                removeChecker = false;

                // Checking if we have a winner.
                if (rules.lose(NineMenMorrisRules.BLUE_MARKER)) {
                    System.out.println("Red have < 3 checker's. Blue wins!");
                    activity.updateUI("Blue wins!");
                    isGameOver = true;
                    Toast.makeText(activity.getApplicationContext(), "Red have < 3 checker's. Blue wins!", Toast.LENGTH_LONG).show();
                    return true;
                }
                else if (rules.lose(NineMenMorrisRules.RED_MARKER)) {
                    System.out.println("Blue have < 3 checker's. Red wins!");
                    activity.updateUI("Red wins!");
                    isGameOver = true;
                    Toast.makeText(activity.getApplicationContext(), "Blue have < 3 checker's. Red wins!", Toast.LENGTH_LONG).show();
                    return true;
                }
                updateUI();
                return true;
            }
        }

        int nextMove = getMarkerTurn(pointFrom);
        
        boolean isLegal = rules.legalMove(pointTo.getNumber(), pointFrom.getNumber(), nextMove);

        System.out.println("next move: " + nextMove);
        System.out.println("isLegal: " + isLegal);

        if(isLegal) {
            lastTouchedChecker.setOnPoint(pointTo.getNumber());
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
        updateUI();
        return true;
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

    public void addMarkerToBoard(float x, float y) {

        Point p = getPoint(x, y);
        if(p == null)
            return;

        // Removing if we got mill
        if(removeChecker) {
            System.out.println("point to remove: " + p.getNumber());
            boolean isLegalRemove = rules.remove(p.getNumber(), rules.getTurn() + 3);
            if(isLegalRemove) {
                int index = getCheckerOnPointPhaseOne(p);
                board.getCheckers().remove(index);
                removeChecker = false;
                updateUI();
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
        updateUI();
    }

    private int getCheckerOnPointPhaseOne(Point point) {
//        System.out.println("LAST POINT getCheckerOnPoint: x = " + point.getX() + ", y = " + point.getY());

        int returnIndex = -1;
        for (int i = 0; i < board.getCheckers().size(); i++) {
            Checker currentChecker = board.getCheckers().get(i);
//            System.out.println("currentChecker x = " + currentChecker.getLastPointX() + ", y = " + currentChecker.getLastPointY());
            if (((int) point.getX() == (int) currentChecker.getX()) && ((int) point.getY() == (int)currentChecker.getY())) {
                System.out.println("FOUND");
                returnIndex = i;
                break;
            }
        }
        System.out.println("index: " + returnIndex);
        return returnIndex;
    }

    private Point getPoint(float x, float y) {

        Point returnPoint = null;

        List<Point> points = board.getPoints();
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

    private int getCheckerOnPoint(Point point) {
        System.out.println("LAST POINT getCheckerOnPoint: x = " + point.getX() + ", y = " + point.getY());

        int returnIndex = -1;
        for (int i = 0; i < board.getCheckers().size(); i++) {
            Checker currentChecker = board.getCheckers().get(i);
            System.out.println("currentChecker x = " + currentChecker.getLastPointX() + ", y = " + currentChecker.getLastPointY());
            if (((int) point.getX() == (int) currentChecker.getLastPointX()) && ((int) point.getY() == (int)currentChecker.getLastPointY())) {
                System.out.println("FOUND");
                returnIndex = i;
                break;
            }
        }
        System.out.println("index: " + returnIndex);
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

    /**
     * returns the color in gameRules representation of the checker the user is trying to move.
     * @param point
     * @return
     */
    private int getMarkerTurn(Point point) {

        int index = getCheckerOnPoint(point);
//        Paint paint = board.getCheckers().get(index).getPaint();

        System.out.println("point number: " + board.getCheckers().get(index).getOnPoint());

        if (board.getCheckers().get(index).getColor() == Color.RED) {
//        if(paint.getColor() == Color.RED) {
            System.out.println("BLUE MOVES");
            return NineMenMorrisRules.BLUE_MOVES;
        }

        System.out.println("RED MOVES");
        return NineMenMorrisRules.RED_MOVES;
    }

    public NineMenMorrisRules getRules() {
        return rules;
    }

    public int getPhase() {
        int turnsLeftPhaseOne = rules.getBluemarker() + rules.getBluemarker();

        if(turnsLeftPhaseOne >= 0) {
            return 1;
        }
        return 2;
    }

    public boolean ifRemove() {
        return removeChecker;
    }

    public Checker getLastTouchedChecker() {
        return lastTouchedChecker;
    }

    public void setLastTouchedChecker(Checker lastTouchedChecker) {
        this.lastTouchedChecker = lastTouchedChecker;
    }
}
