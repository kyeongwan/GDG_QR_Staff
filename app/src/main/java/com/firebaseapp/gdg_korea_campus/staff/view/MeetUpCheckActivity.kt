package com.firebaseapp.gdg_korea_campus.staff.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.firebaseapp.gdg_korea_campus.staff.R
import com.firebaseapp.gdg_korea_campus.staff.adapter.RSVPListAdapter
import com.firebaseapp.gdg_korea_campus.staff.data.source.MeetUpRSVPRepository
import com.firebaseapp.gdg_korea_campus.staff.view.presenter.MeetUpCheckContract
import com.firebaseapp.gdg_korea_campus.staff.view.presenter.MeetUpCheckPresenter
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.check_meetup_layout.*

class MeetUpCheckActivity : AppCompatActivity(), MeetUpCheckContract.View {

    private lateinit var presenter: MeetUpCheckPresenter
    private lateinit var rsvpListAdapter: RSVPListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_meetup_layout)

        rsvpListAdapter = RSVPListAdapter()
        lv_checkmeetup_rsvplist.adapter = rsvpListAdapter

        permissionCheck()

        bt_checkmeetup_restartCam.setOnClickListener { IntentIntegrator(this).initiateScan() }

        presenter = MeetUpCheckPresenter().apply {
            view = this@MeetUpCheckActivity
            rsvpData = MeetUpRSVPRepository
            adapterModel = rsvpListAdapter
            adapterView = rsvpListAdapter
            url = intent.getStringExtra("url")
            apiKey = intent.getStringExtra("API")
        }

        presenter.loadRSVPList(this, false)

        et_checkmeetup_answer.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.searchRSVP(applicationContext, s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

    }

    fun permissionCheck(){
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
                }
            }
        }
    }

    override fun startCam() = IntentIntegrator(this).initiateScan()

    override fun showDialog(msg: String) {
        AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, null)
                .show()
    }

    override fun showMemberAnswerAndCheckRSVP(msg: String, _id: Int) {
        AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("확인") { dialog, _ ->
                    presenter.checkAttend(this, _id)
                    dialog.dismiss()
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.d("CheckActivity", "Cancelled scan")
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Log.d("CheckActivity", result.contents)

                val data = decrypt(result.contents, "gdgkrcampus")
                val d = data.split("/")

                presenter.checkAttend(this,data)
            }
        } else {
            Log.d("CheckActivity", "Weird")
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun decrypt(data: String, key: String): String {
        val result = Base64.decode(data, 0)
        var results = ""
        for (i in result.indices) {
            val c = result[i].toInt().let { if (it < 0) it + 256 else it }
            val keychar = key[(i % key.length - 1).let { if (it == -1) key.length - 1 else it }].toInt()
            results += (c - keychar).toChar()
        }
        return results
    }
}
