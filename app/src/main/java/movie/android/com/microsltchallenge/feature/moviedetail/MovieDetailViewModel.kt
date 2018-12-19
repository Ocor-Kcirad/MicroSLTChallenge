package movie.android.com.microsltchallenge.feature.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import movie.android.com.microsltchallenge.model.Movie

//TODO: Implement d.i
class MovieDetailViewModel : ViewModel() {

    val movie: LiveData<Movie> get() = movieLiveData

    private val movieLiveData: MediatorLiveData<Movie> = MediatorLiveData()

    fun loadMovie(id: String) {

    }
}