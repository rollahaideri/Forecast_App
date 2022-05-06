package com.example.forecast_app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

internal class CustomAdapter(private var itemsList: ArrayList<String>) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>(){

    // Init - Extend interface
    private lateinit var mListener : OnItemClickListener
    var itemFilterList = ArrayList<String>()



    // ???
    init {
        itemFilterList = itemsList
    }

    // Create interface
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    // Method Definition
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener


    }
        internal inner class MyViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
            var itemTextView: TextView = view.findViewById(R.id.itemText)

            init {
                itemView.setOnClickListener{
                    listener.onItemClick(adapterPosition)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemFilterList[position]
        holder.itemTextView.text = item
    }

    override fun getItemCount(): Int {
        // return itemsList.size
        return itemFilterList.size
    }

    fun getFilter() : Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemFilterList = itemsList
                } else {
                    val resultList = ArrayList<String>()
                    for (row in itemsList) {
                        if (row.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    itemFilterList = resultList
                }

                // Is a must - For filtering
                val filterResults = FilterResults()
                filterResults.values = itemFilterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) {
                    itemFilterList = results.values as ArrayList<String>
                    notifyDataSetChanged()
                }
            }
        }
    }

    }







