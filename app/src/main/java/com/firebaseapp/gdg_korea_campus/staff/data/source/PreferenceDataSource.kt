package com.firebaseapp.gdg_korea_campus.staff.data.source

import android.content.SharedPreferences

/**
 * Created by lk on 13/06/2017.
 */

object PreferenceDataSource {

    fun getAPIKey(preferences: SharedPreferences) = preferences.getString("Meetup_API", "")

    fun setAPIKey(editor: SharedPreferences.Editor, key: String) {
        editor.putString("Meetup_API", key)
        editor.commit()
    }

}