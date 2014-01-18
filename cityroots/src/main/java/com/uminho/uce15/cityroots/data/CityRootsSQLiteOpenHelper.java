package com.uminho.uce15.cityroots.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CityRootsSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cityroots.db";
    private static final String DATABASE_CREATE_FILENAME = "create.sql";
    private static final String DATABASE_DROP_FILENAME = "drop.sql";
    private static final int DATABASE_VERSION = 1;
    private static final String LOGGER_TAG = "SQLiteOpenHelper";

    public CityRootsSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createSQL = null;

        try {
            createSQL = IOUtils.toString(new FileInputStream(new File(DATABASE_CREATE_FILENAME)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        database.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i2) {
        Log.d(LOGGER_TAG, "Upgrading database");

        String dropSQL = null;

        try {
            dropSQL = IOUtils.toString(new FileInputStream(new File(DATABASE_DROP_FILENAME)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        database.execSQL(dropSQL);

        this.onCreate(database);

    }
}
