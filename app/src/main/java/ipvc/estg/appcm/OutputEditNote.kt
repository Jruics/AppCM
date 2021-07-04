package ipvc.estg.appcm

data class OutputEditNote(
    val title: String,
    val latitude: String,
    val longitude: String,
    val description: String,
    val category: String,
    val photo: String,
    val user_id: String,
    val status: String,
    val statusMessage: String
)