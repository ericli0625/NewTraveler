package com.example.eric.newtraveler.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.eric.newtraveler.R

class ExpandableListViewAdapter(
        private val context: Context,
        private val countyList: List<String>,
        private val cityList: List<List<String>>
) : BaseExpandableListAdapter() {

    override fun getGroup(position: Int): Any {
        return countyList[position]
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
            position: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?
    ): View {
        val view = convertView ?: LayoutInflater
                .from(context).inflate(R.layout.component_county_card, null)

        val textView = view.findViewById<TextView>(R.id.text_title)
        textView.text = countyList[position]
        return view
    }

    override fun getChildrenCount(position: Int): Int {
        if (cityList.isEmpty()) return 0
        return cityList[position].size
    }

    override fun getChild(position: Int, childPosition: Int): Any {
        return cityList[position][childPosition]
    }

    override fun getGroupId(position: Int): Long {
        return position.toLong()
    }

    override fun getChildView(
            position: Int,
            childPosition: Int,
            isExpanded: Boolean,
            convertView: View?,
            parent: ViewGroup?
    ): View {
        val view = convertView ?: LayoutInflater
                .from(context).inflate(R.layout.item_city, null)

        val textView = view.findViewById<TextView>(R.id.text_item)
        textView.text = cityList[position][childPosition]

        return view
    }

    override fun getChildId(position: Int, childPosition: Int): Long {
        return (position * 100 + childPosition).toLong()
    }

    override fun getGroupCount(): Int {
        if (countyList.isEmpty()) return 0
        return countyList.size
    }
}