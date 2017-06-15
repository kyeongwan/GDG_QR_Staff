package com.firebaseapp.gdg_korea_campus.staff.view.presenter

import android.content.Context
import com.firebaseapp.gdg_korea_campus.staff.adapter.RSVPAdapterContract
import com.firebaseapp.gdg_korea_campus.staff.data.source.MeetUpRSVPRepository

/**
 * Created by lk on 2017. 6. 15..
 */

interface MeetUpCheckContract {

    interface View {
        fun showDialog(msg: String)
        fun showMemberAnswerAndCheckRSVP(msg: String, _id : Int)
        fun startCam()
    }

    interface Presenter {
        var rsvpData: MeetUpRSVPRepository
        var adapterModel: RSVPAdapterContract.Model
        var adapterView: RSVPAdapterContract.View?
        var view: MeetUpCheckContract.View
        var url : String
        var apiKey: String
        fun loadRSVPList(context: Context, isClear: Boolean)
        fun checkAttend(context: Context, data: String)
        fun checkAttend(context: Context, _id: Int)
    }

}