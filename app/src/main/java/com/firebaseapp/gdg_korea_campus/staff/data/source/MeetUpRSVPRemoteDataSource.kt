package com.firebaseapp.gdg_korea_campus.staff.data.source

import android.content.Context
import android.os.Handler
import android.util.Log
import com.firebaseapp.gdg_korea_campus.staff.data.MeetUpMember
import com.firebaseapp.gdg_korea_campus.staff.data.MeetUpRSVP
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Created by lk on 2017. 6. 14..
 */
object MeetUpRSVPRemoteDataSource : MeetUpRSVPDataSource {

    val handler = Handler()

    override fun checkAttend(id: Int, url: String, onCheckRSVP: MeetUpRSVPDataSource.CheckRSVPCallback?) {
        val t = NetworkThread2(url, id, onCheckRSVP)
        t.start()
    }

    override fun getRSVPList(context: Context, url: String, loadRSVPListCallback: MeetUpRSVPDataSource.LoadRSVPListCallback?) {
        val t = NetworkThread(url, loadRSVPListCallback)
        t.start()
    }

    class NetworkThread(val url : String, val loadRSVPCallback: MeetUpRSVPDataSource.LoadRSVPListCallback?) : Thread() {

        override fun run() {
            super.run()
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()

            val list = ArrayList<MeetUpRSVP>()
            var responseString = ""
            try {
                val response = client.newCall(request).execute()
                responseString = response.body().string().toString()
                Log.e("EventRemoteDataSource", "Type : Load Event List, Result : " + responseString)
                val responseJson = JSONArray(responseString)
                Log.e("Result", responseJson.toString())

                (0..responseJson.length() - 1)
                        .map { responseJson.getJSONObject(it) }
                        .mapTo(list) {
                                    MeetUpRSVP(MeetUpMember(
                                            it.getJSONObject("member").getInt("id"),
                                            it.getJSONObject("member").getString("name")
                                    ),
                                    it.getJSONArray("answers").getJSONObject(0).getString("answer")) }
                Log.e("Result", list.toString())

                handler.post({
                    loadRSVPCallback?.onLoadList(list)
                })
            } catch (e : JSONException){
                e.printStackTrace()
                handler.post ({
                    loadRSVPCallback?.onFail(JSONObject(responseString))
                 })
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    class NetworkThread2(val url: String, val _id: Int, val onCheckRSVP: MeetUpRSVPDataSource.CheckRSVPCallback?) : Thread() {

        override fun run() {
            super.run()
            val client = OkHttpClient()
            val requestBody = FormBody.Builder().add("member", "$_id").add("status","attended").build()
            val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()
            var result = JSONObject()
            try {
                val response = client.newCall(request).execute()
                val responseString = response.body().string().toString()
                Log.e("EventRemoteDataSource", "Type : id, sKey check, Result : " + responseString)
                result = JSONObject(responseString)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            handler.post({
                onCheckRSVP?.onCheckDone(result)
            })
        }
    }
}