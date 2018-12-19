package movie.android.com.microsltchallenge.feature.moviedetail.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.content_movie_detail.*
import movie.android.com.microsltchallenge.R
import movie.android.com.microsltchallenge.feature.moviedetail.MovieDetailViewModel
import movie.android.com.microsltchallenge.model.Movie

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_MOVIE = "movie.android.com.microsltchallenge.feature.moviedetail.ui.movie"
    }

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders
            .of(this, MovieDetailViewModel.ViewModelFactory(intent))
            .get(MovieDetailViewModel::class.java)

        viewModel.movie.observe(this, Observer { updateView(it) })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateView(movie: Movie) {
        titleTextView.text = movie.title
        genreTextView.text = movie.genres
        yearTextView.text = movie.year.toString()
        ratingTextView.text = movie.rating.toString()
        summaryTextView.text = movie.summary
        val options = RequestOptions().placeholder(R.drawable.image_placeholder)
        Glide.with(this).load(movie.thumbnailUrl).apply(options).into(thumbnailImageView)
    }
}
