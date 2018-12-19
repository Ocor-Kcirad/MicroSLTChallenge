package movie.android.com.microsltchallenge.feature.movielist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.android.synthetic.main.content_movie_list.*
import movie.android.com.microsltchallenge.R
import movie.android.com.microsltchallenge.feature.movielist.MovieListViewModel
import org.jetbrains.anko.toast

class MovieListActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieListViewModel
    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        setSupportActionBar(toolbar)
        setupRecyclerView()

        viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
        viewModel.movies.observe(this, Observer { adapter.setItems(it) })
        viewModel.errors.observe(this, Observer { toast("Something went wrong") })
        viewModel.loadMovies()
    }

    private fun setupRecyclerView() {
        adapter = MoviesAdapter()
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }

}
