package com.firebaseapp.gdg_korea_campus.staff.view.presenter

import android.content.Context
import com.firebaseapp.gdg_korea_campus.staff.adapter.EventAdapterContract
import com.firebaseapp.gdg_korea_campus.staff.data.source.EventRepository

/**
 * Created by lk on 2017. 4. 27..
 */

interface MainContract {

    interface View {
        fun showDialog(_id: String)
    }

    interface Presenter {

        var eventData: EventRepository
        var adapterModel: EventAdapterContract.Model
        var adapterView: EventAdapterContract.View?
        var view: MainContract.View
        fun loadItems(context: Context, isClear: Boolean)
        fun loadOpenKey(context: Context,id: String, sKey: String)
    }

}