package com.example.robin.test;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.example.robin.controller.NineMensMorrisGame;
import com.example.robin.model.Checker;
import com.example.robin.model.Point;

/**
 * Created by Robin on 2015-12-01.
 */
public class NineMensMorrisView extends View {

    private Bitmap background;
    private List<Point> points = new ArrayList<>();
    private ArrayList<Checker> checkers = new ArrayList<>();
    private Checker lastTouchedChecker;
    private NineMensMorrisGame game;

    public NineMensMorrisView(Context context, NineMensMorrisGame game) {
        super(context);
        this.game = game;

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

        // TODO: Fix later
        // Add checkers
//        checkers.add(new Checker(100f, 100f, 50, Color.BLUE));
//        checkers.add(new Checker(200f, 200f, 50, Color.RED));
//        checkers.add(new Checker(200f, 500f, 50, Color.BLUE));
//        checkers.add(new Checker(100f, 500f, 50, Color.RED));
        game.init(lastTouchedChecker, checkers, points);
    }

    private void createPoints() {
        int screenWidth = this.getWidth();
        int screenHeight = this.getHeight();
        int x, y, x1, y1;

        Configuration config = getResources().getConfiguration();

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            x = screenWidth / 2;
            y = screenHeight / 2;

            x1 = screenWidth / 14;
            y1 = screenWidth / 14;
        }
        else {
            x = screenWidth / 2;
            y = screenHeight / 2;

            x1 = screenWidth / 8;
            y1 = screenWidth / 8;
        }

        int number = 1;

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

                points.get(number - 1).setNumber(number++);
                factor += 1;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background, 0, 0, null);

        // Create board points
        createPoints();

        int i = 0;
        // Draw points on board
        for (Point currentPoint : points) {
            float x = currentPoint.getX();
            float y = currentPoint.getY();
            float radius = currentPoint.getRadius();
            Paint checkerPaint = currentPoint.getPaint();

            canvas.drawCircle(x, y, radius, checkerPaint);

            //bugfixing
//            checkerPaint.setTextSize(75);
//            canvas.drawText(String.valueOf(i++), x, y, checkerPaint);
        }

        for (Point currentPoint : points) {

        }


        // Draw rectangles on board
        Rect outer = new Rect();
        outer.set((int) points.get(2).getX(), (int) points.get(2).getY(), (int) points.get(14).getX(), (int) points.get(14).getY());

        Rect middle = new Rect();
        middle.set((int) points.get(1).getX(), (int) points.get(1).getY(), (int) points.get(13).getX(), (int) points.get(13).getY());

        Rect inner = new Rect();
        inner.set((int) points.get(0).getX(), (int) points.get(0).getY(), (int) points.get(12).getX(), (int) points.get(12).getY());

        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(outer, paint);
        canvas.drawRect(middle, paint);
        canvas.drawRect(inner, paint);

        // Draw lines on board
        canvas.drawLine(points.get(0).getX(), points.get(0).getY(), points.get(2).getX(), points.get(2).getY(), paint);
        canvas.drawLine(points.get(3).getX(), points.get(3).getY(), points.get(5).getX(), points.get(5).getY(), paint);
        canvas.drawLine(points.get(6).getX(), points.get(6).getY(), points.get(8).getX(), points.get(8).getY(), paint);
        canvas.drawLine(points.get(9).getX(), points.get(9).getY(), points.get(11).getX(), points.get(11).getY(), paint);
        canvas.drawLine(points.get(12).getX(), points.get(12).getY(), points.get(14).getX(), points.get(14).getY(), paint);
        canvas.drawLine(points.get(15).getX(), points.get(15).getY(), points.get(17).getX(), points.get(17).getY(), paint);
        canvas.drawLine(points.get(18).getX(), points.get(18).getY(), points.get(20).getX(), points.get(20).getY(), paint);
        canvas.drawLine(points.get(21).getX(), points.get(21).getY(), points.get(23).getX(), points.get(23).getY(), paint);


        // Draw checkers
        for (Checker currentChecker : checkers) {
            float x = currentChecker.getX();
            float y = currentChecker.getY();
            float radius = currentChecker.getRadius();
            Paint checkerPaint = currentChecker.getPaint();

            canvas.drawCircle(x, y, radius, checkerPaint);

            // Put stroke around circle if selected
            if (currentChecker.isSelected()) {
                checkerPaint = new Paint();
                checkerPaint.setAntiAlias(true);
                checkerPaint.setColor(Color.CYAN);
                checkerPaint.setStrokeWidth(10);
                checkerPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(x, y, radius, checkerPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        game.newEvent(event);

        // Invalidate to redraw view
        invalidate();
        return super.onTouchEvent(event);
    }

    private void showToast(String string) {
        Context context = this.getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, string, duration);
        toast.show();
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public ArrayList<Checker> getCheckers() {
        return checkers;
    }

    public void setCheckers(ArrayList<Checker> checkers) {
        this.checkers = checkers;
    }

    public Checker getLastTouchedChecker() {
        return lastTouchedChecker;
    }

    public void setLastTouchedChecker(Checker lastTouchedChecker) {
        this.lastTouchedChecker = lastTouchedChecker;
    }
}
