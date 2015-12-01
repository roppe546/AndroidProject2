package com.example.robin.model;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * This class represents a point on a Nine Men's Morris board.
 *
 * Created by Robin on 2015-12-01.
 */
public class Point {
    private final float x;
    private final float y;
    private final float radius;
    private Paint paint;
    private int number;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
        this.radius = 20;
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
