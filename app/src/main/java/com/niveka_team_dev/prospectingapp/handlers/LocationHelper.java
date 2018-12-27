package com.niveka_team_dev.prospectingapp.handlers;

import android.content.Context;
import android.content.Intent;

import com.niveka_team_dev.prospectingapp.services.LocationMonitoringService;

public class LocationHelper {
//    public static boolean saveLocationInDB(Context context,Location loc){
//        SQLiteDatabaseHelper db = new SQLiteDatabaseHelper(context);
//        GPSLocation gpsLocation = new GPSLocation();
//        gpsLocation.setAltitude(loc.getAltitude());
//        gpsLocation.setLatitude(loc.getLatitude());
//        gpsLocation.setLongitude(loc.getLongitude());
//        gpsLocation.setTime(loc.getTime());
//        gpsLocation.setStatus(true);
//        gpsLocation.setProvider(loc.getProvider());
//        return db.addLoc(gpsLocation);
//    }

    public static void stopLocationMonitoringService(Context context){
        Intent intent = new Intent(context, LocationMonitoringService.class);
        context.stopService(intent);
    }

    public static void startLocationMonitoringService(Context context){
        Intent i = new Intent(context,LocationMonitoringService.class);
        context.startService(i);
    }
}
