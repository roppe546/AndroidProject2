package com.example.robin.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the board of a Nine Men's Morris game.
 *
 * Created by Robin on 2015-12-01.
 */
public class Board {
    private ArrayList<Point> points;
    private ArrayList<Checker> checkers;

    public Board() {
        points = new ArrayList<>();
        checkers = new ArrayList<>();
    }

    public Board(ArrayList<Point> points, ArrayList<Checker> checkers) {
        this.points = points;
        this.checkers = checkers;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Checker> getCheckers() {
        return checkers;
    }

    public void setCheckers(ArrayList<Checker> checkers) {
        this.checkers = checkers;
    }

}
