package com.firebaseapp.gdg_korea_campus.staff.data.source

import android.content.Context
import com.firebaseapp.gdg_korea_campus.staff.data.EventData
import org.json.JSONObject

/**
 * Created by lk on 2017. 4. 27..
 */

interface EventDataSource {

    interface LoadEventCallback {
        fun onLoadEvents(list: ArrayList<EventData>)
    }

    interface LoadOpenKeyCallback{
        fun onLoadOpenKey(result: JSONObject)
    }

    fun getEvents(context: Context, size: Int, loadEventCallback: LoadEventCallback?)
    fun getOpenKey(id:String, sKey:String, onLoadOpenKey : LoadOpenKeyCallback?)
}