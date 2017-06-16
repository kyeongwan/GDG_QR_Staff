package com.firebaseapp.gdg_korea_campus.staff.view.presenter

import android.content.Context
import android.util.Log
import com.firebaseapp.gdg_korea_campus.staff.Global
import com.firebaseapp.gdg_korea_campus.staff.adapter.EventAdapterContract
import com.firebaseapp.gdg_korea_campus.staff.data.EventData
import com.firebaseapp.gdg_korea_campus.staff.data.source.EventDataSource
import com.firebaseapp.gdg_korea_campus.staff.data.source.PreferenceRepository
import org.json.JSONObject

/**
 * Created by lk on 2017. 4. 27..
 */

class MainPresenter : MainContract.Presenter {

    lateinit var view: MainContract.View
    lateinit var eventData: EventDataSource
    lateinit var preferenceData: PreferenceRepository

    lateinit var adapterModel: EventAdapterContract.Model

    var adapterView: EventAdapterContract.View? = null
        set(value) {
            field = value
            field?.onClickFunc = { onClickListener(it) }
        }

    override fun loadItems(context: Context, isClear: Boolean) {
        if (Global.EVENT_LIST_DB_URL.isBlank()) {
            Global.EVENT_LIST_DB_URL = preferenceData.getEventListURL()
            Log.e("Presenter", "EventListDB Blank")
        }

        view.showProgress()
        eventData.getEvents(context, 10, object : EventDataSource.LoadEventCallback {
            override fun onLoadEvents(list: ArrayList<EventData>) {
                if (isClear) {
                    adapterModel.clearItem()
                }

                adapterModel.addItems(list)
                adapterView?.notifyAdapter()

                view.dismissProgress()

                if (list.size == 0)
                    view.showBlankDBKey()
            }
        })
    }

    override fun loadOpenKey(context: Context, id: String, sKey: String) {
        if (sKey.isBlank()) {
            view.showMessage("비밀키를 입력해주세요")
            return
        }

        view.showProgress()
        eventData.getOpenKey(id, sKey, object : EventDataSource.LoadOpenKeyCallback {
            override fun onLoadOpenKey(result: JSONObject) {

                Log.e("onLoadOpenKey", "result = $result")

                view.dismissProgress()

                if (result.getString("result") == "Fail") {
                    view.showMessage("비밀키가 다릅니다.")
                    return
                }

                if (result.getString("type") == "meetup") {
                    view.startMeetUpCheck(result.getString("result"), result.getString("API"))
                } else {
                    view.startCheckQR(result.getString("result"))
                }
            }
        })
    }

    private fun onClickListener(position: Int) {
        adapterModel.getItem(position).let {
            view.showSecurityKeyDialog(it._id)
        }
    }

    override fun setEventListURL(url: String) {
        preferenceData.setEventListURL(url)
    }

    override fun getEventListURL() = preferenceData.getEventListURL()
}