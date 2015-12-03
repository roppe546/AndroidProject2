package com.example.robin.test;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.robin.controller.NineMensMorrisGame;
import com.example.robin.model.Board;
import com.example.robin.model.Checker;
import com.example.robin.model.Point;

/**
 * Created by Robin on 2015-12-01.
 */
public class NineMensMorrisView extends View {

    private Bitmap background;

    private Drawable blue;
    private Drawable green;
    private Drawable yellow;
    private Drawable red;

    private NineMensMorrisGame game;
    private Board board;
    private Checker lastTouchedChecker;
    private float preferredRadius = -1;

    public NineMensMorrisView(Context context, AttributeSet attrs) {
        super(context, attrs);

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        red = context.getResources().getDrawable(R.drawable.red);
        yellow = context.getResources().getDrawable(R.drawable.yellow);
        blue = context.getResources().getDrawable(R.drawable.blue);
        green = context.getResources().getDrawable(R.drawable.green);

    }

    public void initialize(Board board, NineMensMorrisGame game) {
        this.board = board;
        this.game = game;
        lastTouchedChecker = null;
    }

    private void createPoints() {
        int screenWidth = this.getWidth();
        int screenHeight = this.getHeight();
        int x, y, x1, y1;

        Configuration config = getResources().getConfiguration();

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            x = screenWidth / 2;
            y = screenHeight / 2;

            x1 = screenWidth / 18;
            y1 = screenWidth / 18;
        }
        else {
            x = screenWidth / 2;
            y = screenHeight / 2;

            x1 = screenWidth / 8;
            y1 = screenWidth / 8;
        }

        int number = 1;

        float radius = getPreferredRadius();
        ArrayList<Point> points = board.getPoints();
        for (int i = 0; i < 8; i++) {
            int factor = 1;
            for (int j = 0; j < 3; j++) {
                if (i == 0) {
                    points.add(new Point(x - x1 * factor, y - y1 * factor, radius));
                }
                else if (i == 1) {
                    points.add(new Point(x, y - y1 * factor, radius));
                }
                else if (i == 2) {
                    points.add(new Point(x + x1 * factor, y - y1 * factor, radius));
                }
                else if (i == 3) {
                    points.add(new Point(x + x1 * factor, y, radius));
                }
                else if (i == 4) {
                    points.add(new Point(x + x1 * factor, y + y1 * factor, radius));
                }
                else if (i == 5) {
                    points.add(new Point(x, y + y1 * factor, radius));
                }
                else if (i == 6) {
                    points.add(new Point(x - x1 * factor, y + y1 * factor, radius));
                }
                // i == 7
                else {
                    points.add(new Point(x - x1 * factor, y, radius));
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
        ArrayList<Point> points = board.getPoints();
        for (Point currentPoint : points) {
            float x = currentPoint.getX();
            float y = currentPoint.getY();
            float radius = currentPoint.getRadius();
            Paint checkerPaint = currentPoint.getPaint();

//            canvas.drawCircle(x, y, radius, checkerPaint);

            Rect imageBounds = canvas.getClipBounds();  // Adjust this for where you want it

            red.draw(canvas);


            //bugfixing
//            checkerPaint.setTextSize(75);
//            canvas.drawText(String.valueOf(i++), x, y, checkerPaint);
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
        canvas.drawLine(points.get(3).getX(), points.get(3).getY(), points.get(5).getX(), points.get(5).getY(), paint);
        canvas.drawLine(points.get(9).getX(), points.get(9).getY(), points.get(11).getX(), points.get(11).getY(), paint);
        canvas.drawLine(points.get(15).getX(), points.get(15).getY(), points.get(17).getX(), points.get(17).getY(), paint);
        canvas.drawLine(points.get(21).getX(), points.get(21).getY(), points.get(23).getX(), points.get(23).getY(), paint);

        // Put checkers in correct positions
        ArrayList<Checker> checkers = board.getCheckers();
        for (Checker currentChecker : checkers) {
            // Find which point the checker is on
            Point checkerPoint = board.getPoints().get(currentChecker.getOnPoint() - 1);
            float pointX = checkerPoint.getX();
            float pointY = checkerPoint.getY();

            currentChecker.setX(pointX);
            currentChecker.setY(pointY);
        }
        board.setCheckers(checkers);

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

        // TODO: make game.newEvent return true or false, if it returns true, that means something changed, so invalidate, don't invalidate if false
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

    public Checker getLastTouchedChecker() {
        return lastTouchedChecker;
    }

    public void setLastTouchedChecker(Checker lastTouchedChecker) {
        this.lastTouchedChecker = lastTouchedChecker;
    }

    public float getPreferredRadius() {
        if (preferredRadius == -1) {
            preferredRadius = this.getWidth() / 24;
        }

        return preferredRadius;
    }

    public void setPreferredRadius(float preferredRadius) {
        this.preferredRadius = preferredRadius;
    }
}
