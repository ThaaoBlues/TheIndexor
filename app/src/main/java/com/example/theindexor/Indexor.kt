package com.example.theindexor

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.*
import kotlin.math.min


class Indexor(argcontext : Context) {

    var context : Context = argcontext
    fun Search(query : String,category: String): MutableList<Result>{




        val request = Request.Builder().url("https://raw.githubusercontent.com/ThaaoBlues/TheIndexor/master/database.json").build();

        val client = OkHttpClient()

        val response = client.newCall(request).execute()

        var json = JSONObject(response.body()?.string() ?: "{}")

        var results : MutableList<Result> = mutableListOf()


        //loop on each website from database.json

        // select language
        var lang : String = Locale.getDefault().toString().split("_")[0]
        json = JSONObject(json[lang].toString())

        println(json.toString())
        // select categories
        json = JSONObject(json[category].toString())


        json.keys().forEach {
            scrape_website(JSONObject(json[it].toString()),query).forEach {
                results.add(it)
            }
        }


        return results
    }


    // called for each website present in database
    fun scrape_website(site_struct : JSONObject,query : String) : List<Result>{

        val doc = Jsoup.connect(site_struct["search_url"].toString().replace("%s",query))
            .ignoreHttpErrors(true)
            .get()

        val titles = doc.select(site_struct["title_css_selector"].toString())
        val descriptions = doc.select(site_struct["desc_css_selector"].toString())
        val minia_urls = doc.select(site_struct["minia_url_css_selector"].toString())
        val content_urls = doc.select(site_struct["content_url_css_selector"].toString())

        val results: MutableList<Result> = mutableListOf()

        if (titles.size > 0) {
            for (i in 0 until titles.size) {
                var r = Result()

                r.setTitle(titles[i].text())
                if(i < descriptions.size){
                    r.setDesc(descriptions[i].text())
                }else{
                    r.setDesc("scraping error, sorry :/")
                }

                if(i < minia_urls.size){
                    r.setMiniaUrl(minia_urls[i].attr("href").toString())
                }else{
                    r.setMiniaUrl("scraping error, sorry :/")
                }
                if((i < content_urls.size) &&
                    (content_urls[i].attr("href").toString()
                        .contains(site_struct["content_url_scheme"].toString()))){

                    r.setUrl(content_urls[i].attr("href").toString())
                }else{
                    r.setUrl("scraping error, sorry :/")
                }
                results.add(r)
            }
        }

        return results
    }

}