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
import java.text.SimpleDateFormat
import java.util.*

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
        var status: TextView = myView.findViewById(R.id.card_request_status)
        var creationDate: TextView = myView.findViewById(R.id.card_request_date_created)



        fun initializeUIComponents(model: RequestModel){
            description.text = model.description
            roomNumber.text = model.roomNumber
            cause.text = model.cause
            priority.text = model.priority
            status.text = model.status
            category.text = model.category
            creationDate.text = model.dateTime
        }

        fun formatData(date: String): String{
            val format = SimpleDateFormat("yyyy-MM-ddTHH:mm:sss", Locale.GERMANY)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val date = format.parse(date)
            val outputFormat = SimpleDateFormat("yyyy MMMM dd - HH:mm")
            return outputFormat.format(date)
        }

    }
}

