package movie.android.com.microsltchallenge.libs.socket

import com.google.gson.annotations.SerializedName
import movie.android.com.microsltchallenge.model.Movie

data class MovieEvent(@SerializedName("movie") val movie: Movie, @SerializedName("operation") val operation: String) {

    companion object {
        const val UPDATE_OPERATION = "u"
        const val INSERT_OPERATION = "i"
        const val DELETE_OPERATION = "d"
    }
}
