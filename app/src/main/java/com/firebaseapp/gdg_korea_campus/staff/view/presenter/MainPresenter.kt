package com.firebaseapp.gdg_korea_campus.staff.view.presenter

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.firebaseapp.gdg_korea_campus.staff.adapter.EventAdapterContract
import com.firebaseapp.gdg_korea_campus.staff.data.EventData
import com.firebaseapp.gdg_korea_campus.staff.data.source.EventDataSource
import com.firebaseapp.gdg_korea_campus.staff.data.source.EventRepository
import com.firebaseapp.gdg_korea_campus.staff.view.MainActivity
import org.json.JSONObject

/**
 * Created by lk on 2017. 4. 27..
 */

class MainPresenter : MainContract.Presenter {

    lateinit override var view: MainContract.View
    lateinit override var eventData: EventRepository

    lateinit override var adapterModel: EventAdapterContract.Model
    override var adapterView: EventAdapterContract.View? = null
        set(value) {
            field = value
            field?.onClickFunc = { onClickListener(it) }
        }

    override fun loadItems(context: Context, isClear: Boolean) {
        val mProgressDialog = ProgressDialog.show(context,"", "잠시만 기다려 주세요.",true)
        eventData.getEvents(context, 10, object : EventDataSource.LoadEventCallback {
            override fun onLoadEvents(list: ArrayList<EventData>) {
                if (isClear) {
                    adapterModel.clearItem()
                }

                adapterModel.addItems(list)
                adapterView?.notifyAdapter()
                mProgressDialog.dismiss()
            }
        })
    }

    override fun loadOpenKey(context: Context, id:String, sKey: String) {
        val mProgressDialog = ProgressDialog.show(context,"", "잠시만 기다려 주세요.",true)
        eventData.getOpenKey(id, sKey, object : EventDataSource.LoadOpenKeyCallback{
            override fun onLoadOpenKey(result: JSONObject) {
                Log.e("onLoadOpenKey", "result = $result")
                mProgressDialog.dismiss()
                if(result.getString("result").equals("Fail")){
                    Toast.makeText(context, "비밀키가 다릅니다.", 1000).show()
                }else {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("url", result.getString("result"))
                    context.startActivity(intent)
                }
            }
        })
    }

    private fun onClickListener(position: Int) {
        adapterModel.getItem(position).let {
            view.showDialog(it._id)
        }
    }
}