package com.june0122.bis_sample.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.june0122.bis_sample.R
import com.june0122.bis_sample.ui.fragment.SearchFragment
import com.june0122.bis_sample.ui.fragment.StationBusListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    StationBusListFragment(), StationBusListFragment::class.java.name)
                .commit()
    }

}
