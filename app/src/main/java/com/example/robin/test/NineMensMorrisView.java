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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.robin.controller.NineMensMorrisGame;
import com.example.robin.test.Board;
import com.example.robin.test.Checker;
import com.example.robin.test.Point;

/**
 * Created by Robin on 2015-12-01.
 */
public class NineMensMorrisView extends View {

    private final Bitmap blueResizedBitmap;
    private final Bitmap greenResizedBitmap;
    private final Bitmap greyResizedBitmap;
    private final Bitmap orangeResizedBitmap;
    private final Bitmap redResizedBitmap;
    private final Bitmap yellowResizedBitmap;

    private int _background = 1;
    private Bitmap background1;
    private Bitmap background2;
    private Bitmap background3;

    private NineMensMorrisGame game;
    private Board board;
    private Checker lastTouchedChecker;
    private float preferredRadius = -1;

    private String player1color = "red";
    private String player2color = "blue";

    public NineMensMorrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        background1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        background2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.grey2);
        background3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.gray3);

        Bitmap blue = BitmapFactory.decodeResource(getResources(), R.drawable.circle_blue);
        blueResizedBitmap = Bitmap.createScaledBitmap(blue, 130, 130, false);

        Bitmap green = BitmapFactory.decodeResource(getResources(), R.drawable.circle_green);
        greenResizedBitmap = Bitmap.createScaledBitmap(green, 130, 130, false);

        Bitmap grey = BitmapFactory.decodeResource(getResources(), R.drawable.circle_grey);
        greyResizedBitmap = Bitmap.createScaledBitmap(grey, 130, 130, false);

        Bitmap orange = BitmapFactory.decodeResource(getResources(), R.drawable.circle_orange);
        orangeResizedBitmap = Bitmap.createScaledBitmap(orange, 130, 130, false);

        Bitmap red = BitmapFactory.decodeResource(getResources(), R.drawable.circle_red);
        redResizedBitmap = Bitmap.createScaledBitmap(red, 130, 130, false);

        Bitmap yellow = BitmapFactory.decodeResource(getResources(), R.drawable.circle_yellow);
        yellowResizedBitmap = Bitmap.createScaledBitmap(yellow, 130, 130, false);


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

        if(_background == 1) {
            canvas.drawBitmap(background1, 0, 0, null);
        } else if (_background == 2) {
            canvas.drawBitmap(background2, 0, 0, null);
        } else {
            canvas.drawBitmap(background3, 0, 0, null);
        }

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
            Paint checkerPaint = new Paint();
            checkerPaint.setColor(currentChecker.getColor());

            if(currentChecker.getColor() == Color.RED) {

                if (player1color.equals("blue")) {
                    canvas.drawBitmap(blueResizedBitmap, x - 65, y - 65, null);
                } else if (player1color.equals("green")) {
                    canvas.drawBitmap(greenResizedBitmap, x - 65, y - 65, null);
                } else if (player1color.equals("grey")) {
                    canvas.drawBitmap(greyResizedBitmap, x - 65, y - 65, null);
                } else if (player1color.equals("orange")) {
                    canvas.drawBitmap(orangeResizedBitmap, x - 65, y - 65, null);
                } else if (player1color.equals("red")) {
                    canvas.drawBitmap(redResizedBitmap, x - 65, y - 65, null);
                } else if (player1color.equals("yellow")) {
                    canvas.drawBitmap(yellowResizedBitmap, x - 65, y - 65, null);
                }
            } else {

                if (player2color.equals("blue")) {
                    canvas.drawBitmap(blueResizedBitmap, x - 65, y - 65, null);
                } else if (player2color.equals("green")) {
                    canvas.drawBitmap(greenResizedBitmap, x - 65, y - 65, null);
                } else if (player2color.equals("grey")) {
                    canvas.drawBitmap(greyResizedBitmap, x - 65, y - 65, null);
                } else if (player2color.equals("orange")) {
                    canvas.drawBitmap(orangeResizedBitmap, x - 65, y - 65, null);
                } else if (player2color.equals("red")) {
                    canvas.drawBitmap(redResizedBitmap, x - 65, y - 65, null);
                } else if (player2color.equals("yellow")) {
                    canvas.drawBitmap(yellowResizedBitmap, x - 65, y - 65, null);
                }
            }

            // Put stroke around circle if selected
            if (currentChecker.isSelected()) {
                checkerPaint = new Paint();
                checkerPaint.setAntiAlias(true);
                checkerPaint.setColor(Color.WHITE);
                checkerPaint.setStrokeWidth(10);
                checkerPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(x, y, 60, checkerPaint);
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

    public String getPlayer1color() {
        return player1color;
    }

    public void setPlayer1color(String player1color) {
        this.player1color = player1color;
    }

    public String getPlayer2color() {
        return player2color;
    }

    public void setPlayer2color(String player2color) {
        this.player2color = player2color;
    }

    public int get_background() {
        return _background;
    }

    public void set_background(int _background) {
        this._background = _background;
    }
}
