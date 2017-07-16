package com.firebaseapp.gdg_korea_campus.staff.adapter

import com.firebaseapp.gdg_korea_campus.staff.data.EventData

/**
 * Created by lk on 2017. 4. 27..
 */
interface EventAdapterContract {
    interface View {
        var onClickFunc : ((Int) -> Unit)?
        fun notifyAdapter()
    }

    interface Model {
        fun addItems(item: ArrayList<EventData>)
        fun clearItem()
        fun getItem(position: Int): EventData
    }
}