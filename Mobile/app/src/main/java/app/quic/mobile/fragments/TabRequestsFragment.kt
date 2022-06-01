package app.quic.mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.quic.mobile.R
import app.quic.mobile.activities.MainActivity
import app.quic.mobile.adapters.AdapterRequests
import app.quic.mobile.models.RequestModel
import app.quic.mobile.services.ApiClient
import app.quic.mobile.services.LoggedInUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TabRequestsFragment(var type: String): Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvRequestsAdapter: AdapterRequests
    private lateinit var requests: ArrayList<RequestModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tab_requests, container, false)

        recyclerView = view.findViewById(R.id.requests_view)
        rvRequestsAdapter = AdapterRequests(activity as MainActivity, childFragmentManager, type)
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
                        }
                    }
                    requestList?.let { rvRequestsAdapter.setRequests(it) }
                }
            }

            override fun onFailure(call: Call<List<RequestModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getSpecificRequests(){
        val requestsCall: Call<List<RequestModel>> = ApiClient.getService().getRequestsByUsername(
            LoggedInUser.username!!)

        requests = ArrayList()

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
                        }
                    }
                    requestList?.let { rvRequestsAdapter.setRequests(it) }
                }
            }

            override fun onFailure(call: Call<List<RequestModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}