package model;

import android.graphics.Paint;

/**
 * This class represents a man (checker) in the Nine Men's Morris game.
 *
 * Created by Robin on 2015-12-01.
 */
public class Checker {
    private float x;
    private float y;
    private float radius;
    private Paint paint;
    private boolean selected;

    public Checker() {
    }

    public Checker(float x, float y, float radius, int color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.paint = new Paint();
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL);
        this.selected = false;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
