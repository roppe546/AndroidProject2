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
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import com.example.robin.controller.NineMensMorrisGame;
import com.example.robin.test.Board;
import com.example.robin.test.Checker;
import com.example.robin.test.Point;

/**
 * Created by Robin on 2015-12-01.
 */
public class NineMensMorrisView extends View {

    private Bitmap blueResizedBitmap;
    private Bitmap greenResizedBitmap;
    private Bitmap greyResizedBitmap;
    private Bitmap orangeResizedBitmap;
    private Bitmap redResizedBitmap;
    private Bitmap yellowResizedBitmap;

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
            float pointX = currentChecker.getX();
            float pointY = currentChecker.getY();
            if (!currentChecker.isSelected()) {
                Point checkerPoint = board.getPoints().get(currentChecker.getOnPoint() - 1);
                pointX = checkerPoint.getX();
                pointY = checkerPoint.getY();
            }

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
                    Bitmap blue = BitmapFactory.decodeResource(getResources(), R.drawable.circle_blue);
                    blueResizedBitmap = Bitmap.createScaledBitmap(blue, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(blueResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                } else if (player1color.equals("green")) {
                    Bitmap green = BitmapFactory.decodeResource(getResources(), R.drawable.circle_green);
                    greenResizedBitmap = Bitmap.createScaledBitmap(green, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(greenResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                } else if (player1color.equals("grey")) {
                    Bitmap grey = BitmapFactory.decodeResource(getResources(), R.drawable.circle_grey);
                    greyResizedBitmap = Bitmap.createScaledBitmap(grey, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(greyResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                } else if (player1color.equals("orange")) {
                    Bitmap orange = BitmapFactory.decodeResource(getResources(), R.drawable.circle_orange);
                    orangeResizedBitmap = Bitmap.createScaledBitmap(orange, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(orangeResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                } else if (player1color.equals("red")) {
                    Bitmap red = BitmapFactory.decodeResource(getResources(), R.drawable.circle_red);
                    redResizedBitmap = Bitmap.createScaledBitmap(red, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(redResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                } else if (player1color.equals("yellow")) {
                    Bitmap yellow = BitmapFactory.decodeResource(getResources(), R.drawable.circle_yellow);
                    yellowResizedBitmap = Bitmap.createScaledBitmap(yellow, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(yellowResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                }
            } else {

                if (player2color.equals("blue")) {
                    Bitmap blue = BitmapFactory.decodeResource(getResources(), R.drawable.circle_blue);
                    blueResizedBitmap = Bitmap.createScaledBitmap(blue, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(blueResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                } else if (player2color.equals("green")) {
                    Bitmap green = BitmapFactory.decodeResource(getResources(), R.drawable.circle_green);
                    greenResizedBitmap = Bitmap.createScaledBitmap(green, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(greenResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                } else if (player2color.equals("grey")) {
                    Bitmap grey = BitmapFactory.decodeResource(getResources(), R.drawable.circle_grey);
                    greyResizedBitmap = Bitmap.createScaledBitmap(grey, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(greyResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                } else if (player2color.equals("orange")) {
                    Bitmap orange = BitmapFactory.decodeResource(getResources(), R.drawable.circle_orange);
                    orangeResizedBitmap = Bitmap.createScaledBitmap(orange, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(orangeResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                } else if (player2color.equals("red")) {
                    Bitmap red = BitmapFactory.decodeResource(getResources(), R.drawable.circle_red);
                    redResizedBitmap = Bitmap.createScaledBitmap(red, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(redResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                } else if (player2color.equals("yellow")) {
                    Bitmap yellow = BitmapFactory.decodeResource(getResources(), R.drawable.circle_yellow);
                    yellowResizedBitmap = Bitmap.createScaledBitmap(yellow, (int) getPreferredRadius(), (int) getPreferredRadius(), false);
                    canvas.drawBitmap(yellowResizedBitmap, x - getPreferredRadius() / 2, y - getPreferredRadius() / 2, null);
                }
            }

            // Put stroke around circle if selected
            if (currentChecker.isSelected()) {
                checkerPaint = new Paint();
                checkerPaint.setAntiAlias(true);
                checkerPaint.setColor(Color.WHITE);
                checkerPaint.setStrokeWidth(10);
                checkerPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(x, y, getPreferredRadius(), checkerPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        float x = event.getX();
        float y = event.getY();

        if (game.getPhase() == 1) {
            game.addMarkerToBoard(x, y);
        }
        // Phase 2
        else {
            switch (action) {
                case (MotionEvent.ACTION_DOWN):
                case (MotionEvent.ACTION_MOVE):
                    if (lastTouchedChecker == null) {

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
                                game.setLastTouchedChecker(lastTouchedChecker);
                                lastTouchedChecker.setSelected(true);

                                // Register its last point
                                lastTouchedChecker.setLastPointX(currentX);
                                lastTouchedChecker.setLastPointY(currentY);
                                System.out.println("LAST POINT : x = " + lastTouchedChecker.getLastPointX() + ", y = " + lastTouchedChecker.getLastPointY());


                                break;
                            }
                        }
                    }

                    // A checker was previously selected
                    else {
//                        System.out.println("last x : " + lastTouchedChecker.getLastPointX());
//                        System.out.println("last y : " + lastTouchedChecker.getLastPointY());


                        float lastTouchedCheckerX = board.getPoints().get(lastTouchedChecker.getOnPoint()).getX();
                        float lastTouchedCheckerY = board.getPoints().get(lastTouchedChecker.getOnPoint()).getY();
                        float radius = lastTouchedChecker.getRadius();



                        // Check if same checker was touched again
//                        if ((x >= lastTouchedCheckerX - radius) && (x <= lastTouchedCheckerX + radius) && (y >= lastTouchedCheckerY - radius) && (y <= lastTouchedCheckerY + radius)) {
//
//                            // Same checker was touched again, unselect it
//                            Log.i("TOUCH", "Touched same checker, unselect it.");
//                            lastTouchedChecker.setX(lastTouchedChecker.getLastPointX());
//                            lastTouchedChecker.setY(lastTouchedChecker.getLastPointY());
//                            lastTouchedChecker.setSelected(false);
//                            game.setLastTouchedChecker(null);
//                            lastTouchedChecker = null;
//                        }

                        // Move checker to location
//                        else {
                            lastTouchedChecker.setX(x);
                            lastTouchedChecker.setY(y);
//                        }
                    }

                    invalidate();
                    break;

                case (MotionEvent.ACTION_UP):
                    // check position, update model, ...

                    if (lastTouchedChecker != null) {
                        System.out.println("NOT NULL");
                        if (game.moveTo(x, y)) {
                            // Change point number

                            game.setLastTouchedChecker(null);
                            lastTouchedChecker.setSelected(false);
                            lastTouchedChecker = null;
                        }
                        else {
                            lastTouchedChecker.setX(lastTouchedChecker.getLastPointX());
                            lastTouchedChecker.setY(lastTouchedChecker.getLastPointY());
                            game.setLastTouchedChecker(null);
                            lastTouchedChecker.setSelected(false);
                            lastTouchedChecker = null;
                        }
                    }
                    else {
                        System.out.println("NULL");
                    }

                    break;
            }
        }

        invalidate();


        return true;

//        game.newEvent(event);
//
//        // TODO: make game.newEvent return true or false, if it returns true, that means something changed, so invalidate, don't invalidate if false
//        // Invalidate to redraw view
//        invalidate();
//        return super.onTouchEvent(event);
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
