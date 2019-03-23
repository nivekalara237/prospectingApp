package com.niveka_team_dev.prospectingapp.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

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
    public static final String BASE_URL = "http://192.168.0.192:8080/api/";
    public static final int API_TIMEOUT = 60*2;

    public static int randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }

    public static String ramdomString(int len){
        StringBuilder rs = new StringBuilder();
        char[] i= "abcdefghijklmnopkrstuvwxyz1234567890".toCharArray();
        int r = (new Random()).nextInt(i.length-1);
        for (int o=0;o<len;o++){
            rs.append(String.valueOf(i[o]));
        }
        return rs.toString();
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

    public static void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public static void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getRealPathFromURI2(Context mContext,Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

/*    public static String getImageFilePath(Context ctx,Uri uri) {
        String path = null, image_id = null;

        Cursor cursor = ctx.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            image_id = cursor.getString(0);
            image_id = image_id.substring(image_id.lastIndexOf(":") + 1);
            cursor.close();
        }

        Cursor cursor = ctx.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor!=null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }*/

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getMimeType(Context context,Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static Object[] getFormattedFileSize(long size) {
        String[] suffixes = new String[] { "Ko", "Mo", "Go", "To" };
        Object[] res = new String[2];

        double tmpSize = size;
        int i = 0;

        while (tmpSize >= 1024) {
            tmpSize /= 1024.0;
            i++;
        }

        // arrondi à 10^-2
        tmpSize *= 100;
        tmpSize = (int) (tmpSize + 0.5);
        tmpSize /= 100;

        res[0]=tmpSize;
        res[1] = suffixes[i];

        return res;
    }
}
