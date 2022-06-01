package app.quic.mobile.models

import app.quic.mobile.services.LoggedInUser
import java.util.*

class RequestModel (var id: Long, var username: String, var description: String, var roomNumber: String, var cause: String, var category: String, var priority: String, var status: String, var dateTime: String) {
    fun getRequestStatus(): String? {
        return status
    }
}