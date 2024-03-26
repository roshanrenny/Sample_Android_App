package com.example.sampleandroidapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.sampleandroidapp.R

class GridAdapter(private val context: Context, private val items: Array<String>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        val holder = view.tag as? ViewHolder ?: ViewHolder(view)
        holder.textView.text = items[position]
        return view
    }

    private class ViewHolder(view: View) {
        val textView: TextView = view.findViewById(R.id.item_text)
    }
}
