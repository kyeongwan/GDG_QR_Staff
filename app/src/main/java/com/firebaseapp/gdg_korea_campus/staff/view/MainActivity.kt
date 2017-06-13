package com.firebaseapp.gdg_korea_campus.staff.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import com.firebaseapp.gdg_korea_campus.staff.adapter.EventListAdapter
import com.firebaseapp.gdg_korea_campus.staff.data.source.EventRepository
import com.firebaseapp.gdg_korea_campus.staff.view.presenter.MainContract
import com.firebaseapp.gdg_korea_campus.staff.view.presenter.MainPresenter

import android.widget.EditText
import com.firebaseapp.gdg_korea_campus.staff.R
import com.firebaseapp.gdg_korea_campus.staff.data.source.PreferenceRepository

/**
 * Created by lk on 2017. 4. 27..
 */
class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainPresenter
    private lateinit var toolbar: Toolbar
    lateinit var eventListAdapter: EventListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = (findViewById(R.id.toolbar) as Toolbar)
        setSupportActionBar(toolbar)

        eventListAdapter = EventListAdapter()
        (findViewById(R.id.lv_main_eventlist) as ListView).adapter = eventListAdapter

        val preferences = getSharedPreferences("pref", MODE_PRIVATE)

        presenter = MainPresenter().apply {
            view = this@MainActivity
            eventData = EventRepository
            preferenceData = PreferenceRepository(preferences)
            adapterModel = eventListAdapter
            adapterView = eventListAdapter
        }

        presenter.loadItems(this, false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == R.id.action_settings){
            presenter.showAPIKeySetDialog(this@MainActivity)
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    override fun showSecuritKeyDialog(_id: String) {
        Toast.makeText(applicationContext, _id, 1000).show()
        val ad = AlertDialog.Builder(this@MainActivity)
        ad.setTitle("비밀키 입력")

        val et = EditText(this@MainActivity)
        ad.setView(et)

        ad.setPositiveButton("확인") { dialog, which ->
            val value = et.text.toString()
            Log.e("value", value + "")
            presenter.loadOpenKey(this@MainActivity,_id,value)
            dialog.dismiss()
        }

        ad.setNegativeButton("취소") { dialog, which ->
            dialog.dismiss()
        }
        ad.show()
    }
}