package movie.android.com.microsltchallenge.feature.movielist.ui.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_create_movie.*
import movie.android.com.microsltchallenge.R
import movie.android.com.microsltchallenge.model.NewMovie
import org.jetbrains.anko.toast

class CreateMovieDialog {

    private var callback: ((NewMovie) -> Unit)? = null

    fun createCallback(callback: (NewMovie) -> Unit) = apply {
        this.callback = callback
    }

    fun show(context: Context) {
        val createDialog = AlertDialog.Builder(context)
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
                        callback?.invoke(newMovie)
                    }
                }
            }
            .setCancelable(false)
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            .setView(R.layout.dialog_create_movie)
            .create()

        createDialog.show()
    }
}