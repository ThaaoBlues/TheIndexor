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

        // select categories
        json = JSONObject(json[category].toString())


        json.keys().forEach {
            scrape_website(JSONObject(json[it].toString()),query).forEach {
                results.add(it)
            }
        }

        // add soap2day as we can't scrape it but the website is incredible

        if (category == "film-series") {
            var r : Result = Result()
            r.setUrl("https://soapgate.org")
            r.setTitle("Soap2day websites are one of the best platform but we can't scrape content on it yet. You can still try to see if your content is in here :)")
            results.add(r)
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

        // first, eliminate all bad links
        for(i in 0 until content_urls.size){

            // as we may shorten the array
            if (i > content_urls.size){
                break
            }

            // now verify size and match
            val url = content_urls[i].attr("href").toString()
            val url_scheme = site_struct["content_url_scheme"].toString()
            if (!url
                .contains(url_scheme) ||
                !(url.length > url_scheme.length)){

                // remove this
                content_urls.removeAt(i)
            }
        }

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
                if(i < content_urls.size){

                    // check if we didn't scrap a relative path
                    var url = content_urls[i].attr("href").toString()
                    if (!url.contains("http")){
                        // get the domain name and add it http + content relative path
                        url = "http://"+
                                site_struct["search_url"].toString().split("/")[2]+ url
                    }

                    r.setUrl(url)

                }else{
                    r.setUrl("scraping error, sorry :/")
                }
                results.add(r)
            }
        }

        return results
    }

}