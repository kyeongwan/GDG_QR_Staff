package com.firebaseapp.gdg_korea_campus.staff.view

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.firebaseapp.gdg_korea_campus.staff.R
import com.google.zxing.integration.android.IntentIntegrator

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Integer.parseInt

class CheckActivity : AppCompatActivity() {

    lateinit var url: String
    lateinit var mProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        url = intent.getStringExtra("url")


        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
                }
            }
        } else {
            IntentIntegrator(this).initiateScan()
        }


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
                val thread = NetworkThread(d[0], d[1])
                thread.start()
                mProgressDialog = ProgressDialog.show(this@CheckActivity, "", "잠시만 기다려 주세요.", true)

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


    internal inner class NetworkThread(val row: String, val mail: String) : Thread() {

        override fun run() {
            val client = OkHttpClient()
            val jsonObject = JSONObject()
            try {
                jsonObject.put("row", parseInt(row))
                jsonObject.put("mail", mail)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val body1 = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("row", "${parseInt(row) + 1}")
                    .addFormDataPart("mail", mail)
                    .build()
            val request = Request.Builder()
                    .url(url)
                    .post(body1)
                    .build()
            try {
                val response = client.newCall(request).execute()
                val responseString = response.body().string().toString()
                Log.e("Result", responseString)
                runOnUiThread {
                    mProgressDialog.dismiss()
                    if (responseString == "{\"result\":\"success\"}") {
                        val alert = AlertDialog.Builder(this@CheckActivity)
                        alert.setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        alert.setMessage("확인 되었습니다")
                        alert.setOnDismissListener {
                            IntentIntegrator(this@CheckActivity).initiateScan()
                        }
                        alert.show()
                    } else {
                        val alert = AlertDialog.Builder(this@CheckActivity)
                        alert.setPositiveButton("OK") { dialog, which ->
                            dialog.dismiss()
                        }
                        alert.setMessage("확인되지 않은 사용자입니다.")
                        alert.show()
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


}
