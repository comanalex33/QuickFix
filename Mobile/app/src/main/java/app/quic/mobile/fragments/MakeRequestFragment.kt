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
import app.quic.mobile.models.*
import app.quic.mobile.services.ApiClient
import app.quic.mobile.services.LoggedInUser
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class MakeRequestFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var categorySpinner: Spinner
    private lateinit var prioritySpinner: Spinner
    private lateinit var descriptionField: EditText
    private lateinit var roomNumberField: EditText
    private lateinit var causeField: EditText
    private lateinit var sendRequestButton: Button

    private var selectedCategory: CategoryModel? = null
    var categories: List<CategoryModel> = listOf()
    private var selectedPriority: String? = null
    val priorityList: List<String> = listOf("low", "medium", "high")

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
        categorySpinner.onItemSelectedListener = this
        prioritySpinner.onItemSelectedListener = this

        getCategories()

        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, priorityList)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = adapter2

        sendRequestButton.setOnClickListener {
            val requestModel = RequestNewModel(LoggedInUser.username!!, descriptionField.text.toString(), roomNumberField.text.toString(), causeField.text.toString(), selectedCategory!!.name, selectedPriority!!, "pending", LocalDateTime.now().toString())
            val requestCall: Call<RequestModel> = ApiClient.getService().sendRequest(LoggedInUser.getTokenForAuthentication()!!, requestModel)

            requestCall.enqueue(object : Callback<RequestModel> {
                override fun onResponse(call: Call<RequestModel>, response: Response<RequestModel> ) {
                    if(response.isSuccessful) {
                        Toast.makeText(context, "Request sent successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = response.errorBody()?.string()
                        if(errorMessage != null) {
                            val dialog = InfoDialog(errorMessage)
                            dialog.show(childFragmentManager, "Information dialog")
                        } else {
                            val dialog = InfoDialog("Something went wrong")
                            dialog.show(childFragmentManager, "Information dialog")
                        }
                    }
                }

                override fun onFailure(call: Call<RequestModel>, t: Throwable) {
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

        return view
    }

    private fun getCategories(){
        val categoryListCall: Call<List<CategoryModel>> = ApiClient.getService().getCategories()
        categoryListCall.enqueue(object : Callback<List<CategoryModel>> {
            override fun onResponse(call: Call<List<CategoryModel>>, response: Response<List<CategoryModel>>) {
                if(response.isSuccessful) {
                    categories = response.body()!!
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if(p0!!.id == R.id.request_category)
            selectedCategory = categories[p2]
        else
            selectedPriority = priorityList[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}