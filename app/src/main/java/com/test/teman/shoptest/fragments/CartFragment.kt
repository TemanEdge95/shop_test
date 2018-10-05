package com.test.teman.shoptest.fragments

import android.content.Context
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.test.teman.shoptest.R
import com.test.teman.shoptest.model.CartAdapter
import com.test.teman.shoptest.model.Product

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var resultList: ArrayList<Product> = ArrayList<Product>()

private lateinit var rv: RecyclerView
private lateinit var textViewTotal: TextView
private lateinit var textViewEmpty: TextView
private lateinit var totalView: View

class CartFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewFinal = inflater.inflate(R.layout.fragment_cart, container, false)

        textViewEmpty = viewFinal.findViewById<TextView>(R.id.textViewEmpty)
        textViewEmpty.visibility = if (resultList.isEmpty())  View.VISIBLE else View.GONE

        totalView = viewFinal.findViewById<View>(R.id.constraintLayoutInfo)
        totalView.visibility = if (!resultList.isEmpty())  View.VISIBLE else View.GONE

        textViewTotal = viewFinal.findViewById<TextView>(R.id.textViewTotal)
        textViewTotal.text = "Total: " + getTotalAmount()

        rv = viewFinal.findViewById<RecyclerView>(R.id.recyclerViewCart).apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 1)
            adapter = CartAdapter(resultList, this.context)
        }

        return viewFinal
    }

    fun removeItem(position: Int) {
        resultList.removeAt(position)
        rv.adapter!!.notifyItemRemoved(position)
        rv.adapter!!.notifyItemRangeChanged(position, resultList.size)

        textViewTotal.text = "Total: " + getTotalAmount()

        checkVisibility()
    }

    fun checkVisibility() {
        textViewEmpty.visibility = if (resultList.isEmpty())  View.VISIBLE else View.GONE
        totalView.visibility = if (!resultList.isEmpty())  View.VISIBLE else View.GONE
    }

    private fun getTotalAmount(): String {
        var totalAmount: Float = 0.0F
        if (!resultList.isEmpty()) {
            for (i in 0..(resultList.size - 1)) totalAmount += resultList[i].productVersion.toFloat()
        }
        return totalAmount.toString()
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    fun addItemToCart(name: String, price: String, image: Int) {
        resultList.add(Product(image, name, price ))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CartFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
