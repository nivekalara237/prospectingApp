package com.niveka_team_dev.prospectingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public Session(Context contxt) {
        prefs = contxt.getSharedPreferences(Utils.PREFRENCES_FILE, Context.MODE_PRIVATE);
        this.editor = prefs.edit();
    }

    public void saveDataInt(String attr, int data) {
        this.editor.putInt(attr, data);
        editor.apply();
    }

    public void saveDataString(String attr, String data) {
        this.editor.putString(attr, data);
        editor.apply();
    }

    public void saveDataBoolean(String attr, boolean data) {
        this.editor.putBoolean(attr, data);
        editor.apply();
    }

    public void saveDataFloat(String attr, float data) {
        this.editor.putFloat(attr, data);
        editor.apply();
    }

    public void saveDataLong(String attr, long data) {
        this.editor.putLong(attr, data);
        editor.apply();
    }

    public void editValueInteger(String KEY, int newValue) {
        // this.editor.remove(KEY).commit();
        this.editor.putInt(KEY, newValue).commit();
    }

    public void editValueString(String KEY, String newValue) {
        // this.editor.remove(KEY).commit();
        this.editor.putString(KEY, newValue).commit();
    }

    public void editValueFloat(String KEY, float newValue) {
        // this.editor.remove(KEY).commit();
        this.editor.putFloat(KEY, newValue).commit();
    }

    public void editValueBoolean(String KEY, boolean newValue) {
        // this.editor.remove(KEY).commit();
        this.editor.putBoolean(KEY, newValue).commit();
    }

    public String retrieveDataString(String key) {
        return String.valueOf(this.prefs.getString(key, ""));
    }

    public int retrieveDataInteger(String key) {
        return this.prefs.getInt(key, 0);
    }

    public boolean retrieveDataBoolean(String key) {
        return this.prefs.getBoolean(key, false);
    }

    public float retrieveDataFloat(String key) {
        return this.prefs.getFloat(key, 0);
    }

    public float retrieveDataLong(String key) {
        return this.prefs.getLong(key, 0);
    }
}