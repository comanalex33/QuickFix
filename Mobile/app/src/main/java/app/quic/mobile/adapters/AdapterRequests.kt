package app.quic.mobile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import app.quic.mobile.HelperClass
import app.quic.mobile.R
import app.quic.mobile.dialogs.InfoDialog
import app.quic.mobile.models.CategoryModel
import app.quic.mobile.models.RequestModel
import app.quic.mobile.services.ApiClient
import app.quic.mobile.services.LoggedInUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AdapterRequests(var context: Context, var fragment: FragmentManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        holder.acceptButton.setOnClickListener {
            changeStatus("accepted", arrayList[position].id)
            holder.buttons.visibility = View.GONE
            holder.status.text = "accepted"
            HelperClass.notify(arrayList[position].username, "Your service request has been accepted!", "Request status", context!!)
        }
        holder.declineButton.setOnClickListener {
            changeStatus("declined", arrayList[position].id)
            holder.buttons.visibility = View.GONE
            holder.status.text = "accepted"
            HelperClass.notify(arrayList[position].username, "Your service request has been declined!", "Request status", context!!)
        }
    }

    fun changeStatus(status: String, id: Long){
        val changeStatusCall: Call<RequestModel> = ApiClient.getService().changeStatus(LoggedInUser.getTokenForAuthentication()!!, id, status)

        changeStatusCall.enqueue(object: Callback<RequestModel>{
            override fun onResponse(call: Call<RequestModel>, response: Response<RequestModel>) {
                if(response.isSuccessful) {
                    //Toast.makeText(context, "Updated status!", Toast.LENGTH_SHORT).show()
                    val dialog = InfoDialog("Updated status!")
                    dialog.show(fragment, "Information dialog")
                } else {
                    val errorMessage = response.errorBody()?.string()
                    if(errorMessage != null) {
                        //Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<RequestModel>, t: Throwable) {
                //Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
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
        var buttons: ConstraintLayout = myView.findViewById(R.id.buttons_request)
        var acceptButton: Button = myView.findViewById(R.id.accept_request_button)
        var declineButton: Button = myView.findViewById(R.id.decline_request_button)



        fun initializeUIComponents(model: RequestModel){
            description.text = model.description
            roomNumber.text = model.roomNumber
            cause.text = model.cause
            priority.text = model.priority
            status.text = model.status
            category.text = model.category
            creationDate.text = formatData(model.dateTime)

            if(LoggedInUser.getUserRole() == "handyman"){
                buttons.visibility = View.VISIBLE
            }
            else{
                buttons.visibility = View.GONE
            }

            if(status.text != "pending"){
                buttons.visibility = View.GONE
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun formatData(date: String): String{
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss", Locale.GERMANY)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val outputDate = format.parse(date)
            val outputFormat = SimpleDateFormat("yyyy MMMM dd - HH:mm")
            return outputFormat.format(outputDate!!)
        }

    }
}

