package com.firebaseapp.gdg_korea_campus.staff.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.firebaseapp.gdg_korea_campus.staff.R
import com.firebaseapp.gdg_korea_campus.staff.data.MeetUpRSVP

/**
 * Created by lk on 2017. 6. 15..
 */
class RSVPListAdapter : RSVPAdapterContract.View, BaseAdapter(), RSVPAdapterContract.Model{

    override fun getView(p: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        }

        val tv = view!!.findViewById(R.id.tv_event_title) as TextView
        tv.text = getItem(p).member.name
        view.setOnClickListener { onClickFunc?.invoke(p) }

        return view
    }

    override fun getItemId(p: Int) = p.toLong()
    private var rsvpList: MutableList<MeetUpRSVP> = ArrayList()
    override var onClickFunc: ((Int) -> Unit)? = null

    override fun getItem(p: Int) = rsvpList[p]
    override fun addItems(items: List<MeetUpRSVP>) {
        rsvpList.clear()
        rsvpList.addAll(items)
    }
    override fun clearItem() = rsvpList.clear()
    override fun notifyAdapter() = notifyDataSetChanged()
    override fun getCount() = rsvpList.size



}