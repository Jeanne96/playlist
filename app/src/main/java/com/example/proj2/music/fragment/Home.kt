package com.example.proj2.music.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.proj2.music.R
import com.example.proj2.music.Toptrack

import kotlinx.android.synthetic.main.fragment_home.*

@SuppressLint("ValidFragment")
class Home (context: Context): Fragment() {
    private var parentContext = context
    private var initialized: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (!this.initialized) {
            val fm = fragmentManager
            val ft = fm?.beginTransaction()
            ft?.add(R.id.list_holder, Toptrack(this.parentContext), "NEW_FRAG") //frag 1: load top track
            ft?.commit()
            //search artist
            search_edit_text.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchText = search_edit_text.text
                    search_edit_text.setText("")
                    if (searchText.toString() == "") {
                        val toast = Toast.makeText(this.parentContext, "Please enter text", Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                        return@setOnEditorActionListener true
                    }
                    else {
                        performSearch(searchText.toString())
                        return@setOnEditorActionListener false
                    }
                }

                return@setOnEditorActionListener false
            }

            this.initialized = true
        }
    }

    private fun performSearch(query: String) {
        // Load Fragment into View
        val fm = fragmentManager

        // add
        val fragment = ResultList(this.parentContext, query) //frag2 : load search result
        val ft = fm?.beginTransaction()
        ft?.replace(R.id.list_holder, fragment, "RESULTS_FRAG")
        ft?.commit()
    }
}