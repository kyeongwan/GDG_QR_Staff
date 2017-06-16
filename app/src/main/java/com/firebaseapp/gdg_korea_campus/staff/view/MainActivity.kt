package com.firebaseapp.gdg_korea_campus.staff.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.firebaseapp.gdg_korea_campus.staff.R
import com.firebaseapp.gdg_korea_campus.staff.adapter.EventListAdapter
import com.firebaseapp.gdg_korea_campus.staff.data.source.EventRepository
import com.firebaseapp.gdg_korea_campus.staff.data.source.PreferenceRepository
import com.firebaseapp.gdg_korea_campus.staff.view.presenter.MainContract
import com.firebaseapp.gdg_korea_campus.staff.view.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

/**
 * Created by lk on 2017. 4. 27..
 */
class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    lateinit var eventListAdapter: EventListAdapter

    private val progressDlg by lazy {
        ProgressDialog(this).apply {
            setMessage("잠시만 기다려 주세요.")
            setCancelable(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        eventListAdapter = EventListAdapter()
        lv_main_eventlist.adapter = eventListAdapter

        presenter = MainPresenter().apply {
            view = this@MainActivity
            eventData = EventRepository
            preferenceData = PreferenceRepository(getSharedPreferences("pref", MODE_PRIVATE))
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
        if (id == R.id.action_settings) {
            showAPIKeySetDialog()
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    override fun showBlankDBKey() {
        AlertDialog.Builder(this)
                .setMessage("DB가 비어있거나 URL이 잘못되었습니다.")
                .setPositiveButton(android.R.string.ok, null)
                .show()
    }

    override fun showSecurityKeyDialog(_id: String) {
//        Toast.makeText(applicationContext, _id, Toast.LENGTH_SHORT).show()

        AlertDialog.Builder(this@MainActivity).apply {
            setTitle("비밀키 입력")

            val et = EditText(this@MainActivity)
            setView(et)

            setPositiveButton("확인") { _, _ ->
                val value = et.text.toString()
                Log.e("SecurityKeyDialog", value + "")
                presenter.loadOpenKey(this@MainActivity, _id, value)
            }

            setNegativeButton("취소", null)
        }.show()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        if (progressDlg.isShowing) {
            dismissProgress()
        }
        progressDlg.show()
    }

    override fun dismissProgress() {
        progressDlg.hide()
    }

    fun showAPIKeySetDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("이벤트 리스트 URL 수정")

            val et = EditText(this@MainActivity)
            et.setText(presenter.getEventListURL())

            setView(et)

            setPositiveButton("확인") { _, _ ->
                val value = et.text.toString()
                Log.e("value", value + "")
                presenter.setEventListURL(value)
                presenter.loadItems(this@MainActivity, true)
            }

            setNegativeButton("취소", null)
        }.show()
    }

    override fun startMeetUpCheck(result: String, api: String) {
        startActivity(
                Intent(this, MeetUpCheckActivity::class.java).apply {
                    putExtra("url", result)
                    putExtra("API", api)
                })
    }

    override fun startCheckQR(result: String) {
        startActivity(
                Intent(this, CheckActivity::class.java).run {
                    putExtra("url", result)
                })
    }
}