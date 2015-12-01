package com.example.robin.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.Checker;
import model.Point;

/**
 * Created by Robin on 2015-12-01.
 */
public class NineMensMorrisView extends View {
    Bitmap background;
    List<Point> points = new ArrayList<>();
    List<Checker> checkers = new ArrayList<>();
    Checker lastTouchedChecker;

    public NineMensMorrisView(Context context) {
        super(context);

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

        // Create board points
        createPoints();

        // TODO: Fix later
        // Add checkers
        checkers.add(new Checker(100f, 100f, 50, Color.BLUE));
        checkers.add(new Checker(200f, 200f, 50, Color.RED));
        checkers.add(new Checker(200f, 500f, 50, Color.BLUE));
        checkers.add(new Checker(100f, 500f, 50, Color.RED));
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background, 0, 0, null);


        // Draw points on board
        for (Point currentPoint : points) {
            float x = currentPoint.getX();
            float y = currentPoint.getY();
            float radius = currentPoint.getRadius();
            Paint checkerPaint = currentPoint.getPaint();

            canvas.drawCircle(x, y, radius, checkerPaint);
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
        // TODO: Move to methods, possibly in model classes

        // Get touch coordinates
        float x = event.getX();
        float y = event.getY();
//        showToast("Coordinates: x: " + x + ", y: " + y);

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
}
