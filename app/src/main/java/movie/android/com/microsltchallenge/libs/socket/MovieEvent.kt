package movie.android.com.microsltchallenge.libs.socket

import movie.android.com.microsltchallenge.model.Movie

data class MovieEvent(val o: Movie, val op: String) {

    companion object {
        const val UPDATE_OPERATION = "u"
        const val INSERT_OPERATION = "i"
        const val DELETE_OPERATION = "d"
    }
}
