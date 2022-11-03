package com.example.theindexor

class Result{


    data class ResultType(var desc : String,
                          var url : String,
                          var minia_url : String,
                          var title : String)

    open var content = ResultType("","","","");

    fun setDesc(arg: String){
        content.desc = arg;
    }

    fun setUrl(arg: String){
        content.url = arg;
    }

    fun setMiniaUrl(arg: String){
        content.minia_url = arg;
    }

    fun setTitle(arg: String){
        content.title = arg;
    }

    fun getDesc(): String {
        return content.desc;
    }

    fun getUrl(): String{
        return content.url
    }

    fun getMiniaUrl(): String{
        return content.minia_url
    }

    fun getTitle(): String{
        return content.title
    }




}