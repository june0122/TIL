package com.june0122.bis_sample.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.june0122.bis_sample.R
import com.june0122.bis_sample.ui.adapter.SearchResultViewPagerAdapter
import com.june0122.bis_sample.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.fragment_preview_bus.*
import kotlinx.android.synthetic.main.fragment_search.*

fun EditText.setAttributes(hint: String, inputType: Int) {
    this.hint = hint
    this.inputType = inputType
}

class SearchInfoFragment : Fragment() {
    var inputData = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = SearchResultViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(PreviewBusFragment(inputData), "버스")
        viewPagerAdapter.addFragment(PreviewStationFragment(inputData), "정류장")
        searchResultViewPager.adapter = viewPagerAdapter
        searchTypeTabs.setupWithViewPager(searchResultViewPager, false)  // autoRefresh 검증 필요

        searchTypeTabs.getTabAt(0)?.setIcon(R.drawable.ic_bus_front)
        searchTypeTabs.getTabAt(1)?.setIcon(R.drawable.ic_bus_station)
        searchTypeTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("INPUT", inputData)
                when (tab?.position) {
                    0 -> searchEditText.setAttributes(getString(R.string.search_tab_hint_1), InputType.TYPE_CLASS_NUMBER)
                    1 -> searchEditText.setAttributes(getString(R.string.search_tab_hint_2), InputType.TYPE_CLASS_TEXT)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        Thread(Runnable {
            activity?.runOnUiThread {


                searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        inputData = searchEditText.text.toString()

                        when (searchTypeTabs.selectedTabPosition) {
                            0 -> viewPagerAdapter.updateBusFragment(PreviewBusFragment(inputData))
                            1 -> viewPagerAdapter.updateStationFragment(PreviewStationFragment(inputData))
                        }
                        viewPagerAdapter.notifyDataSetChanged()
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }
                })

            }
        }).start()

        searchResultViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }
        })
    }
}