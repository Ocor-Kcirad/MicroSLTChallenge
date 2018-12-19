package movie.android.com.microsltchallenge.feature.movielist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_movie.view.*
import movie.android.com.microsltchallenge.R
import movie.android.com.microsltchallenge.model.Movie

class MoviesAdapter(private val callback: Delegate) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private val items = mutableListOf<Movie>()

    interface Delegate {
        fun onClickMovie(movie: Movie)
        fun onDeleteMovie(movie: Movie)
        fun onEditMovie(movie: Movie)
    }

    fun setItems(newItems: List<Movie>) {
        val result = DiffUtil.calculateDiff(DiffCallback(items, newItems))
        items.clear()
        items.addAll(newItems)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int) = ViewHolder.create(viewGroup, callback)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(items[position])

    class DiffCallback(private val oldList: List<Movie>, private val newList: List<Movie>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

    }

    class ViewHolder(itemView: View, private val callback: Delegate) : RecyclerView.ViewHolder(itemView) {

        private val requestOptions = RequestOptions().placeholder(R.drawable.image_placeholder)
        private var movie: Movie? = null

        companion object {
            fun create(parent: ViewGroup, callback: Delegate): ViewHolder = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
                .let { ViewHolder(it, callback) }
        }

        init {
            itemView.thumbnail.setOnClickListener {
                movie?.let { movie -> callback.onClickMovie(movie) }
            }

            itemView.thumbnail.setOnLongClickListener {
                movie?.let { movie -> callback.onDeleteMovie(movie) }
                true
            }
            itemView.editButton.setOnClickListener {
                movie?.let { movie -> callback.onEditMovie(movie) }
            }
        }

        fun bind(movie: Movie) {
            this.movie = movie
            Glide.with(itemView).load(movie.thumbnailUrl).apply(requestOptions).into(itemView.thumbnail)
            itemView.titleTextView.text = movie.title
            itemView.genreTextView.text = movie.genres
        }

    }
}