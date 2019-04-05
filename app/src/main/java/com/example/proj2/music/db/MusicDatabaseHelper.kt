package com.example.proj2.music.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MusicDatabaseHelper(context: Context): SQLiteOpenHelper(context, DbSettings.DB_NAME, null, DbSettings.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        var createToptrackTableQuery = "CREATE TABLE " + DbSettings.DBPlaylistEntry.TABLE + " ( " +
                DbSettings.DBPlaylistEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbSettings.DBPlaylistEntry.NAME + " TEXT NULL, " +
                DbSettings.DBPlaylistEntry.ARTIST + " TEXT NULL); "
               // DbSettings.DBPlaylistEntry.DURATION + " TEXT NULL" +
               // DbSettings.DBPlaylistEntry.URL + " TEXT NULL" +
               // DbSettings.DBPlaylistEntry.PLAYCOUNT + " TEXT NULL);"
               // DbSettings.DBPlaylistEntry.IMAGE + " TEXT NULL, " +

        db?.execSQL(createToptrackTableQuery)

    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + DbSettings.DBPlaylistEntry.TABLE)
        onCreate(db)
    }
}