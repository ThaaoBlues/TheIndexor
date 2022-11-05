package com.example.theindexor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "query"
private const val ARG_PARAM2 = "category"


class results_list : Fragment() {
    // TODO: Rename and change types of parameters
    private var query: String? = null
    private var category : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = it.getString(ARG_PARAM1)
            category = it.getString(ARG_PARAM2)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var recycler : RecyclerView = view.findViewById(R.id.results_list_recycler)


        // effectue la recherche
        val results : MutableList<Result> = Indexor(view.context).Search(query.toString(),category.toString())

        // result object to say we didn't find anything more
        // but user can help us by updating our database
        var r : com.example.theindexor.Result = Result()
        r.setUrl("https://github.com/thaaoblues/TheIndexor")
        r.setTitle("Oops, that's all ! :/ You can help complete our database by clicking here :D")
        results.add(r)




        // this creates a vertical layout Manager
        recycler.layoutManager = LinearLayoutManager(view.context)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (result in results) {

            // TODO : replace image with real miniature
            data.add(ItemsViewModel(androidx.appcompat.R.drawable.abc_ic_go_search_api_material, result.getTitle(),result))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        recycler.adapter = adapter


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results_list, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param query search query.
         * @param category search catecory.
         * @return A new instance of fragment results_list.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(query: String,category: String) =
            results_list().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, query)
                    putString(ARG_PARAM2, category)

                }
            }
    }
}