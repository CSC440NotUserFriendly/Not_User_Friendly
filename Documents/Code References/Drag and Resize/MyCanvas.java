/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.DisplayExp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 * @author Brad
 */
public class MyCanvas extends View implements View.OnTouchListener{

    Bitmap original;
    Bitmap myImg;
    Paint p;
    //Center of image
    int xCenter;
    int yCenter;
    //Top left
    int xLeft;
    int yTop;
    //Bottom right
    int xRight;
    int yBottom;

    int width;
    int height;

    final int FATFINGER = 20;

    public MyCanvas(Context context){
        super(context);

        //Making copies of the original to preserve resolution when resizing
        original = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        myImg = Bitmap.createScaledBitmap(original, 100, 100, true);
        width = myImg.getWidth();
        height = myImg.getHeight();

        xLeft = super.getWidth()/2;
        yTop = super.getWidth()/2;
        setLoc(xLeft, yTop);

        p = new Paint();
        p.setColor(Color.WHITE);
        p.setFakeBoldText(true);

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas){

        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        canvas.drawBitmap(myImg, xLeft,
            yTop, null);

        canvas.drawText("xTouch = " + xCenter + " yTouch = " + yCenter, 10, 10, p);
        
    }

    void setLoc(int x, int y) {

        xLeft = x;
        yTop = y;

        xCenter = x + width/2;
        yCenter = y + height/2;

        xRight = xLeft + width;
        yBottom = yTop + height;
    }

    void newImage(int x, int y){

        width = x - xLeft;
        height = y - yTop;

        myImg = Bitmap.createScaledBitmap(original, width, height, true);

        setLoc(xLeft, yTop); //We want the top left to be fixed when resizing
    }

    public boolean onTouch(View view, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        //If touched in bottom right resize
        if(Math.abs(x - xRight) < FATFINGER &&
                Math.abs(y - yBottom) < FATFINGER){

            newImage(x, y);
           
            invalidate(); //Forces a redraw, same as repaint.

        }
        //Touched anywhere else on the picture to move
        else if(x > (xLeft - FATFINGER) &&
                x < (xRight + FATFINGER) &&
                y > (yTop - FATFINGER) &&
                y < (yBottom + FATFINGER)){

            setLoc(x - width/2, y - height/2);
            invalidate();
        }

        return true;
    }
    



}
