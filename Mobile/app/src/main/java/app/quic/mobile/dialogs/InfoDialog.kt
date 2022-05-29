package app.quic.mobile.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import app.quic.mobile.R

class InfoDialog(var message: String): DialogFragment() {

    private lateinit var messageTextView: TextView
    private lateinit var okButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_info, container, false)

        //Transparent background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        messageTextView = view.findViewById(R.id.info_text)
        okButton = view.findViewById(R.id.ok_button_info)

        messageTextView.text = message

        okButton.setOnClickListener {
            dialog?.dismiss()
        }

        return view
    }
}