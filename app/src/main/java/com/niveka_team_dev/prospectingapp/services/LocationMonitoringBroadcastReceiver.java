package com.niveka_team_dev.prospectingapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;


import java.util.HashMap;
import java.util.Map;


public class LocationMonitoringBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }
//    public static final String BROADCAST = LocationConstants.BROADSCAST_ACTION;
//    public  final static  String TAG = LocationMonitoringBroadcastReceiver.class.getSimpleName();
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Bundle extras = intent.getExtras();
//        User userRemotely = UserHelpers.getCurrentUser(context);
//        MySharedPreference session = new MySharedPreference(context);
//        if (extras != null && userRemotely!=null) {
//            double lat = extras.getDouble(LocationConstants.LATITUDE);
//            double longt = extras.getDouble(LocationConstants.LONGITUDE);
//            Location location = extras.getParcelable("loc");
//            //Log.e(TAG,lat+"__"+longt);
//            DatabaseReference ref = FirebaseDatabase
//                    .getInstance()
//                    .getReference(Utils.MCS_FIRE_DB_DRIVERS_NODE);
//
//            if (this.getSteedDisponibility(context)) {
//                this.removeLocationInGeoFire("steed_"+userRemotely.getId(),ref.child(Utils.MCS_FIRE_DB_DRIVERS_UNAVAILABLE_NODE));
//                ref = ref.child(Utils.MCS_FIRE_DB_DRIVERS_AVAILABLE_NODE);
//            }else {
//
//                this.removeLocationInGeoFire("steed_"+userRemotely.getId(),ref.child(Utils.MCS_FIRE_DB_DRIVERS_AVAILABLE_NODE));
//                ref = ref.child(Utils.MCS_FIRE_DB_DRIVERS_UNAVAILABLE_NODE);
//            }
//
//            String keyGeoFire = "steed_"+String.valueOf(userRemotely.getId());
//            //String keyGeoFire = "steed_2";
//            GeoLocation geoLocation = new GeoLocation(lat,longt);
//
//            ///custum GeoLocation with data
//
//            Map<String, Object> object = new HashMap<>();
//            Map<String, Object> item = new HashMap<>();
//            assert location != null;
//            //item.put("speed",location.getSpeed());
//            //object.put("time",location.getTime());
//            object.put("username",userRemotely.getUsername());
//            object.put("telephone",userRemotely.getTelephone());
//            object.put("id",userRemotely.getId());
//            object.put("email",userRemotely.getEmail());
//            object.put("fcm_token", FirebaseInstanceId.getInstance().getToken());
//            userRemotely.setRemember_token(location.getSpeed()+"");
//
//            //item.put("fcm_token",userRemotely.getFcm_token());
//            item.put("node1","user");
//            item.put("node2","speed");
//            item.put("node3","time");
//            item.put("node4","course_id");
//            item.put("object1",object);
//            item.put("object2",location.getSpeed());
//            item.put("object3",location.getTime());
//            item.put("object4",session.retrieveDataLong(Utils.STEED_COURSE_ID_RUNNING));
//
//            CustomGeoLocationOfGeoFire geoLocationForGeoFire = new CustomGeoLocationOfGeoFire(ref);
//            geoLocationForGeoFire.setLocation(keyGeoFire, item, geoLocation, new GeoFire.CompletionListener() {
//                @Override
//                public void onComplete(String key, DatabaseError error) {
//                    if (error != null) {
//                        FirebaseCrash.report(error.toException());
//                        System.err.println("There was an error saving the location to GeoFire: " + error);
//                    } else {
//                        System.out.println("Location saved on server successfully!"+key);
//                    }
//                }
//            });
//        }
//    }
//
//    //si l'utilisateur est non disponible
//
//    public void removeLocationInGeoFire(String key,DatabaseReference ref){
//        CustomGeoLocationOfGeoFire geoLocationForGeoFire = new CustomGeoLocationOfGeoFire(ref);
//        geoLocationForGeoFire.removeLocation(key, new GeoFire.CompletionListener() {
//            @Override
//            public void onComplete(String key, DatabaseError error) {
//                if (error != null) {
//                    FirebaseCrash.setCrashCollectionEnabled(true);
//                    FirebaseCrash.report(error.toException());
//                    System.err.println("There was an error saving the location to GeoFire: " + error);
//                } else {
//                    System.out.println("Location remeved on server successfully!"+key);
//                }
//            }
//        });
//    }
//
//    private String getSteedNodeStatus(Context c){
//        String s = Utils.MCS_FIRE_DB_DRIVERS_AVAILABLE_NODE;
//        MySharedPreference session = new MySharedPreference(c);
//        if (session.retrieveDataBoolean(Utils.STEED_DISPONIBILITY))
//            s = Utils.MCS_FIRE_DB_DRIVERS_AVAILABLE_NODE;
//        else
//            s = Utils.MCS_FIRE_DB_DRIVERS_UNAVAILABLE_NODE;
//        return s;
//    }
//
//    private boolean getSteedDisponibility(Context c){
//        MySharedPreference session = new MySharedPreference(c);
//        return session.retrieveDataBoolean(Utils.STEED_DISPONIBILITY);
//    }
}
