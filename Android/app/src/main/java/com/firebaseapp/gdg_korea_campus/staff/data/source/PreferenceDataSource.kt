package com.firebaseapp.gdg_korea_campus.staff.data.source

import android.content.SharedPreferences

/**
 * Created by lk on 13/06/2017.
 */

object PreferenceDataSource {

    fun getEventListURL(preferences: SharedPreferences) = preferences.getString("EventListURL", "")

    fun setEventListURL(editor: SharedPreferences.Editor, url: String) {
        editor.putString("EventListURL", url)
        editor.commit()
    }

}