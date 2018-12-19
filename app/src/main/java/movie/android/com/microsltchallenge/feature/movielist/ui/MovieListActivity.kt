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
import movie.android.com.microsltchallenge.R
import movie.android.com.microsltchallenge.feature.moviedetail.ui.MovieDetailActivity
import movie.android.com.microsltchallenge.feature.movielist.MovieListViewModel
import movie.android.com.microsltchallenge.feature.movielist.ui.dialog.CreateMovieDialog
import movie.android.com.microsltchallenge.feature.movielist.ui.dialog.EditMovieDialog
import movie.android.com.microsltchallenge.model.Movie
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
        showHint()

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
        val intent = intentFor<MovieDetailActivity>(MovieDetailActivity.KEY_MOVIE_ID to movie.id)
        startActivity(intent)
    }

    override fun onDeleteMovie(movie: Movie) {
        AlertDialog.Builder(this)
            .setTitle("Delete the entry `${movie.title}` ?")
            .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.deleteMovie(movie) }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            .setCancelable(true)
            .show()
    }

    override fun onEditMovie(movie: Movie) {
        EditMovieDialog(movie)
            .editCallback(viewModel::updateMovie)
            .show(this)
    }

    private fun setupRecyclerView() {
        adapter = MoviesAdapter(this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }

    private fun setupFab() {
        fab.setOnClickListener {
            CreateMovieDialog()
                .createCallback(viewModel::createMovie)
                .show(this)
        }
    }

    private fun showHint() {
        AlertDialog.Builder(this)
            .setTitle("Hint")
            .setMessage("Long press to delete an entry \n")
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .setCancelable(true)
            .show()
    }

}
