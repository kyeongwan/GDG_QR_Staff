package com.firebaseapp.gdg_korea_campus.staff.data.source

import android.content.SharedPreferences
import com.firebaseapp.gdg_korea_campus.staff.Global

/**
 * Created by lk on 13/06/2017.
 */

class PreferenceRepository(val preferences: SharedPreferences) {

    private val preferenceDataSource = PreferenceDataSource

    fun getEventListURL() : String{
        return preferenceDataSource.getEventListURL(preferences).let { if(it.isBlank()) Global.DEFULT_EVENT_LIST_DB_URL else it }
    }

    fun setEventListURL(url: String){
        Global.EVENT_LIST_DB_URL = url
        val editor = preferences.edit()
        preferenceDataSource.setEventListURL(editor, url)
    }
}