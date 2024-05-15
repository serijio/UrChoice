package com.example.urchoice2.Classes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class CenterCropDrawable extends Drawable {
    private Bitmap mBitmap;

    public CenterCropDrawable(Resources res, Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }

        Rect bounds = getBounds();
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        float bitmapRatio = (float) mBitmap.getWidth() / mBitmap.getHeight();
        float canvasRatio = (float) canvasWidth / canvasHeight;

        float scale;
        float translateX = 0f;
        float translateY = 0f;

        if (bitmapRatio > canvasRatio) {
            // Scale based on width
            scale = (float) canvasWidth / mBitmap.getWidth();
            translateY = (canvasHeight - mBitmap.getHeight() * scale) / 2f;
        } else {
            // Scale based on height
            scale = (float) canvasHeight / mBitmap.getHeight();
            translateX = (canvasWidth - mBitmap.getWidth() * scale) / 2f;
        }

        canvas.save();
        canvas.translate(translateX, translateY);
        canvas.scale(scale, scale);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {}

    @Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {}

    @Override
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }
}