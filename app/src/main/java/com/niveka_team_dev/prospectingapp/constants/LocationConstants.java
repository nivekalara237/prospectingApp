package com.niveka_team_dev.prospectingapp.constants;

public class LocationConstants {
    public static final String BROADSCAST_ACTION = "com.multicanalservices.livraisonexpressapp.android.action.broadcast";
    public static final int LOCATION_INTERVAL = 1000;
    public static final int FASTEST_LOCATION_INTERVAL = 1000;

    public static final String TABLE_NAME = "gps_location_coursier";
    public static final String _ID = "_id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ALTITUDE = "altitude";
    public static final String PROVIDER = "provider";
    public static final String TIME = "_time";
    public static final String STATUS = "status";
    public static final String DELETED_AT = "deleted_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String CREATED_AT = "created_at";

    public static String[] COLUMNS = {
            _ID,
            LATITUDE,
            LONGITUDE,
            ALTITUDE,
            PROVIDER,
            TIME,
            STATUS,
            CREATED_AT,
            DELETED_AT,
            UPDATED_AT
    };

    public final static String CREATE_TABLE_SQL = ""
            + "CREATE TABLE "+TABLE_NAME+" ( "
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LATITUDE + " DOUBLE NULL, "
            + LONGITUDE + " DOUBLE NULL, "
            + ALTITUDE + " DOUBLE NULL, "
            + PROVIDER + " TEXT NULL, "
            + TIME + " BIGINT NULL, "
            + STATUS + " BOOLEAN NOT NULL DEFAULT FALSE, "
            + CREATED_AT + " TEXT NULL, "
            + DELETED_AT + " TEXT NULL, "
            + UPDATED_AT + " TEXT NULL )";

//table disponibilité coursier

    public static final String TABLE_DISPONIBILITY_NAME = "disponibilite_coursier";
    public static final String DISPO_ID = "_id";
    public static final String DISPO_DATE = "date_dispo";
    public static final String DISPO_STATUT = "status";
    public static final String DISPO_DATE_INDISPO = "date_indispo";
    public static final String DISPO_COURSE_ID = "course_id";

    public static String[] DISPO_COLUMNS = {
            DISPO_ID,
            DISPO_STATUT,
            DISPO_DATE,
            DISPO_DATE_INDISPO,
            DISPO_COURSE_ID
    };

    public final static String CREATE_TABLE_DISPO_SQL = ""
            + "CREATE TABLE "+TABLE_DISPONIBILITY_NAME+" ( "
            + DISPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DISPO_STATUT + " BOOLEAN NOT NULL DEFAULT FALSE, "
            + DISPO_DATE_INDISPO + "TEXT NULL, "
            + DISPO_COURSE_ID + "BIGINT NULL, "
            + DISPO_DATE + " TEXT NULL )";

}
