package com.firebaseapp.gdg_korea_campus.staff.data.source

import android.content.Context
import com.firebaseapp.gdg_korea_campus.staff.data.MeetUpRSVP
import org.json.JSONObject

/**
 * Created by lk on 2017. 6. 14..
 */

object MeetUpRSVPRepository : MeetUpRSVPDataSource{

    private val meetUpRSVPRemoteDataSource = MeetUpRSVPRemoteDataSource

    override fun getRSVPList(context: Context, url: String, loadRSVPListCallback: MeetUpRSVPDataSource.LoadRSVPListCallback?) {
        meetUpRSVPRemoteDataSource.getRSVPList(context, url, object : MeetUpRSVPDataSource.LoadRSVPListCallback {
            override fun onLoadList(list: ArrayList<MeetUpRSVP>) {
                loadRSVPListCallback?.onLoadList(list)
            }

            override fun onFail(error: JSONObject) {
                loadRSVPListCallback?.onFail(error)
            }
        })
    }

    override fun checkAttend(id: Int, url: String, onCheckRSVP: MeetUpRSVPDataSource.CheckRSVPCallback?) {
        meetUpRSVPRemoteDataSource.checkAttend(id, url, object : MeetUpRSVPDataSource.CheckRSVPCallback{
            override fun onCheckDone(result: JSONObject) {
                onCheckRSVP?.onCheckDone(result)
            }

        })
    }
}