package com.example.theindexor

import android.content.Context
import android.content.res.Resources
import android.util.JsonReader
import com.example.theindexor.Result
import org.json.JSONStringer
import org.jsoup.Jsoup

class Indexor(argcontext : Context) {

    var context : Context = argcontext
    fun Search(query : String): Result{



        // TODO : loop on each website from database.json
        val doc = Jsoup.connect("https://kissmanga.nl/search?q=$query").get()

        // TODO : replace h4 by title_class etc..

        var json_string : JSONStringer;

        doc.select("h4").forEach{
            println(it.text())
        }

        var res = Result();

        res.setDesc(context.getString(R.string.lorem_ipsum))
        res.setUrl("https://portail.pages.dev")
        res.setTitle("Naruto Shipuden")
        res.setMiniaUrl("https://3.bp.blogspot.com/_hKzMu64muJg/TOCUXycqIsI/AAAAAAAAAJQ/g3uldCkRSLg/s400/naruto_wallpaper_uzumaki_naruto-20114.jpeg")

        return res
    }
}