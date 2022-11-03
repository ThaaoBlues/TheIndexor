package com.example.theindexor

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.jsoup.Jsoup


class Indexor(argcontext : Context) {

    var context : Context = argcontext
    fun Search(query : String): MutableList<Result>{




        val request = Request.Builder().url("https://raw.githubusercontent.com/ThaaoBlues/TheIndexor/master/database.json").build();

        val client = OkHttpClient()

        val response = client.newCall(request).execute()

        var json = JSONObject(response.body()?.string() ?: "{}")

        var results : MutableList<com.example.theindexor.Result> = mutableListOf()


        //loop on each website from database.json

        json.keys().forEach {

            scrape_website(JSONObject(json[it].toString()),query).forEach {
                results.add(it)
            }
        }





        return results
    }


    // called for each website present in database
    fun scrape_website(site_struct : JSONObject,query : String) : List<Result>{

        val doc = Jsoup.connect(site_struct["search_url"].toString() + query)
            .ignoreHttpErrors(true)
            .get()

        val results_qtt = doc.select(site_struct["title_class"].toString()).size
        val results: MutableList<Result> = mutableListOf()


        val titles = doc.select(site_struct["title_class"].toString())
        val descriptions = doc.select(site_struct["desc_class"].toString())
        val minia_urls = doc.select(site_struct["minia_url_class"].toString())
        val content_urls = doc.select(site_struct["content_url_class"].toString())

        for (i in 0 until results_qtt) {
            var r = Result()
            r.setTitle(titles.get(i).text())
            r.setDesc(descriptions.get(i).text())
            r.setMiniaUrl(minia_urls.get(i).attr("href").toString())
            r.setUrl(content_urls.get(i).attr("href").toString())
            results.add(r)
        }
        return results
    }

}