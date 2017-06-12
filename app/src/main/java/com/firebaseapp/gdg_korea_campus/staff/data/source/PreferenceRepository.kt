package com.firebaseapp.gdg_korea_campus.staff.data.source

import android.content.SharedPreferences

/**
 * Created by lk on 13/06/2017.
 */

class PreferenceRepository(val preferences: SharedPreferences) {

    private val preferenceDataSource = PreferenceDataSource

    fun getAPIKey() : String{
        return preferenceDataSource.getAPIKey(preferences)
    }

    fun setAPIKey(key: String){
        val editor = preferences.edit()
        preferenceDataSource.setAPIKey(editor, key)
    }
}