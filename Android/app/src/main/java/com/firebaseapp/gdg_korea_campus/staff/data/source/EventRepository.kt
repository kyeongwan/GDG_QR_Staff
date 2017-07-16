package com.firebaseapp.gdg_korea_campus.staff.data.source

import android.content.Context
import com.firebaseapp.gdg_korea_campus.staff.data.EventData
import org.json.JSONObject

/**
 * Created by lk on 2017. 4. 27..
 */

object EventRepository : EventDataSource{

    private val eventRemoteDataSource = EventRemoteDataSource

    override fun getEvents(context: Context, size: Int, loadEventCallback: EventDataSource.LoadEventCallback?) {
        eventRemoteDataSource.getEvents(context, size, object : EventDataSource.LoadEventCallback {
            override fun onLoadEvents(list: ArrayList<EventData>){
                loadEventCallback?.onLoadEvents(list)
            }
        })
    }

    override fun getOpenKey(id: String, sKey: String, loadOpenKeyCallback: EventDataSource.LoadOpenKeyCallback?){
        eventRemoteDataSource.getOpenKey(id, sKey, object : EventDataSource.LoadOpenKeyCallback{
            override fun onLoadOpenKey(result: JSONObject) {
                loadOpenKeyCallback?.onLoadOpenKey(result)
            }
        })
    }
}