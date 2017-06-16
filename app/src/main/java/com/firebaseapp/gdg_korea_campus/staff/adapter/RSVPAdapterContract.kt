package com.firebaseapp.gdg_korea_campus.staff.adapter

import com.firebaseapp.gdg_korea_campus.staff.data.MeetUpRSVP

/**
 * Created by lk on 2017. 6. 15..
 */

interface RSVPAdapterContract {
    interface View {
        var onClickFunc : ((Int) -> Unit)?
        fun notifyAdapter()
    }

    interface Model {
        fun addItems(item: ArrayList<MeetUpRSVP>)
        fun clearItem()
        fun getItem(position: Int): MeetUpRSVP
    }
}