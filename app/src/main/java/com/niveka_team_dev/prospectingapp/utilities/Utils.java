package com.niveka_team_dev.prospectingapp.utilities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import com.github.thunder413.datetimeutils.DateTimeUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Utils {
    public static final String PREFRENCES_FILE = "prospecting_prefs";
    public static final String FIREBASE_DB_NAME = "prospecting_app";
    public static final int MAX_PROSPECTS_TO_DISPLAY_PER_PAGE = 64;//"nbre_prospect_a_afficher";
    public static final int TOTAL_MESSAGE_ITEMS_TO_LOAD = 5;


    public static int randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertJodaTimeToReadable(String jodaStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        DateTime myJodaFromString = new DateTime(jodaStr);
        return DateTimeUtils.formatWithPattern(myJodaFromString.toDate(),"dd-MM-yyyy HH:mm:ss");
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertJodaTimeToReadable2(String jodaStr){
        //DateTime myJodaFromString = new DateTime("2018-05-05T05:55:55.000-04:00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        DateTime myJodaFromString = new DateTime(jodaStr);
        return DateTimeUtils.formatWithPattern(myJodaFromString.toDate(),"dd-MM-yyyy HH:mm:ss");
    }

    public static Date getDateToJoda(String jodaStr){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        Date date = null;
        DateTime dt = formatter.parseDateTime(jodaStr);
        date = dt.toDate();
        return date;
    }

    public static String currentJodaDateStr(){
        DateTime today = new DateTime().toDateTime();
        return today.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
    }

    public enum TYPE_PROSPECTION {
        ;
        public static final int SUIVI = 0;
        public static final int PROSPECTION = 1;
    }

    public enum TYPE_REPORT {
        ;
        public static final int INFO_TECH = 0;
        public static final int INGO_GEN = 1;
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }

    public static boolean isServiceRunning(Context c, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
