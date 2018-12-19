package movie.android.com.microsltchallenge.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("_id") val id: String,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val genres: String,
    val year: Int,
    val rating: Int,
    val isDeleted: Boolean
)


