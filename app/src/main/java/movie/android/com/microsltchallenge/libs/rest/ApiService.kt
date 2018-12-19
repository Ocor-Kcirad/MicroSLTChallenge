package movie.android.com.microsltchallenge.libs.rest

import io.reactivex.Completable
import io.reactivex.Single
import movie.android.com.microsltchallenge.model.Movie
import movie.android.com.microsltchallenge.model.NewMovie
import retrofit2.http.*

interface ApiService {

    @GET("/movies")
    fun getMovies(): Single<List<Movie>>

    @POST("/movies")
    fun createMovie(@Body movie: NewMovie): Single<Movie>

    @DELETE("/movies/{id}")
    fun deleteMovie(@Path("id") id: String): Completable

    @PATCH("/movies/{id}")
    fun updateMovie(@Path("id") id: String, @Body movie: Movie): Single<Movie>

}