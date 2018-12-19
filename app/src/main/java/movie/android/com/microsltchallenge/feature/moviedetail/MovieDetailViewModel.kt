package movie.android.com.microsltchallenge.feature.moviedetail

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import movie.android.com.microsltchallenge.feature.moviedetail.ui.MovieDetailActivity
import movie.android.com.microsltchallenge.libs.arch.ActionLiveData
import movie.android.com.microsltchallenge.libs.rest.ApiClient
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
            val movie = intent.getStringExtra(MovieDetailActivity.KEY_MOVIE_ID)
            if (instance is MovieDetailViewModel) {
                instance.loadMovie(movie)
            }
            return instance
        }
    }

    val movie: LiveData<Movie> get() = movieLiveData

    private val movieLiveData: MediatorLiveData<Movie> = MediatorLiveData()
    private val movieEventLiveData = MovieEventLiveData()
    private val errorsLiveData = ActionLiveData<Throwable>()
    private val disposables = CompositeDisposable()

    init {
        movieLiveData.addSource(movieEventLiveData) { event ->
            when (event.operation) {
                MovieEvent.UPDATE_OPERATION -> updateMovieItem(event)
                MovieEvent.INSERT_OPERATION -> Unit
                MovieEvent.DELETE_OPERATION -> Unit
            }
        }

    }

    fun loadMovie(id: String) {
        disposables.add(ApiClient
            .service
            .getMovie(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { },
                { errorsLiveData.value = it }
            ))
    }

    private fun updateMovieItem(movieEvent: MovieEvent) {
        movieLiveData.postValue(movieEvent.movie)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        movieEventLiveData.disconnect()
    }
}