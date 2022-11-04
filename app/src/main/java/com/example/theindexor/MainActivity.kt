package com.example.theindexor

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.theindexor.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //allow network on main thread
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        val search_btn : Button = findViewById(R.id.search_button)
        val search_bar : EditText = findViewById(R.id.main_search_bar)

        search_btn.setOnClickListener {

            // change fragment and put search query into bundle
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val b = Bundle()
            b.putString("query", search_bar.text.toString())
            var f = results_list()
            f.arguments = b
            transaction.replace(R.id.content_main_activity,f)
            transaction.commit()
        }


    }

}