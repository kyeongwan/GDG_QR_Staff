package com.firebaseapp.gdg_korea_campus.staff.view.presenter

import android.content.Context

/**
 * Created by lk on 2017. 4. 27..
 */

interface MainContract {

    interface View {
        fun showBlankDBKey()
        fun showSecurityKeyDialog(_id: String)
        fun showMessage(msg: String)

        fun showProgress()
        fun dismissProgress()
        fun startMeetUpCheck(result: String, api: String)
        fun startCheckQR(result: String)
    }

    interface Presenter {
        fun loadItems(context: Context, isClear: Boolean)
        fun loadOpenKey(context: Context,id: String, sKey: String)
        fun getEventListURL(): String
        fun setEventListURL(url: String)
    }

}