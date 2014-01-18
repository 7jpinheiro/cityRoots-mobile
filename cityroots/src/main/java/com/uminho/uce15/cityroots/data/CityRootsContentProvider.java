package com.uminho.uce15.cityroots.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class CityRootsContentProvider extends ContentProvider {

    private CityRootsSQLiteOpenHelper databaseHelper;
    private static final String TAG = "CityRootsContentProvider";
    private static final String AUTHORITY = "com.uminho.uce15.cityroots.data.provider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final int ATTRACTIONS    = 1;
    private static final int ATTRACTIONS_ID = 2;
    private static final int EVENTS         = 3;
    private static final int EVENTS_ID      = 4;
    private static final int SERVICES       = 5;
    private static final int SERVICES_ID    = 6;
    private static final int POIS           = 7;
    private static final int POIS_ID        = 8;
    private static final int USERS          = 9;
    private static final int USERS_ID       = 10;
    private static final int ROUTES         = 11;
    private static final int ROUTES_ID      = 12;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, "attractions", ATTRACTIONS);
        matcher.addURI(AUTHORITY, "attractions/#", ATTRACTIONS_ID);

        matcher.addURI(AUTHORITY, "events", EVENTS);
        matcher.addURI(AUTHORITY, "events/#", EVENTS_ID);

        matcher.addURI(AUTHORITY, "services", SERVICES);
        matcher.addURI(AUTHORITY, "services/#", SERVICES_ID);

        matcher.addURI(AUTHORITY, "pois", POIS);
        matcher.addURI(AUTHORITY, "pois/#", POIS_ID);

        matcher.addURI(AUTHORITY, "users", USERS);
        matcher.addURI(AUTHORITY, "users/#", USERS_ID);

        matcher.addURI(AUTHORITY, "routes", ROUTES);
        matcher.addURI(AUTHORITY, "routes/#", ROUTES_ID);

        return matcher;
    }

    private synchronized Uri buildContentUri(String tableName) {
        return BASE_CONTENT_URI.buildUpon().appendPath(tableName).build();
    }

    private synchronized String getTableName(Uri uri){
        switch (this.uriMatcher.match(uri)) {
            case ATTRACTIONS: return "attractions";
            case EVENTS: return "events";
            case SERVICES: return "services";
            case POIS: return "pois";
            case USERS: return "users";
            default: throw new IllegalArgumentException();
        }
    }

    @Override
    public synchronized boolean onCreate() {
        this.databaseHelper = new CityRootsSQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public synchronized Cursor query(Uri uri,
                                     String[] projection,
                                     String selection,
                                     String[] selectionArgs,
                                     String sortOrder) {
        String tableName = this.getTableName(uri);

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(tableName,
                                       projection,
                                       selection,
                                       selectionArgs,
                                       null,
                                       null,
                                       sortOrder);

        return cursor;
    }

    @Override
    public synchronized String getType(Uri uri) {
        // TODO
        return null;
    }

    @Override
    public synchronized Uri insert(Uri uri, ContentValues contentValues) {
        String tableName = this.getTableName(uri);

        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        long id = database.insertOrThrow(tableName, null, contentValues);

        // notify about the insertion of the new values
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(this.buildContentUri(tableName), id);
    }

    @Override
    public synchronized int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = this.getTableName(uri);

        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        int numberOfRowsAffected = database.delete(tableName, selection, selectionArgs);

        // notify about deletion of values
        getContext().getContentResolver().notifyChange(uri, null);

        return numberOfRowsAffected;
    }

    @Override
    public synchronized int update(Uri uri,
                                   ContentValues contentValues,
                                   String selection,
                                   String[] selectionArgs) {
        String tableName = this.getTableName(uri);

        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        int numberOfRowsAffected = database.update(tableName,
                                                   contentValues,
                                                   selection,
                                                   selectionArgs);
        
        // notify about deletion of values
        getContext().getContentResolver().notifyChange(uri, null);

        return numberOfRowsAffected;
    }
}
