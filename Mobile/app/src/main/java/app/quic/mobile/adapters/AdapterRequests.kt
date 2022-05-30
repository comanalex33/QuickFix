package app.quic.mobile.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.quic.mobile.R
import app.quic.mobile.models.CategoryModel
import app.quic.mobile.models.RequestModel
import app.quic.mobile.services.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterRequests(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var arrayList = emptyList<RequestModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.card_request, parent, false)
        return FaqViewHolder(view)
    }

    fun setRequests(list: List<RequestModel>){
        this.arrayList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val requestItem: RequestModel = arrayList[position]
        (holder as FaqViewHolder)
            .initializeUIComponents(requestItem)
    }

    // Holder that initialize elements inside the card view
    inner class FaqViewHolder(myView: View) : RecyclerView.ViewHolder(myView){
        var description: TextView = myView.findViewById(R.id.card_request_description)
        var roomNumber: TextView = myView.findViewById(R.id.card_request_room)
        var cause: TextView = myView.findViewById(R.id.card_request_cause)
        var category: TextView = myView.findViewById(R.id.card_request_category)
        var priority: TextView = myView.findViewById(R.id.card_request_priority)



        fun initializeUIComponents(model: RequestModel){
            description.text = model.description
            roomNumber.text = model.roomNumber
            cause.text = model.cause
            priority.text = model.priority
            getBuildingName(model.categoryId)
        }

        private fun getBuildingName(id: Long) {
            val getBuildingNameCall: Call<CategoryModel> = ApiClient.getService().getCategoryById(id)

            getBuildingNameCall.enqueue(object : Callback<CategoryModel> {
                override fun onResponse(call: Call<CategoryModel>, response: Response<CategoryModel>) {
                    if(response.isSuccessful) {
                        category.text = response.body()!!.name
                    } else {
                        val errorMessage = response.errorBody()?.string()
                        if(errorMessage != null) {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<CategoryModel>, t: Throwable) {
                    if(t.message != null){
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
    }
}

