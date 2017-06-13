package com.firebaseapp.gdg_korea_campus.staff.data.source

import android.content.Context
import android.os.Handler
import android.util.Log
import com.firebaseapp.gdg_korea_campus.staff.Global
import com.firebaseapp.gdg_korea_campus.staff.data.EventData
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Created by lk on 2017. 4. 27..
 */

object EventRemoteDataSource : EventDataSource {

    val handler = Handler()
    override fun getEvents(context: Context, size: Int, loadEventCallback: EventDataSource.LoadEventCallback?) {
        val t = NetworkThread(loadEventCallback)
        t.start()
    }

    override fun getOpenKey(id: String, sKey: String, loadOpenKeyCallback: EventDataSource.LoadOpenKeyCallback?) {
        val t = NetworkThread2(id, sKey, loadOpenKeyCallback)
        t.start()
    }

    class NetworkThread(val loadEventCallback: EventDataSource.LoadEventCallback?) : Thread() {

        override fun run() {
            super.run()
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(Global.EVENT_LIST_DB_URL)
                    .get()
                    .build()

            val list = ArrayList<EventData>()
            try {
                val response = client.newCall(request).execute()
                val responseString = response.body().string().toString()
                Log.e("aa", responseString)
                val responseJson = JSONArray(responseString)
                Log.e("Result", responseJson.toString())

                (0..responseJson.length() - 1)
                        .map { responseJson.getJSONObject(it) }
                        .mapTo(list) { EventData(it.getString("_id"), it.getString("Title"), "", "") }
                Log.e("Result", list.toString())

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e : JSONException){
                e.printStackTrace()
            }

            handler.post({
                Log.e("EventDataSource", "CallBack")
                loadEventCallback?.onLoadEvents(list)
            })
        }
    }

    class NetworkThread2(val _id: String, val sKey: String, val loadOpenKeyCallback: EventDataSource.LoadOpenKeyCallback?) : Thread() {

        override fun run() {
            super.run()
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url("${Global.EVENT_LIST_DB_URL}?id=$_id&sKey=$sKey")
                    .get()
                    .build()
            var result = JSONObject()
            try {
                val response = client.newCall(request).execute()
                val responseString = response.body().string().toString()
                Log.e("aa", responseString)
                result = JSONObject(responseString)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            handler.post({
                loadOpenKeyCallback?.onLoadOpenKey(result)
            })
        }
    }
}