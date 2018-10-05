package com.test.teman.shoptest.model

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.teman.shoptest.R
import com.test.teman.shoptest.fragments.CartFragment
import kotlinx.android.synthetic.main.cart_item_large.view.*

private val cartFragment: CartFragment = CartFragment()

class CartAdapter(private val dataSet: ArrayList<Product>,
                      val context: Context?) :
        RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun getItemCount() = dataSet.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textViewNameCart = view.textViewNameCart!!
        val textViewVersionCart = view.textViewVersionCart!!
        val imageViewCart = view.imageViewCart!!
        val buttonDelete = view.buttonDelete!!
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item_large,
                parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewNameCart.text = dataSet[position].productName
        holder.textViewVersionCart.text = dataSet[position].productVersion
        holder.imageViewCart.setImageResource(dataSet[position].productImage)
        holder.buttonDelete.setOnClickListener {
            cartFragment.removeItem(position)
        }
    }
}