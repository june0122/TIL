package com.june0122.bis_sample.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.june0122.bis_sample.R
import com.june0122.bis_sample.ui.adapter.SearchResultViewPagerAdapter
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

        searchTypeTabs.getTabAt(0)?.setIcon(R.drawable.ic_bus_front)
        searchTypeTabs.getTabAt(1)?.setIcon(R.drawable.ic_bus_station)
        searchEditText.inputType = InputType.TYPE_CLASS_NUMBER

        searchTypeTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                searchEditText.setAttributes("버스 검색", InputType.TYPE_CLASS_NUMBER)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> searchEditText.setAttributes("버스 검색", InputType.TYPE_CLASS_NUMBER)
                    1 -> searchEditText.setAttributes("정류장, ID 검색", InputType.TYPE_CLASS_TEXT)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        val viewPagerAdapter = SearchResultViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(SearchBusFragment(inputData), "버스")
        viewPagerAdapter.addFragment(SearchStationFragment(), "정류장")
        searchResultViewPager.adapter = viewPagerAdapter
        searchTypeTabs.setupWithViewPager(searchResultViewPager)

        Thread(Runnable {
            activity?.runOnUiThread {

                searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        inputData = searchEditText.text.toString()
                        viewPagerAdapter.updateFragment(SearchBusFragment(inputData))
                        viewPagerAdapter.notifyDataSetChanged()
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }
                })
            }
        }).start()
    }
}