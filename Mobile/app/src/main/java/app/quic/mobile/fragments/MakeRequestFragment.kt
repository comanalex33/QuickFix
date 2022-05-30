package app.quic.mobile.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.quic.mobile.R
import app.quic.mobile.activities.MainActivity
import app.quic.mobile.activities.RegisterActivity
import app.quic.mobile.dialogs.InfoDialog
import app.quic.mobile.models.CategoryModel
import app.quic.mobile.models.ErrorModel
import app.quic.mobile.models.LoginModel
import app.quic.mobile.models.TokenModel
import app.quic.mobile.services.ApiClient
import app.quic.mobile.services.LoggedInUser
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeRequestFragment : Fragment() {

    private lateinit var categorySpinner: Spinner
    private lateinit var prioritySpinner: Spinner
    private lateinit var descriptionField: EditText
    private lateinit var roomNumberField: EditText
    private lateinit var causeField: EditText
    private lateinit var sendRequestButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_make_request, container, false)

        val toolbarTitle = (activity as MainActivity).toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Make request"

        categorySpinner = view.findViewById(R.id.request_category)
        prioritySpinner = view.findViewById(R.id.request_priority)
        descriptionField = view.findViewById(R.id.request_description)
        roomNumberField = view.findViewById(R.id.request_room_number)
        causeField = view.findViewById(R.id.request_cause)
        sendRequestButton= view.findViewById(R.id.send_request_button)

        getCategories()

        val priorityList: List<String> = listOf("low", "medium", "high")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, priorityList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = adapter

        sendRequestButton.setOnClickListener {

        }

        return view
    }

    fun getCategories(){
        val categoryListCall: Call<List<CategoryModel>> = ApiClient.getService().getCategories()
        categoryListCall.enqueue(object : Callback<List<CategoryModel>> {
            override fun onResponse(call: Call<List<CategoryModel>>, response: Response<List<CategoryModel>>) {
                if(response.isSuccessful) {
                    var categoryList: List<CategoryModel> = response.body()!!
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    categorySpinner.adapter = adapter
                } else {
                    val gson = Gson()
                    val error = gson.fromJson(response.errorBody()?.string(), ErrorModel::class.java)
                    val dialog = InfoDialog(error.title)
                    dialog.show(childFragmentManager, "Information dialog")
                }
            }

            override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
                if(t.message != null){
                    val dialog = InfoDialog(t.message!!)
                    dialog.show(childFragmentManager, "Information dialog")
                } else {
                    val dialog = InfoDialog("Something went wrong")
                    dialog.show(childFragmentManager, "Information dialog")
                }
            }

        })
    }
}