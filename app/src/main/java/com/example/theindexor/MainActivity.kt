package com.example.theindexor

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.theindexor.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


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
        val spinner : Spinner = findViewById(R.id.fragment_main_spinner)

        //category selected on spinner

        var category : String = "manga-comics"

        search_btn.setOnClickListener {

            // change fragment and put search query into bundle
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val b = Bundle()
            b.putString("query", search_bar.text.toString())
            b.putString("category",category)
            var f = results_list()
            f.arguments = b
            transaction.replace(R.id.content_main_activity,f)
            transaction.commit()
        }


        // setup spinner
        var list : ArrayList<String> = ArrayList()

        // get available categories from database

        val request = Request.Builder().url("https://raw.githubusercontent.com/ThaaoBlues/TheIndexor/master/database.json").build();

        val client = OkHttpClient()

        val response = client.newCall(request).execute()

        var json = JSONObject(response.body()?.string() ?: "{}")

        // fr is just to select a language but we could take any
        json = JSONObject(json["fr"].toString())

        for (key in json.keys()) {
            list.add(key.toString())
        }

        var adapter : ArrayAdapter<String> = ArrayAdapter(spinner.context,R.layout.custom_dropdow_item,list)

        spinner.adapter = adapter

        spinner.setSelection(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                category = parent.getItemAtPosition(pos).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }

}