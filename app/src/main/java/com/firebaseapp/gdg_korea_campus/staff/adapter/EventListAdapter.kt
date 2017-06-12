package com.firebaseapp.gdg_korea_campus.staff.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.firebaseapp.gdg_korea_campus.staff.R
import com.firebaseapp.gdg_korea_campus.staff.data.EventData

/**
 * Created by lk on 2017. 4. 27..
 */

class EventListAdapter : EventAdapterContract.View, BaseAdapter(), EventAdapterContract.Model{
    override fun getView(p: Int,convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        }

        val tv = view!!.findViewById(R.id.tv_event_title) as TextView
        tv.text = getItem(p).title
        view.setOnClickListener { onClickFunc?.invoke(p) }

        return view
    }

    override fun getItemId(p: Int) = p.toLong()
    private var eventList: ArrayList<EventData> = ArrayList()
    override var onClickFunc: ((Int) -> Unit)? = null

    override fun getItem(p: Int) = eventList[p]
    override fun addItems(items: ArrayList<EventData>) {eventList = items}
    override fun clearItem() = eventList.clear()
    override fun notifyAdapter() = notifyDataSetChanged()
    override fun getCount() = eventList.size



}