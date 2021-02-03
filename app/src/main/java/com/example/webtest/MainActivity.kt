package com.example.webtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.webtest.adapter.TabAdater
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAdapter()
    }

    private fun setAdapter() {
        sliding_tabs!!.addTab(sliding_tabs!!.newTab().setText("List"))
        sliding_tabs!!.addTab(sliding_tabs!!.newTab().setText("Add User"))
        sliding_tabs!!.tabGravity = TabLayout.GRAVITY_FILL
        val adviseNotesTabAdater = TabAdater(
            supportFragmentManager,
            sliding_tabs!!.tabCount
        )
        viewpager!!.adapter = adviseNotesTabAdater
        viewpager!!.addOnPageChangeListener(TabLayoutOnPageChangeListener(sliding_tabs))
        sliding_tabs!!.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}