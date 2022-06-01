package app.quic.mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.quic.mobile.services.LoggedInUser
import app.quic.mobile.R
import app.quic.mobile.activities.MainActivity
import app.quic.mobile.adapters.AdapterRequests
import app.quic.mobile.models.RequestModel
import app.quic.mobile.services.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvRequestsAdapter: AdapterRequests
    private lateinit var requests: ArrayList<RequestModel>
    private lateinit var requests2: ArrayList<RequestModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val toolbarTitle = (activity as MainActivity).toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Home ${LoggedInUser.getUserRole()}"

        recyclerView = view.findViewById(R.id.home_view)
        rvRequestsAdapter = AdapterRequests(activity as MainActivity, childFragmentManager, "home")
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = rvRequestsAdapter
        if(LoggedInUser.getUserRole() == "student") {
            getSpecificRequests()
        }
        else{
            getAllRequests()
        }

        return view
    }

    private fun getAllRequests() {
        val requestsCall: Call<List<RequestModel>> = ApiClient.getService().getAllRequests()

        requests = ArrayList()
        requests2 = ArrayList()

        requestsCall.enqueue(object : Callback<List<RequestModel>> {
            override fun onResponse(
                call: Call<List<RequestModel>>,
                response: Response<List<RequestModel>>
            ) {
                if(response.isSuccessful) {
                    val requestList: List<RequestModel>? = response.body()

                    if(requestList != null) {
                        for(request in requestList) {
                            requests.add(request)
                            if(request.getRequestStatus() == "accepted"){
                                requests2.add(request)
                            }
                        }
                    }

                    requests2?.let { rvRequestsAdapter.setRequests(it) }
                }
            }

            override fun onFailure(call: Call<List<RequestModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getSpecificRequests(){
        val requestsCall: Call<List<RequestModel>> = ApiClient.getService().getRequestsByUsername(LoggedInUser.username!!)

        requests = ArrayList()
        requests2 = ArrayList()

        requestsCall.enqueue(object : Callback<List<RequestModel>> {
            override fun onResponse(
                call: Call<List<RequestModel>>,
                response: Response<List<RequestModel>>
            ) {
                if(response.isSuccessful) {
                    val requestList: List<RequestModel>? = response.body()
                    if(requestList != null) {
                        for(request in requestList) {
                            requests.add(request)
                            if(request.getRequestStatus() == "accepted"){
                                requests2.add(request)
                            }
                        }
                    }
                    requests2?.let { rvRequestsAdapter.setRequests(it) }
                }
            }

            override fun onFailure(call: Call<List<RequestModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}