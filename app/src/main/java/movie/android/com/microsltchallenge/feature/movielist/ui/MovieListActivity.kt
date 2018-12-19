package movie.android.com.microsltchallenge.feature.movielist.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.android.synthetic.main.content_movie_list.*
import kotlinx.android.synthetic.main.dialog_create_movie.*
import movie.android.com.microsltchallenge.R
import movie.android.com.microsltchallenge.feature.moviedetail.ui.MovieDetailActivity
import movie.android.com.microsltchallenge.feature.movielist.MovieListViewModel
import movie.android.com.microsltchallenge.model.Movie
import movie.android.com.microsltchallenge.model.NewMovie
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class MovieListActivity : AppCompatActivity(), SearchView.OnQueryTextListener, MoviesAdapter.Delegate {

    private lateinit var viewModel: MovieListViewModel
    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        setSupportActionBar(toolbar)
        setupRecyclerView()
        setupFab()
        viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
        viewModel.movies.observe(this, Observer { adapter.setItems(it) })
        viewModel.errors.observe(this, Observer { toast("Something went wrong") })
        viewModel.loadMovies()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_list, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.filterMovies(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.filterMovies(newText)
        return true
    }

    override fun onClickMovie(movie: Movie) {
        val intent = intentFor<MovieDetailActivity>(MovieDetailActivity.KEY_MOVIE to movie)
        startActivity(intent)
    }

    override fun onDeleteMovie(movie: Movie) {
        AlertDialog.Builder(this)
            .setTitle("Delete Movie?")
            .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.deleteMovie(movie) }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            .setCancelable(true)
            .show()
    }

    private fun setupRecyclerView() {
        adapter = MoviesAdapter(this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }

    private fun setupFab() {
        fab.setOnClickListener {
            AlertDialog.Builder(this)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    if (dialog is AlertDialog) {
                        val title = dialog.titleEditText.text.toString()
                        val year = dialog.yearEditText.text.toString().toInt()
                        val rating = dialog.ratingEditText.text.toString().toInt()
                        val genre = dialog.genreEditText.text.toString()
                        val summary = dialog.summaryEditText.text.toString()
                        val thumbnail = dialog.urlEditText.text.toString()
                        val newMovie = NewMovie(title, summary, thumbnail, genre, year, rating)
                        viewModel.createMovie(newMovie)
                    }
                }
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
                .setView(R.layout.dialog_create_movie)
                .show()
        }
    }

}
