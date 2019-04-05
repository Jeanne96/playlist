package com.example.proj2.music.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.example.proj2.music.R
import com.example.proj2.music.ViewModel.MusicViewModel
import com.example.proj2.music.model.TopTrack

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_music_detail.*
import kotlinx.android.synthetic.main.result_list_item.*

class MusicDetailActivity: AppCompatActivity() {
    private lateinit var music: TopTrack
    private lateinit var viewModel: MusicViewModel

    //private lateinit var adapter: FeatureAdapter
    //private var features = ArrayList<Feature>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_detail)

        music = intent.extras!!.getSerializable("music") as TopTrack
        //Log.e("FAVORITE", product.isFavorite.toString())

        viewModel = ViewModelProviders.of(this).get(MusicViewModel::class.java)

        this.loadUI(music)
    }

    override fun onBackPressed() {
        this.finish()
    }


    private fun loadUI(music: TopTrack) {

        detail_music_title.text = "title: " + music.getName()
        detail_music_artist.text = "artist: " + music.getArtist()
        detail_music_duration.text = "duration: " + music.getDuration()
        detail_music_url.text = "url: " + music.getURL()
        detail_music_playcount.text = "playcount: " + music.getPlaycount()

        add_playlist.setOnClickListener {
            // do
            //music.isInPlaylist = true
            viewModel.addPlayList(this.music)
            this.music.isInPlaylist = true

        }
        //music_artist.text = music.getArtist()


        val images = music.getImage()
        if (images.size > 0) {
            Picasso.with(this).load(music.getImage()[3]).into(detail_music_img)
        } else {
            // eventually show image not available pic
        }


    }
}
