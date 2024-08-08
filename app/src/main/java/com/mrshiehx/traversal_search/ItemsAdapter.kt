package com.mrshiehx.traversal_search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

class ItemsAdapter(
    private val context: Context,
    private val items: List<Item>,
    private val isInDetailsInterface: Boolean
) : BaseAdapter() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount() = items.size

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertViewProvided: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        var convertView: View? = convertViewProvided

        if (convertView == null) {
            holder = ViewHolder()
            convertView = mInflater.inflate(R.layout.item, null)!!

            with(holder) {
                titleLayout = convertView.findViewById(R.id.titleLayout)
                titleTextView = convertView.findViewById(R.id.title)
                totalCountTextView = convertView.findViewById(R.id.totalCount)

                lineLayout1 = convertView.findViewById(R.id.lineLayout1)
                lineCount1TextView = convertView.findViewById(R.id.lineCount1)
                line1TextView = convertView.findViewById(R.id.line1)

                lineLayout2 = convertView.findViewById(R.id.lineLayout2)
                lineCount2TextView = convertView.findViewById(R.id.lineCount2)
                line2TextView = convertView.findViewById(R.id.line2)

                lineLayout3 = convertView.findViewById(R.id.lineLayout3)
                lineCount3TextView = convertView.findViewById(R.id.lineCount3)
                line3TextView = convertView.findViewById(R.id.line3)
            }
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        if (!isInDetailsInterface) {
            val item = getItem(position)
            holder.titleLayout.visibility = View.VISIBLE
            holder.titleTextView.text = item.fileName
            if (item.founds.size == 1) {
                holder.totalCountTextView.visibility = View.GONE
            } else {
                holder.totalCountTextView.visibility = View.VISIBLE
                holder.totalCountTextView.text = "共${item.founds.size}个>>"
            }
            val entries = item.founds[0].toSortedMap { a, b -> a.compareTo(b) }.entries.toList()
            if (entries.size >= 1) {
                holder.lineLayout1.visibility = View.VISIBLE
                holder.lineCount1TextView.text = entries[0].key.toString()
                holder.line1TextView.text = entries[0].value
            } else {
                holder.lineLayout1.visibility = View.GONE
            }

            if (entries.size >= 2) {
                holder.lineLayout2.visibility = View.VISIBLE
                holder.lineCount2TextView.text = entries[1].key.toString()
                holder.line2TextView.text = entries[1].value
            } else {
                holder.lineLayout2.visibility = View.GONE
            }

            if (entries.size >= 3) {
                holder.lineLayout3.visibility = View.VISIBLE
                holder.lineCount3TextView.text = entries[2].key.toString()
                holder.line3TextView.text = entries[2].value
            } else {
                holder.lineLayout3.visibility = View.GONE
            }
        } else {
            val item = getItem(0)
            holder.titleLayout.visibility = View.GONE

            val entries = item.founds[position].entries.toList()

            if (entries.size >= 1) {
                holder.lineLayout1.visibility = View.VISIBLE
                holder.lineCount1TextView.text = entries[0].key.toString()
                holder.line1TextView.text = entries[0].value
            } else {
                holder.lineLayout1.visibility = View.GONE
            }

            if (entries.size >= 2) {
                holder.lineLayout2.visibility = View.VISIBLE
                holder.lineCount2TextView.text = entries[1].key.toString()
                holder.line2TextView.text = entries[1].value
            } else {
                holder.lineLayout2.visibility = View.GONE
            }

            if (entries.size >= 3) {
                holder.lineLayout3.visibility = View.VISIBLE
                holder.lineCount3TextView.text = entries[2].key.toString()
                holder.line3TextView.text = entries[2].value
            } else {
                holder.lineLayout3.visibility = View.GONE
            }
        }
        return convertView
    }

    class ViewHolder {
        lateinit var titleLayout: LinearLayout
        lateinit var titleTextView: TextView
        lateinit var totalCountTextView: TextView

        lateinit var lineLayout1: LinearLayout
        lateinit var lineCount1TextView: TextView
        lateinit var line1TextView: TextView

        lateinit var lineLayout2: LinearLayout
        lateinit var lineCount2TextView: TextView
        lateinit var line2TextView: TextView

        lateinit var lineLayout3: LinearLayout
        lateinit var lineCount3TextView: TextView
        lateinit var line3TextView: TextView
    }
}