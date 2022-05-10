package app.quic.mobile.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import app.quic.mobile.R
import app.quic.mobile.models.UserModel
import app.quic.mobile.services.ApiClient
import app.quic.mobile.services.LoggedInUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.file.attribute.UserDefinedFileAttributeView

class ProfileDialog : DialogFragment() {

    private lateinit var role: TextView
    private lateinit var username: TextView
    private lateinit var email: EditText
    private lateinit var building: EditText
    private lateinit var updateButton: Button

    private lateinit var userData: UserModel

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

        updateButton.setOnClickListener {
            Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun initUi(view: View) {
        role = view.findViewById(R.id.profile_role)
        username = view.findViewById(R.id.profile_username)
        email = view.findViewById(R.id.profile_email)
        building = view.findViewById(R.id.profile_building)
        updateButton = view.findViewById(R.id.profile_update)
    }

    private fun updateUi() {
        role.text = LoggedInUser.getUserRole()
        username.text = LoggedInUser.username
        email.setText(userData.email, TextView.BufferType.EDITABLE)
        building.setText(userData.buildingId.toString(), TextView.BufferType.EDITABLE)
    }

    fun setUserData(_userdata: UserModel?) {
        userData = _userdata!!
    }
}