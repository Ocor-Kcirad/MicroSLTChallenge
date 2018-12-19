package movie.android.com.microsltchallenge.feature.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import movie.android.com.microsltchallenge.libs.arch.ActionLiveData
import movie.android.com.microsltchallenge.libs.rest.ApiClient
import movie.android.com.microsltchallenge.libs.socket.MovieEvent
import movie.android.com.microsltchallenge.libs.socket.MoviesLiveData
import movie.android.com.microsltchallenge.model.Movie

//TODO: Implement d.i
class MovieListViewModel : ViewModel() {

    val movies: LiveData<List<Movie>> get() = moviesLiveData
    val errors: LiveData<Throwable> get() = errorsLiveData

    private val moviesLiveData: MediatorLiveData<List<Movie>> = MediatorLiveData()
    private val moviesSocketLiveData = MoviesLiveData()
    private val errorsLiveData = ActionLiveData<Throwable>()
    private val disposables = CompositeDisposable()

    init {
        moviesLiveData.addSource(moviesSocketLiveData) { event ->
            val target = event.movie
            val items = moviesLiveData.value?.toMutableList() ?: mutableListOf()
            when (event.operation) {
                MovieEvent.UPDATE_OPERATION -> {
                    val index = items.indexOfFirst { target.id == it.id }
                    items[index] = target

                }
                MovieEvent.INSERT_OPERATION -> {
                    items.add(target)
                }
                MovieEvent.DELETE_OPERATION -> {
                    val index = items.indexOfFirst { target.id == it.id }
                    items.removeAt(index)
                }
            }
            moviesLiveData.value = items
        }
    }

    fun loadMovies() {
        disposables.add(ApiClient
            .service
            .getMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {

                    moviesLiveData.value = it
                },
                { errorsLiveData.value = it }
            ))
    }

    fun createMovie(movie: Movie) {
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

    fun deleteMovie(id: String) {
        disposables.add(ApiClient
            .service
            .deleteMovie(id)
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


    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}