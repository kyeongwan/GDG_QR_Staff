package com.firebaseapp.gdg_korea_campus.staff.view.presenter

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.firebaseapp.gdg_korea_campus.staff.adapter.RSVPAdapterContract
import com.firebaseapp.gdg_korea_campus.staff.data.MeetUpRSVP
import com.firebaseapp.gdg_korea_campus.staff.data.source.MeetUpRSVPDataSource
import com.firebaseapp.gdg_korea_campus.staff.data.source.MeetUpRSVPRepository
import org.json.JSONObject

/**
 * Created by lk on 2017. 6. 15..
 */

class MeetUpCheckPresenter : MeetUpCheckContract.Presenter {

    lateinit override var view: MeetUpCheckContract.View
    lateinit override var rsvpData: MeetUpRSVPRepository
    var rsvpList = ArrayList<MeetUpRSVP>()
    lateinit override var url : String
    lateinit override var apiKey : String

    lateinit override var adapterModel: RSVPAdapterContract.Model
    override var adapterView: RSVPAdapterContract.View? = null
        set(value) {
            field = value
            field?.onClickFunc = { onClickListener(it) }
        }

    override fun loadRSVPList(context: Context, isClear: Boolean) {

        val mProgressDialog = ProgressDialog.show(context, "", "잠시만 기다려 주세요.", true)
        rsvpData.getRSVPList(context, "${url}rsvps?key=${apiKey}&fields=answers&response=yes", object : MeetUpRSVPDataSource.LoadRSVPListCallback {
            override fun onLoadList(list: ArrayList<MeetUpRSVP>) {
                if (isClear) {
                    adapterModel.clearItem()
                }

                rsvpList = list
                adapterModel.addItems(list)
                adapterView?.notifyAdapter()
                mProgressDialog.dismiss()

                if (list.size == 0)
                    view.showDialog("DB가 비었거나 문제가 발생했습니다")
                else
                    view.startCam()
            }

            override fun onFail(error: JSONObject) {
                Log.e("onFail", "onFail")
                mProgressDialog.dismiss()
                view.showDialog(error.toString())
            }
        })
    }

    override fun checkAttend(context: Context, data: String) {
        val mProgressDialog = ProgressDialog.show(context, "", "잠시만 기다려 주세요.", true)
        val email = data.split("/")[1]
        val member = rsvpList.filter { it.answer.contains(email) }.firstOrNull()

        Log.e("MeetUpCheckPresenter", "email: $email / member: $member , List : $rsvpList")
        mProgressDialog.dismiss()
        if(member == null){
            view.showDialog("등록하지 않은 회원입니다.")
            return
        }

        checkAttend(context, member.member._id)

    }

    override fun checkAttend(context: Context, _id: Int){
        val mProgressDialog = ProgressDialog.show(context, "", "잠시만 기다려 주세요.", true)
        rsvpData.checkAttend(_id, "${url}attendance?key=${apiKey}", object : MeetUpRSVPDataSource.CheckRSVPCallback{
            override fun onCheckDone(result: JSONObject) {
                if(result.has("message")){
                    if(result.getString("message").equals("attendance updated")) {
                        mProgressDialog.dismiss()
                        Toast.makeText(context, "확인되었습니다", Toast.LENGTH_SHORT).show()
                        view.startCam()
                        return
                    }
                }
                mProgressDialog.dismiss()
                view.showDialog(result.toString())
            }

        })
    }

    private fun onClickListener(position: Int) {
        adapterModel.getItem(position).let {
            view.showMemberAnswerAndCheckRSVP(it.answer, it.member._id)
        }
    }
}