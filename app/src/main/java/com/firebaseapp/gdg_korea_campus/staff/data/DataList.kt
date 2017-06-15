package com.firebaseapp.gdg_korea_campus.staff.data

/**
 * Created by lk on 2017. 4. 27..
 */
data class EventData(val _id: String, val title: String, val openKey: String, val sKey: String)
data class MeetUpRSVP(val member: MeetUpMember, val answer: String)
data class MeetUpMember(val _id: Int, val name: String)
