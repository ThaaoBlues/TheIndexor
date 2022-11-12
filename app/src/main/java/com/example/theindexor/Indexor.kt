package com.example.theindexor

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*
import kotlin.math.min
import kotlin.reflect.typeOf


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
            r.setScript("NO SELECTOR")
            results.add(r)
        }


        return results
    }


    // called for each website present in database
    fun scrape_website(site_struct : JSONObject,query : String) : List<Result>{

        val doc = Jsoup.connect(site_struct["search_url"].toString().replace("%s",query))
            .ignoreHttpErrors(true)
            .get()

        val website_hostname = site_struct["search_url"].toString().split("/")[2]


        val titles = doc.select(site_struct["title_css_selector"].toString())
        val descriptions = doc.select(site_struct["desc_css_selector"].toString())
        val minia_urls = doc.select(site_struct["minia_url_css_selector"].toString())
        var content_urls : MutableList<String> = mutableListOf()

        // filter all trash urls and duplicate results
        val url_scheme = site_struct["content_url_scheme"].toString()
        doc.select(site_struct["content_url_css_selector"].toString()).forEach {

            val url = it.attr("href").toString()
            // check url scheme and length
            if (url.contains(url_scheme) && url.length > url_scheme.length){



                // don't add  duplicates
                if(content_urls.lastIndex != -1){
                    if(content_urls[content_urls.lastIndex] != url){
                        content_urls.add(url)

                    }
                }else{
                    // first element
                    content_urls.add(url)
                }


            }
        }

        // init results list
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
                if(i < content_urls.size){

                    var url = content_urls[i]


                    // check if we didn't scrap a relative path
                    if (!url.contains("http")){
                        // get the domain name and add it http + content relative path
                        url = "http://$website_hostname/$url"
                    }

                    r.setUrl(url)

                }else{
                    continue // as without link displaying the result is useless
                }

                // add should click on element
                r.setScript(site_struct["script"].toString())

                results.add(r)
            }
        }

        return results
    }

}