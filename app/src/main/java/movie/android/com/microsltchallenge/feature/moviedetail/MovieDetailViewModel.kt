package movie.android.com.microsltchallenge.feature.moviedetail

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import movie.android.com.microsltchallenge.feature.moviedetail.ui.MovieDetailActivity
import movie.android.com.microsltchallenge.libs.socket.MovieEvent
import movie.android.com.microsltchallenge.libs.socket.MovieEventLiveData
import movie.android.com.microsltchallenge.model.Movie

//TODO: Implement d.i
class MovieDetailViewModel : ViewModel() {

    class ViewModelFactory(private val intent: Intent) :
        ViewModelProvider.Factory {
        private val instanceFactory = ViewModelProvider.NewInstanceFactory()
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val instance = instanceFactory.create(modelClass)
            val movie = intent.getParcelableExtra<Movie>(MovieDetailActivity.KEY_MOVIE)
            if (instance is MovieDetailViewModel) {
                instance.initialize(movie)
            }
            return instance
        }
    }

    val movie: LiveData<Movie> get() = movieLiveData

    private val movieLiveData: MediatorLiveData<Movie> = MediatorLiveData()
    private val movieEventLiveData = MovieEventLiveData()

    init {
        movieLiveData.addSource(movieEventLiveData) { event ->
            when (event.operation) {
                MovieEvent.UPDATE_OPERATION -> updateMovieItem(event)
                MovieEvent.INSERT_OPERATION -> Unit
                MovieEvent.DELETE_OPERATION -> Unit
            }
        }

    }

    fun initialize(movie: Movie) = movieLiveData.postValue(movie)

    private fun updateMovieItem(movieEvent: MovieEvent) {
        movieLiveData.postValue(movieEvent.movie)
    }


}