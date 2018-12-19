package movie.android.com.microsltchallenge.feature.movielist.ui.dialog

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_edit_movie.*
import movie.android.com.microsltchallenge.R
import movie.android.com.microsltchallenge.model.Movie
import movie.android.com.microsltchallenge.model.NewMovie
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.toast

class EditMovieDialog(private val movie: Movie) {

    private var callback: ((String, NewMovie) -> Unit)? = null

    fun editCallback(callback: (String, NewMovie) -> Unit) = apply {
        this.callback = callback
    }

    fun show(context: Context) {
        val view = context.layoutInflater.inflate(R.layout.dialog_edit_movie, null)

        view.findViewById<EditText>(R.id.titleEditText).setText(movie.title, TextView.BufferType.EDITABLE)
        view.findViewById<EditText>(R.id.yearEditText).setText(movie.year.toString(), TextView.BufferType.EDITABLE)
        view.findViewById<EditText>(R.id.ratingEditText).setText(movie.rating.toString(), TextView.BufferType.EDITABLE)
        view.findViewById<EditText>(R.id.genreEditText).setText(movie.genres, TextView.BufferType.EDITABLE)
        view.findViewById<EditText>(R.id.summaryEditText).setText(movie.summary, TextView.BufferType.EDITABLE)
        view.findViewById<EditText>(R.id.urlEditText).setText(movie.thumbnailUrl, TextView.BufferType.EDITABLE)

        val editDialog = AlertDialog.Builder(context)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                if (dialog is AlertDialog) {
                    val title = dialog.titleEditText.text.toString()
                    val year = dialog.yearEditText.text.toString().toIntOrNull() ?: 0
                    val rating = dialog.ratingEditText.text.toString().toIntOrNull() ?: 0
                    val genre = dialog.genreEditText.text.toString()
                    val summary = dialog.summaryEditText.text.toString()
                    val thumbnail = dialog.urlEditText.text.toString()

                    if (title.isBlank() || year == 0 || rating == 0 || genre.isBlank() || summary.isBlank() || thumbnail.isBlank()) {
                        context.toast("Incomplete details")
                    } else {
                        val newMovie = NewMovie(title, summary, thumbnail, genre, year, rating)
                        callback?.invoke(movie.id, newMovie)
                    }
                }
            }
            .setCancelable(false)
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            .setView(view)
            .create()

        editDialog.show()
    }

}