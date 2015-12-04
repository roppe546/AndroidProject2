package com.example.robin.test;

import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * This class represents a man (checker) in the Nine Men's Morris game.
 *
 * Created by Robin on 2015-12-01.
 */
public class Checker implements Parcelable, Serializable {
    private float x;
    private float y;
    private float radius;
//    private Paint paint;
    private int color;
    private boolean selected;
    private int onPoint = -1;
    private float lastPointX;
    private float lastPointY;

    public Checker() {
    }

    public Checker(float x, float y, float radius, int color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
//        this.paint = new Paint();
//        this.paint.setColor(color);
//        this.paint.setStyle(Paint.Style.FILL);
        this.color = color;
        this.selected = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.x);
        dest.writeFloat(this.y);
        dest.writeFloat(this.radius);
//        dest.writeInt(this.paint.getColor());

        if (selected) {
            dest.writeBooleanArray(new boolean[1]);
        }
        else {
            dest.writeBooleanArray(new boolean[0]);
        }

        dest.writeInt(this.onPoint);

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

//    public Paint getPaint() {
//        return paint;
//    }
//
//    public void setPaint(Paint paint) {
//        this.paint = paint;
//    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getOnPoint() {
        return onPoint;
    }

    public void setOnPoint(int onPoint) {
        this.onPoint = onPoint;
    }

    public float getLastPointX() {
        return lastPointX;
    }

    public void setLastPointX(float lastPointX) {
        this.lastPointX = lastPointX;
    }

    public float getLastPointY() {
        return lastPointY;
    }

    public void setLastPointY(float lastPointY) {
        this.lastPointY = lastPointY;
    }
}
