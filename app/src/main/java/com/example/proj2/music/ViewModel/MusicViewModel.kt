package com.example.proj2.music.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.provider.BaseColumns
import android.util.Log
import com.example.proj2.music.db.DbSettings
import com.example.proj2.music.db.MusicDatabaseHelper
import com.example.proj2.music.model.TopTrack
import com.example.proj2.music.util.QueryUtils


class MusicViewModel(application: Application): AndroidViewModel(application) {
    private  var musicDBHelper: MusicDatabaseHelper = MusicDatabaseHelper(application)
    private  var musicList: MutableLiveData<ArrayList<TopTrack>> = MutableLiveData()
    var myAPIkey = "aeec2b2c8adfdbfd232e70083b0a5644"
    fun getTopTrack(): MutableLiveData<ArrayList<TopTrack>> {

        var topTrack = "?method=chart.gettoptracks&api_key=" + myAPIkey + "&format=json"
        loadTopTrack(topTrack)
        return musicList
    }

    fun getTopTrackByQueryText(query: String): MutableLiveData<ArrayList<TopTrack>> {
        var artistTopTrack = "?method=artist.gettoptracks&artist=$query&api_key=" + myAPIkey + "&format=json"
        loadArtistTopTrack(artistTopTrack)
        return  musicList
    }

    private fun loadTopTrack(query: String) {
        MusicAsyncTask().execute(query)
    }

    private fun loadArtistTopTrack(query: String) {
        ArtistMusicAsyncTask().execute(query)
    }

    @SuppressLint("StaticFieldLeak")
    inner class MusicAsyncTask: AsyncTask<String, Unit, ArrayList<TopTrack>>() {
        override fun doInBackground(vararg params: String?): ArrayList<TopTrack>? {
            return QueryUtils.fetchToptrackData(params[0]!!)
        }

        override fun onPostExecute(result: ArrayList<TopTrack>?) {
            if (result == null) {
                Log.e("RESULTS", "No Results Found")
            }
            else {
                musicList.value = result
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class ArtistMusicAsyncTask: AsyncTask<String, Unit, ArrayList<TopTrack>>() {
        override fun doInBackground(vararg params: String?): ArrayList<TopTrack>? {
            return QueryUtils.fetchArtistToptrackData(params[0]!!)
        }

        override fun onPostExecute(result: ArrayList<TopTrack>?) {
            if (result == null) {
                Log.e("RESULTS", "No Results Found")
            }
            else {
                musicList.value = result
            }
        }
    }

    fun getPlaylist(): MutableLiveData<ArrayList<TopTrack>> {
        this.loadPlaylist()
        return this.musicList
    }

    private fun loadPlaylist(): ArrayList<TopTrack> {
        var playlist: ArrayList<TopTrack> = ArrayList()
        var database = this.musicDBHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID, DbSettings.DBPlaylistEntry.NAME, DbSettings.DBPlaylistEntry.ARTIST)
        var cursor = database.query(
            DbSettings.DBPlaylistEntry.TABLE,
            projection,
            null, null, null, null, null
        )
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(DbSettings.DBPlaylistEntry.NAME))
                val artist = getString(getColumnIndexOrThrow(DbSettings.DBPlaylistEntry.ARTIST))
                val music = TopTrack(name, ArrayList(), artist, "", "", "")
                playlist.add(music)
            }
        }
        musicList.value = playlist
        /*
        while (cursor.moveToNext()) {
            var cursorId = cursor.getColumnIndex(DbSettings.DBPlaylistEntry.ID)
            var cursorName = cursor.getColumnIndex(DbSettings.DBPlaylistEntry.NAME)
            var cursorArtist = cursor.getColumnIndex(DbSettings.DBPlaylistEntry.ARTIST)
            var cursorImage = cursor.getColumnIndex(DbSettings.DBPlaylistEntry.IMAGE)
            var cursorDuration = cursor.getColumnIndex(DbSettings.DBPlaylistEntry.DURATION)
            var cursorUrl = cursor.getColumnIndex(DbSettings.DBPlaylistEntry.URL)
            var cursorPlaycount = cursor.getColumnIndex(DbSettings.DBPlaylistEntry.PLAYCOUNT)

            val imageCursor = database.query(
                DbSettings.DBPlaylistEntry.TABLE,
                arrayOf(
                    DbSettings.DBPlaylistEntry.IMAGE
                ),
                "${DbSettings.DBPlaylistEntry.IMAGE}=?", arrayOf(cursor.getLong(cursorId).toString()), null, null, null
            )


            val images = ArrayList<String>()

            while (imageCursor.moveToNext()) {
                images.add(imageCursor.getString(imageCursor.getColumnIndex(DbSettings.DBPlaylistEntry.IMAGE)))
            }
            imageCursor.close()

            val music = TopTrack(
                cursor.getString(cursorName),
                images,
                cursor.getString(cursorArtist),
                cursor.getString(cursorDuration),
                cursor.getString(cursorUrl),
                cursor.getString(cursorPlaycount)

            )
            music.isInPlaylist = true
            playlist.add(music)

        }*/
        cursor.close()
        database.close()

        return playlist

    }

    fun addPlayList(music: TopTrack) {
        val database: SQLiteDatabase = this.musicDBHelper.writableDatabase

        val listValues = ContentValues()
        //listValues.put(DbSettings.DBToptrackEntry.ID, music.getI)
        listValues.put(DbSettings.DBPlaylistEntry.NAME, music.getName())
       // listValues.put(DbSettings.DBPlaylistEntry.DURATION, music.getDuration())
        listValues.put(DbSettings.DBPlaylistEntry.ARTIST, music.getArtist())
        //listValues.put(DbSettings.DBPlaylistEntry.URL, music.getURL())
       // listValues.put(DbSettings.DBPlaylistEntry.PLAYCOUNT, music.getPlaycount())
        /*
        var listId = database.insertWithOnConflict(
            DbSettings.DBPlaylistEntry.TABLE,
            null,
            listValues,
            SQLiteDatabase.CONFLICT_REPLACE
        )*/
        val newId = database?.insert(DbSettings.DBPlaylistEntry.TABLE, null, listValues)
        database.close()
    }
}