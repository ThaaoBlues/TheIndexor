package com.example.theindexor

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [element_page.newInstance] factory method to
 * create an instance of this fragment.
 */
class element_page : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var title_textview : TextView = view.findViewById(R.id.element_title_textview)
        var desc_textview : TextView = view.findViewById(R.id.element_desc_textview)
        var minia_imgview : ImageView = view.findViewById(R.id.element_minia_imageview)
        var url_button : Button = view.findViewById(R.id.element_url_button)


        /*

        TODO : replace this part false search part with the result attached to it
            from the preview search results list fragment
        */
        var indexor : Indexor = Indexor(view.context)
        var search_result = indexor.Search("naruto")

        title_textview.text = search_result.content.title;

        desc_textview.text = search_result.content.desc;


        Picasso.get().load(search_result.content.minia_url).into(minia_imgview)
        url_button.setOnClickListener {
             startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(search_result.content.url)))
        }








    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_element_page, container, false)
    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment element_page.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            element_page().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}