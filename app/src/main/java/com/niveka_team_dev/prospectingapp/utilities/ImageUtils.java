package com.niveka_team_dev.prospectingapp.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.niveka_team_dev.prospectingapp.R;

import java.io.File;

public class ImageUtils {
    public static Bitmap textAsBitmap(Context context, String text, int textColor) {
        String fontName = "futur";
        float textSize = 50;
        //Typeface font = Typeface.createFromAsset(context.getAssets(), String.format("%s.ttf", fontName));
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        //paint.setTypeface(font);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);
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
        //Typeface font = Typeface.createFromAsset(context.getAssets())
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        //paint.setTypeface(font);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 20f);
        int h = (int)context.getResources().getDimension(R.dimen.chaneel_item_heigth);
        Log.e("H",h+"");
        Bitmap image = Bitmap.createBitmap(h, h, Bitmap.Config.ARGB_8888);
        //Bitmap i = Bitmap.createBitmap()
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, h/2, baseline, paint);

        return image;
    }

    public Bitmap drawTextToBitmap(Context gContext,int gResId, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap =
                BitmapFactory.decodeResource(resources, gResId);

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (14 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

    public static  Bitmap drawText(String text, int textWidth, int color) {

        // Get text dimensions
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.parseColor("#ff00ff"));
        textPaint.setTextSize(30);

        StaticLayout mTextLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap b = Bitmap.createBitmap(textWidth, mTextLayout.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(b);

        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(0, 0);
        mTextLayout.draw(c);
        c.restore();

        return b;
    }

    public static Bitmap pathToBitmap(Context ctx,String path){
        File f = new File(path);
        Bitmap d = new BitmapDrawable(ctx.getResources(), f.getAbsolutePath()).getBitmap();
        return com.fxn.utility.Utility.getScaledBitmap(512, d);
    }

    public static Bitmap fileToBitmap(Context ctx,File f){
        Bitmap d = new BitmapDrawable(ctx.getResources(), f.getAbsolutePath()).getBitmap();
        return com.fxn.utility.Utility.getScaledBitmap(512, d);
    }




}
