package com.test.teman.shoptest.model

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.teman.shoptest.R
import kotlinx.android.synthetic.main.image_item.view.*

class ImagesAdapter(private val dataSet: IntArray,
                    val context: Context?) :
        RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    override fun getItemCount() = dataSet.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageViewInfoItem = view.imageViewInfoItem!!
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_item,
                parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageViewInfoItem.setImageResource(dataSet[position])
    }
}