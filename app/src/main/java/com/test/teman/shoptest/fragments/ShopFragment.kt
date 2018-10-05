package com.test.teman.shoptest.fragments

import android.content.Context
import android.content.res.TypedArray
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import com.test.teman.shoptest.R
import com.test.teman.shoptest.model.Product
import com.test.teman.shoptest.model.ProductsAdapter
import android.widget.SearchView
import android.widget.AdapterView.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_shop.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var isSheetOpened = false

private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

private lateinit var prodsNames: Array<String>
private lateinit var prodsVersions: Array<String>
private lateinit var prodsImages: TypedArray

private lateinit var rvAdapter: RecyclerView.Adapter<*>
private lateinit var rvManager: RecyclerView.LayoutManager

private lateinit var resultList: ArrayList<Product>
private lateinit var resultListCopy: ArrayList<Product>

private var isSheetCheckedFirst = true

private lateinit var rv: RecyclerView

private var isFisrtStart = true

private var savingSort: String = ""
private var savingY: Int = 0

class ShopFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        prodsNames = context!!.resources!!.getStringArray(R.array.dataNames)
        prodsVersions = context!!.resources!!.getStringArray(R.array.dataVersions)
        prodsImages = context!!.resources.obtainTypedArray(R.array.dataImageInt)

        if (isFisrtStart) setItems()

        rvManager = GridLayoutManager(context, if (isSheetChecked()) 1 else 2)
        rvAdapter = ProductsAdapter(resultList, this.context)
    }

    private fun setItems() {
        resultList = ArrayList<Product>()
        for (i in 0..prodsNames.size-1) {
            resultList.add(Product(prodsImages.getResourceId(i, -1), prodsNames[i], prodsVersions[i]))
        }
        resultListCopy = ArrayList<Product>()
        resultListCopy.addAll(resultList)
    }

    private fun sort(sortBy: String){
        when (sortBy) {
            "nameDown" -> {
                resultList.sortBy { it.productName }
            }
            "nameUp" -> {
                resultList.sortByDescending { it.productName }
            }
            "verDown" -> {
                resultList.sortBy { it.productVersion }
            }
            "verUp" -> {
                resultList.sortByDescending { it.productVersion }
            }
        }
        savingSort = sortBy
        buildRV()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val viewFinal = inflater.inflate(R.layout.fragment_shop, container, false)

        val bottomSheetView = viewFinal.findViewById<View>(R.id.bottom_sheet_sort)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)

        val bottomSheet = viewFinal.findViewById<BottomNavigationView>(R.id.bottomSheetMenu)
        bottomSheet.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.sort_name_down -> {
                    sort("nameDown")
                }
                R.id.sort_name_up -> {
                    sort("nameUp")
                }
                R.id.sort_price_down -> {
                    sort("verDown")
                }
                R.id.sort_price_up -> {
                    sort("verUp")
                }
            }
            true
        }

        //for presenting
        val checkBoxOne = viewFinal.findViewById<CheckBox>(R.id.checkBoxOne)
        val checkBoxPairs = viewFinal.findViewById<CheckBox>(R.id.checkBoxPairs)

        if (isFisrtStart) {
            checkBoxOne.isChecked = true
            isSheetCheckedFirst = true
            checkBoxOne.isClickable = false

            resultList.sortBy { it.productName }
        }

        val buttonSheet = viewFinal.findViewById<Button>(R.id.buttonBottomSheet)
        buttonSheet.setOnClickListener { viewFinal ->
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            else bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        if (isSheetOpened) bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        rv = viewFinal.findViewById<RecyclerView>(R.id.recyclerViewProducts).apply {
            setHasFixedSize(true)
            layoutManager = rvManager
            adapter = rvAdapter
        }

        rv.scrollToPosition(0)

        checkBoxOne.setOnClickListener { viewFinal ->
            checkBoxOne.isClickable = false
            checkBoxPairs.isClickable = true

            checkBoxOne.isChecked = true
            checkBoxPairs.isChecked = false

            isSheetCheckedFirst = true

            buildRV()
        }
        checkBoxPairs.setOnClickListener { viewFinal ->
            checkBoxPairs.isClickable = false
            checkBoxOne.isClickable = true

            checkBoxPairs.isChecked = true
            checkBoxOne.isChecked = false

            isSheetCheckedFirst = false

            buildRV()
        }

        isFisrtStart = false

        val searchView: SearchView = viewFinal.findViewById(R.id.searchViewProducts)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return true
            }
        })

        rv.setRecyclerListener {
            if (rv.isFocused && bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        searchView.setOnSearchClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        searchView.setOnQueryTextFocusChangeListener { view, b ->
            if (searchView.isFocused && bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        }

        searchView.setOnClickListener {
            if (searchView.isFocused && bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        return viewFinal
    }

    fun filter(text: String) {
        var text = text
        resultList.clear()
        if (text.isEmpty()) {
            resultList.addAll(resultListCopy)
        } else {
            text = text.toLowerCase()
            for (item in resultListCopy) {
                if (item.productName.toLowerCase().contains(text) || item.productVersion.toLowerCase().contains(text)) {
                    resultList.add(item)
                }
            }
        }
        buildRV()
    }

    fun buildRV() {
        rv.layoutManager = GridLayoutManager(context, if (isSheetChecked()) 1 else 2)
        rv.adapter = rvAdapter
    }

    fun isSheetChecked(): Boolean {
        return isSheetCheckedFirst
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

        if (!isFisrtStart) {
            sort(savingSort)
            kotlin.run { rv.scrollTo(rv.scrollX, savingY) }
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null

        isSheetOpened = bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED
        savingY = rv.scrollY
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ShopFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
