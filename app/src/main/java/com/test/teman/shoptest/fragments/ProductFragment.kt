package com.test.teman.shoptest.fragments

import android.content.Context
import android.content.res.TypedArray
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.test.teman.shoptest.MainActivity
import com.test.teman.shoptest.R
import android.text.method.ScrollingMovementMethod
import com.test.teman.shoptest.model.ImagesAdapter
import com.test.teman.shoptest.support.CenterZoomLayoutManager

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var infoName: String = ""
private var infoPrice: String = ""
private var infoImage: Int = 0

private lateinit var infoAll: Array<String>
private lateinit var imageList: TypedArray
private lateinit var imageListConverted: IntArray

private lateinit var rvImages: RecyclerView

class ProductFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        infoAll = context!!.resources!!.getStringArray(R.array.infoAbout)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val viewFinal = inflater.inflate(R.layout.fragment_product, container, false)

        val textViewInfoName = viewFinal.findViewById<TextView>(R.id.textViewInfoName)
        textViewInfoName.text = infoName

        val textViewPrice = viewFinal.findViewById<TextView>(R.id.textViewVersion)
        textViewPrice.text = infoPrice

        val buttonCancel = viewFinal.findViewById<Button>(R.id.buttonCancel)
        val buttonAdd = viewFinal.findViewById<Button>(R.id.buttonAddToCart)

        buttonCancel.setOnClickListener {
            (context as MainActivity).setFragment("elseWhere")
        }
        buttonAdd.setOnClickListener{
            Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
        }

        val textViewInfo = viewFinal.findViewById<TextView>(R.id.textViewInfoAll)
        textViewInfo.text = infoAll[
                when (infoName) {
                    "Donut" -> 0
                    "Ice-cream" -> 1
                    "Eclair" -> 2
                    "Jelly" -> 3
                    "Froyo" -> 4
                    "KitKat" -> 5
                    "Gingerbread" -> 6
                    "Lollipop" -> 7
                    "Honeycomb" -> 8
                    "Marshmallow" -> 9
                    else -> 10
                }]

        textViewInfo.movementMethod = ScrollingMovementMethod()

        imageList = context!!.resources.obtainTypedArray(
                when (infoName) {
                    "Donut" -> R.array.infoImagesDonut
                    "Ice-cream" -> R.array.infoImagesIcecream
                    "Eclair" -> R.array.infoImagesEclair
                    "Jelly" -> R.array.infoImagesJelly
                    "Froyo" -> R.array.infoImagesFroyo
                    "KitKat" -> R.array.infoImagesKitkat
                    "Gingerbread" -> R.array.infoImagesGinger
                    "Lollipop" -> R.array.infoImagesLollipop
                    "Honeycomb" -> R.array.infoImagesHoney
                    "Marshmallow" -> R.array.infoImagesMarsh
                    else -> R.array.infoImagesPie
                }
        )

        imageListConverted = IntArray(3)
        for (i in 0..2) imageListConverted[i] = imageList.getResourceId(i, -1)

        val imageAdapter = ImagesAdapter(imageListConverted, this.context)

        rvImages = viewFinal.findViewById<RecyclerView>(R.id.recyclerViewInfoImages).apply {
            setHasFixedSize(true)
            layoutManager = CenterZoomLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
        }

        LinearSnapHelper().attachToRecyclerView(rvImages)

        rvImages.smoothScrollToPosition(0)

        return viewFinal
    }
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
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
        rvImages.scrollToPosition(0)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ProductFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun setInfo (name: String, price: String, image: Int) {
        infoName = name
        infoPrice = price
        infoImage = image
    }
}
