package com.test.teman.shoptest

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.View
import com.test.teman.shoptest.fragments.CartFragment
import com.test.teman.shoptest.fragments.ProductFragment
import com.test.teman.shoptest.fragments.ShopFragment

private lateinit var bottomNavigation: BottomNavigationView
private lateinit var fragmentShop: Fragment
private lateinit var fragmentCart: Fragment
private lateinit var fragmentProduct: Fragment

class MainActivity : AppCompatActivity(), ShopFragment.OnFragmentInteractionListener,
        CartFragment.OnFragmentInteractionListener,
        ProductFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigationView()

        bottomNavigation = findViewById(R.id.bottomNavigationView)

        fragmentShop = ShopFragment()
        fragmentCart = CartFragment()
        fragmentProduct = ProductFragment()

        addFragment(fragmentShop, R.id.frameLayoutMain)
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_shop -> {
                    replaceFragment(fragmentShop, R.id.frameLayoutMain)
                }
                R.id.menu_cart -> {
                    replaceFragment(fragmentCart, R.id.frameLayoutMain)
                }
            }
            true
        }
    }

    fun setFragment(fromWhere: String) {
        replaceFragment(when (fromWhere) {
            "fragmentAdapter" -> fragmentProduct else -> fragmentShop
        }, R.id.frameLayoutMain)
    }
}
