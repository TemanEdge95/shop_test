package com.test.teman.shoptest.model

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.teman.shoptest.R
import com.test.teman.shoptest.fragments.ShopFragment
import kotlinx.android.synthetic.main.product_item.view.*
import com.test.teman.shoptest.MainActivity
import com.test.teman.shoptest.fragments.ProductFragment

private val productFragment: ProductFragment = ProductFragment()

class ProductsAdapter(private val dataSet: ArrayList<Product>,
                      val context: Context?) :
        RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    override fun getItemCount() = dataSet.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textViewName = view.textViewName!!
        val textViewVersion = view.textViewVersion!!
        val imageViewProduct = view.imageViewProduct!!
        val viewItem = view.viewItem!!
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val shopFragment = ShopFragment()
        return if (shopFragment.isSheetChecked()) ViewHolder(LayoutInflater.from(context).inflate(R.layout.product_item_large,
                parent, false))
            else ViewHolder(LayoutInflater.from(context).inflate(R.layout.product_item,
                parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewName.text = dataSet[position].productName
        holder.textViewVersion.text = dataSet[position].productVersion
        holder.imageViewProduct.setImageResource(dataSet[position].productImage)
        holder.viewItem.setOnClickListener {
            productFragment.setInfo(dataSet[position].productName, dataSet[position].productVersion, dataSet[position].productImage)
            (context as MainActivity).setFragment("fragmentAdapter")
        }
    }
}