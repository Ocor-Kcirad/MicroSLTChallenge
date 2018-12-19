package movie.android.com.microsltchallenge.feature.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import movie.android.com.microsltchallenge.libs.arch.ActionLiveData
import movie.android.com.microsltchallenge.libs.arch.combine
import movie.android.com.microsltchallenge.libs.rest.ApiClient
import movie.android.com.microsltchallenge.libs.socket.MovieEvent
import movie.android.com.microsltchallenge.libs.socket.MovieEventLiveData
import movie.android.com.microsltchallenge.model.Movie
import movie.android.com.microsltchallenge.model.NewMovie

//TODO: Implement d.i
class MovieListViewModel : ViewModel() {

    companion object {
        private const val FILTER_EMPTY = ""
    }

    val errors: LiveData<Throwable> get() = errorsLiveData
    val movies: LiveData<List<Movie>>
        get() = moviesLiveData.combine<List<Movie>, String, List<Movie>>(
            filterLiveData,
            ::applyFilter
        )

    private val moviesLiveData: MediatorLiveData<List<Movie>> = MediatorLiveData()
    private val movieEventLiveData = MovieEventLiveData()
    private val filterLiveData = MutableLiveData<String>()
    private val errorsLiveData = ActionLiveData<Throwable>()
    private val disposables = CompositeDisposable()

    init {
        moviesLiveData.addSource(movieEventLiveData) { event ->
            when (event.operation) {
                MovieEvent.UPDATE_OPERATION -> updateMovieItem(event)
                MovieEvent.INSERT_OPERATION -> insertMovieItem(event)
                MovieEvent.DELETE_OPERATION -> deleteMovieItem(event)
            }
        }

        filterLiveData.value = FILTER_EMPTY
    }

    fun loadMovies() {
        disposables.add(ApiClient
            .service
            .getMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { moviesLiveData.value = it },
                { errorsLiveData.value = it }
            ))
    }

    fun filterMovies(query: String?) = filterLiveData.postValue(query)

    fun createMovie(movie: NewMovie) {
        disposables.add(ApiClient
            .service
            .createMovie(movie)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { },
                { errorsLiveData.value = it }
            ))
    }

    fun deleteMovie(movie: Movie) {
        disposables.add(ApiClient
            .service
            .deleteMovie(movie.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { },
                { errorsLiveData.value = it }
            ))
    }

    fun updateMovie(movie: Movie) {
        disposables.add(ApiClient
            .service
            .updateMovie(movie.id, movie)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { },
                { errorsLiveData.value = it }
            ))
    }

    private fun updateMovieItem(movieEvent: MovieEvent) {
        val target = movieEvent.movie
        val items = moviesLiveData.value?.toMutableList() ?: mutableListOf()
        val index = items.indexOfFirst { target.id == it.id }
        items[index] = target
        moviesLiveData.value = items
    }

    private fun insertMovieItem(movieEvent: MovieEvent) {
        val target = movieEvent.movie
        val items = moviesLiveData.value?.toMutableList() ?: mutableListOf()
        items.add(target)
        moviesLiveData.value = items
    }

    private fun deleteMovieItem(movieEvent: MovieEvent) {
        val target = movieEvent.movie
        val items = moviesLiveData.value?.toMutableList() ?: mutableListOf()
        val index = items.indexOfFirst { target.id == it.id }
        items.removeAt(index)
        moviesLiveData.value = items
    }

    private fun applyFilter(list: List<Movie>?, filter: String?): List<Movie> = list
        ?.filter { movie ->
            filter?.let { movie.title.contains(it, true) || movie.genres.contains(filter, true) } ?: true
        }?.toList() ?: emptyList()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}