package com.niveka_team_dev.prospectingapp.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

public class ImageUtils {
    public static Bitmap textAsBitmap(Context context, String text, int textColor) {
        String fontName = "arvin";
        float textSize = 50;
        Typeface font = Typeface.createFromAsset(context.getAssets(), String.format("%s.ttf", fontName));
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(font);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 20f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public static Bitmap textAsBitmap(Context context, String text, int textColor,float textSize) {
        String fontName = "arvin";
        Typeface font = Typeface.createFromAsset(context.getAssets(), String.format("%s.ttf", fontName));
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(font);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 20f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}
