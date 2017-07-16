package com.firebaseapp.gdg_korea_campus.staff.data.source

import android.content.Context
import com.firebaseapp.gdg_korea_campus.staff.data.MeetUpRSVP
import org.json.JSONObject

/**
 * Created by lk on 2017. 6. 14..
 */

interface MeetUpRSVPDataSource {

    interface LoadRSVPListCallback {
        fun onLoadList(list: ArrayList<MeetUpRSVP>)
        fun onFail(error: JSONObject)
    }

    interface CheckRSVPCallback {
        fun onCheckDone(result: JSONObject)
    }

    fun getRSVPList(context: Context, url: String, loadRSVPListCallback: LoadRSVPListCallback?)
    fun checkAttend(id:Int, url:String, onCheckRSVP: CheckRSVPCallback?)
}