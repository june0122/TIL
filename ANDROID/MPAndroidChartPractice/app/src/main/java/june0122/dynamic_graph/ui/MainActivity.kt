package june0122.dynamic_graph.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import june0122.dynamic_graph.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                GraphFragmentA(), GraphFragmentA::class.java.name)
            .commit()
    }
}
