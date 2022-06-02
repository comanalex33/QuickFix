package app.quic.mobile.adapters

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import app.quic.mobile.HelperClass
import app.quic.mobile.R
import app.quic.mobile.dialogs.InfoDialog
import app.quic.mobile.models.RequestModel
import app.quic.mobile.services.ApiClient
import app.quic.mobile.services.LoggedInUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class AdapterRequests(var context: Context, var fragment: FragmentManager, var statusText: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var arrayList = emptyList<RequestModel>()
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.card_request, parent, false)
        return RequestViewHolder(view)
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
        (holder as RequestViewHolder)
            .initializeUIComponents(requestItem)
        holder.acceptButton.setOnClickListener {
            selectedPosition = position
            val calendar = Calendar.getInstance();
            val year = calendar.get(Calendar.YEAR);
            val month = calendar.get(Calendar.MONTH);
            val day = calendar.get(Calendar.DAY_OF_MONTH);
            val datePickerDialog = DatePickerDialog(context, this, year, month, day)
            datePickerDialog.show()
        }
        holder.declineButton.setOnClickListener {
            changeMoreStatus("declined", arrayList[position].id, LocalDateTime.now().toString())
            holder.buttons.visibility = View.GONE
            holder.statusMessageBox.visibility = View.VISIBLE
            holder.statusMessage.text = "Declined request"
            holder.status.text = "declined"
            HelperClass.notify(arrayList[position].username, "Your service request has been declined!", "Request status", context!!)
        }
        holder.doneRequestButton.setOnClickListener {
            changeMoreStatus("done", arrayList[position].id, LocalDateTime.now().toString())
            holder.processingBox.visibility = View.GONE
            holder.statusMessageBox.visibility = View.VISIBLE
            holder.statusMessage.text = "Request processed"
            holder.status.text = "done"
            HelperClass.notify(arrayList[position].username, "Your service request has been processed!", "Request status", context!!)
        }
    }

    private fun changeStatus(status: String, id: Long){
        val changeStatusCall: Call<RequestModel> = ApiClient.getService().changeStatus(LoggedInUser.getTokenForAuthentication()!!, id, status)

        changeStatusCall.enqueue(object: Callback<RequestModel>{
            override fun onResponse(call: Call<RequestModel>, response: Response<RequestModel>) {
                if(response.isSuccessful) {
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

    private fun changeMoreStatus(status: String, id: Long, date: String) {
        val changeStatusCall: Call<RequestModel> = ApiClient.getService().changeMoreStatus(LoggedInUser.getTokenForAuthentication()!!, id, status, LoggedInUser.username!!, date)

        changeStatusCall.enqueue(object: Callback<RequestModel>{
            override fun onResponse(call: Call<RequestModel>, response: Response<RequestModel>) {
                if(response.isSuccessful) {
                    val dialog = InfoDialog("Updated status!")
                    dialog.show(fragment, "Information dialog")
                } else {
                    val errorMessage = response.errorBody()?.string()
                    if(errorMessage != null) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        println("Eroare: $errorMessage")
                    } else {
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<RequestModel>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    // Holder that initialize elements inside the card view
    inner class RequestViewHolder(myView: View) : RecyclerView.ViewHolder(myView){
        var description: TextView = myView.findViewById(R.id.card_request_description)
        var building: TextView = myView.findViewById(R.id.card_request_building)
        var roomNumber: TextView = myView.findViewById(R.id.card_request_room)
        var cause: TextView = myView.findViewById(R.id.card_request_cause)
        var category: TextView = myView.findViewById(R.id.card_request_category)
        var priority: TextView = myView.findViewById(R.id.card_request_priority)
        var status: TextView = myView.findViewById(R.id.card_request_status)
        var creationDate: TextView = myView.findViewById(R.id.card_request_date_created)
        var buttons: ConstraintLayout = myView.findViewById(R.id.buttons_request)
        var acceptButton: Button = myView.findViewById(R.id.accept_request_button)
        var declineButton: Button = myView.findViewById(R.id.decline_request_button)
        var statusMessageBox: ConstraintLayout = myView.findViewById(R.id.request_status_message)
        var statusMessage: TextView = myView.findViewById(R.id.request_status_message_text)
        var processingBox: ConstraintLayout = myView.findViewById(R.id.processing_button)
        var doneRequestButton: Button = myView.findViewById(R.id.done_request_button)
        var reporterBox: LinearLayout = myView.findViewById(R.id.card_request_reporter_box)
        var reporter: TextView = myView.findViewById(R.id.card_request_reporter)
        var statusDateBox: LinearLayout = myView.findViewById(R.id.card_request_date_status_box)
        var statusDateText: TextView = myView.findViewById(R.id.card_request_date_status_text)
        var statusDate: TextView = myView.findViewById(R.id.card_request_date_status)


        fun initializeUIComponents(model: RequestModel){
            description.text = model.description
            building.text = model.building
            roomNumber.text = model.roomNumber
            cause.text = model.cause
            priority.text = model.priority
            status.text = model.status
            category.text = model.category
            creationDate.text = formatData(model.dateTime)
            reporter.text = model.handyman
            statusDate.text = formatData(model.acceptedDate)

            if(statusText == "processing") {
                statusDateText.text = "Appointment date"
            } else if(status.text == "declined") {
                statusDateText.text = "Declined date"
            } else if(status.text == "done") {
                statusDateText.text = "Finish date"
            }

            if(LoggedInUser.getUserRole() == "handyman"){
                buttons.visibility = View.VISIBLE
                processingBox.visibility = View.VISIBLE

            } else {
                buttons.visibility = View.GONE
                processingBox.visibility = View.GONE
            }

            if(statusText != "pending") {
                statusDateBox.visibility = View.VISIBLE
                reporterBox.visibility = View.VISIBLE
                buttons.visibility = View.GONE
            }

            if(statusText != "processing") {
                processingBox.visibility = View.GONE
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun formatData(date: String): String{
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss")
            val outputDate = format.parse(date)
            val outputFormat = SimpleDateFormat("yyyy MMMM dd - HH:mm")
            return outputFormat.format(outputDate!!)
        }

    }

    private var myday = 0
    private var myMonth = 0
    private var myYear = 0
    private var myHour = 0
    private var myMinute = 0

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        myYear = p1
        myMonth = p2
        myday = p3
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(context, this, hour, minute, true)
        timePickerDialog.show();
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        myHour = p1
        myMinute = p2
        val date = "${myYear}-${correctFormat(myMonth)}-${correctFormat(myday)}T${correctFormat(myHour)}:${correctFormat(myMinute)}:00"
        changeMoreStatus("processing", arrayList[selectedPosition].id, date)
        HelperClass.notify(arrayList[selectedPosition].username, "Your service request has been finished!", "Request status", context!!)
    }

    private fun correctFormat(value: Int): String {
        if(value < 10)
            return "0$value"
        return "$value"
    }
}

