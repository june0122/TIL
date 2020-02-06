package com.june0122.bis_sample.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.june0122.bis_sample.R
import com.june0122.bis_sample.ui.fragment.SearchInfoFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SearchInfoFragment(), SearchInfoFragment::class.java.name)
                .commit()
    }
}