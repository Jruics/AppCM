package ipvc.estg.appcm

data class Note (
        val id: Int,
        val latitude: Float,
        val longitude: Float,
        val category: String,
        val description: String,
        val photo: String,
        val user_id: Int
)