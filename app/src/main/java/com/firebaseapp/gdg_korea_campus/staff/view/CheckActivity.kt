package com.firebaseapp.gdg_korea_campus.staff.view

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.firebaseapp.gdg_korea_campus.staff.adapter.EventListAdapter
import com.firebaseapp.gdg_korea_campus.staff.data.source.EventRepository
import com.firebaseapp.gdg_korea_campus.staff.view.presenter.MainContract
import com.firebaseapp.gdg_korea_campus.staff.view.presenter.MainPresenter

import android.widget.EditText
import com.firebaseapp.gdg_korea_campus.staff.R

/**
 * Created by lk on 2017. 4. 27..
 */
class CheckActivity : AppCompatActivity(), MainContract.View {


    private lateinit var presenter: MainPresenter
    lateinit var eventListAdapter: EventListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)

        eventListAdapter = EventListAdapter()
        (findViewById(R.id.lv_main_eventlist) as ListView).adapter = eventListAdapter

        presenter = MainPresenter().apply {
            view = this@CheckActivity
            eventData = EventRepository
            adapterModel = eventListAdapter
            adapterView = eventListAdapter
        }

        presenter.loadItems(this, false)


    }

    override fun showDialog(_id: String) {
        Toast.makeText(applicationContext, _id, 1000).show()
        val ad = AlertDialog.Builder(this@CheckActivity)
        ad.setTitle("비밀키 입력")

        val et = EditText(this@CheckActivity)
        ad.setView(et)

        ad.setPositiveButton("확인") { dialog, which ->
            val value = et.text.toString()
            Log.e("value", value + "")
            presenter.loadOpenKey(this@CheckActivity,_id,value)
            dialog.dismiss()
        }

        ad.setNegativeButton("취소") { dialog, which ->
            dialog.dismiss()
        }
        ad.show()

    }
}