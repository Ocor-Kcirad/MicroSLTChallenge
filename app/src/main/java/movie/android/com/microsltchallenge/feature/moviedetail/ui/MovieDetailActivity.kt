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

        viewModel.movie.observe(this, Observer {
            titleTextView.text = it.title
            genreTextView.text = it.genres
            yearTextView.text = it.year.toString()
            ratingTextView.text = it.rating.toString()
            summaryTextView.text = it.summary
            val options = RequestOptions().placeholder(R.drawable.image_placeholder)
            Glide.with(this).load(it.thumbnailUrl).apply(options).into(thumbnailImageView)
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
