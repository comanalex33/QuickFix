package app.quic.mobile.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import app.quic.mobile.R
import app.quic.mobile.interfaces.IUpdateData
import app.quic.mobile.models.BuildingModel
import app.quic.mobile.models.UserModel
import app.quic.mobile.services.ApiClient
import app.quic.mobile.services.LoggedInUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ProfileDialog : DialogFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var role: TextView
    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var building: TextView
    private lateinit var buildingEdit: Spinner
    private lateinit var updateButton: Button
    private lateinit var cancelButton: Button
    private lateinit var editButton: ImageView
    private lateinit var profileMessage: TextView

    private lateinit var userData: UserModel
    private lateinit var buildingName: String
    var buildings: List<BuildingModel> = listOf()
    private var selectedBuilding: BuildingModel? = null

    lateinit var listener: IUpdateData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_profile, container, false)

        //set background transparent
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        initUi(view)
        updateUi()
        getBuildings()

        if(LoggedInUser.getUserRole() != "student") {
            editButton.visibility = View.GONE
        }

        updateButton.setOnClickListener {
            updateBuilding()
            normalMode()
        }

        cancelButton.setOnClickListener {
            normalMode()
        }

        editButton.setOnClickListener{
            buildings = removeUnused()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, buildings)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            buildingEdit.adapter = adapter
            editMode()
        }

        return view
    }

    private fun initUi(view: View) {
        role = view.findViewById(R.id.profile_role)
        username = view.findViewById(R.id.profile_username)
        email = view.findViewById(R.id.profile_email)
        building = view.findViewById(R.id.profile_building)
        buildingEdit = view.findViewById(R.id.profile_building_edit)
        updateButton = view.findViewById(R.id.profile_update)
        cancelButton = view.findViewById(R.id.profile_cancel)
        editButton = view.findViewById(R.id.profile_edit)
        profileMessage = view.findViewById(R.id.profile_message)

        buildingEdit.onItemSelectedListener = this
    }

    private fun updateUi() {
        role.text = LoggedInUser.getUserRole()
        username.text = LoggedInUser.username
        email.text = userData.email
        building.text = buildingName
    }

    private fun normalMode() {
        updateButton.visibility = View.GONE
        editButton.visibility = View.VISIBLE
        building.visibility = View.VISIBLE
        profileMessage.visibility = View.VISIBLE
        buildingEdit.visibility = View.GONE
        cancelButton.visibility = View.GONE
    }

    private fun editMode() {
        updateButton.visibility = View.VISIBLE
        editButton.visibility = View.GONE
        building.visibility = View.GONE
        profileMessage.visibility = View.GONE
        buildingEdit.visibility = View.VISIBLE
        cancelButton.visibility = View.VISIBLE
    }

    fun setUserData(_userdata: UserModel?, _buildingName: String?) {
        userData = _userdata!!
        buildingName = _buildingName!!
    }

    fun addListener(_listener: IUpdateData) {
        listener = _listener
    }

    private fun removeUnused(): List<BuildingModel> {
        val newBuildings = mutableListOf<BuildingModel>()
        for(building in buildings) {
            if(building.name != null) {
                newBuildings.add(building)
            }
        }
        return newBuildings
    }

    private fun getBuildings() {
        val getAllBuildingsCall: Call<List<BuildingModel>> = ApiClient.getService().getAllBuildings()

        getAllBuildingsCall.enqueue(object : Callback<List<BuildingModel>> {
            override fun onResponse(
                call: Call<List<BuildingModel>>,
                response: Response<List<BuildingModel>>
            ) {
                buildings = if(response.isSuccessful) {
                    response.body()!!
                } else {
                    listOf()
                }
            }

            override fun onFailure(call: Call<List<BuildingModel>>, t: Throwable) {
                Toast.makeText(context, "Failure", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun updateBuilding() {
        val tokenAuth: String
        if (LoggedInUser.getTokenForAuthentication() != null) {
            tokenAuth = LoggedInUser.getTokenForAuthentication()!!
        } else {
            Toast.makeText(context, "Authentication went wrong", Toast.LENGTH_SHORT).show()
            return
        }

        val postBuildingCall : Call<UserModel> = ApiClient.getService().addBuildingToUser(tokenAuth,
                                                                        LoggedInUser.username!!,
                                                                        selectedBuilding!!.id)
        postBuildingCall.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if(response.isSuccessful) {
                    building.text = selectedBuilding!!.name
                    listener.updateData(selectedBuilding!!.name!!)

                    profileMessage.text = "Updated successfully"
                    profileMessage.setTextColor(resources.getColor(R.color.white))
                    Handler().postDelayed({
                        profileMessage.text = ""
                    }, 3000)

                } else {
                    profileMessage.text = "Something went wrong"
                    profileMessage.setTextColor(resources.getColor(R.color.red))
                    Handler().postDelayed({
                        profileMessage.text = ""
                    }, 3000)
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(context, "Failure", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selectedBuilding = buildings[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}