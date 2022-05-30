package app.quic.mobile.models

class CategoryModel (var id: Long, var name: String) {
    override fun toString(): String {
        return name
    }
}