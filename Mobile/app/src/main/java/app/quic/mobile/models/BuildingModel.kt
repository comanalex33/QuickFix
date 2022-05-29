package app.quic.mobile.models

class BuildingModel(var id: Long, var name: String?) {
    override fun toString(): String {
        return name!!
    }
}