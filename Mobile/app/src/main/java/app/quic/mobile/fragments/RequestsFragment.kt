package app.quic.mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.quic.mobile.R
import app.quic.mobile.activities.MainActivity

class RequestsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_requests, container, false)

        val toolbarTitle = (activity as MainActivity).toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Requests"

        return view
    }

}