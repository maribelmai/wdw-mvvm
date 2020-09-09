package ar.com.maribelmai.wdw.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.com.maribelmai.wdw.R
import ar.com.maribelmai.wdw.fragments.ImageListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ImageListFragment.newInstance())
                    .commitNow()
        }
    }
}