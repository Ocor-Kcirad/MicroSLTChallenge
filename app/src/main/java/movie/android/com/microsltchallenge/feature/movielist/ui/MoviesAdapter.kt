package movie.android.com.microsltchallenge.feature.movielist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_movie.view.*
import movie.android.com.microsltchallenge.R
import movie.android.com.microsltchallenge.model.Movie

//TODO: Add diffutil
class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private val items = mutableListOf<Movie>()


    fun setItems(items: List<Movie>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int) = ViewHolder.create(viewGroup)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(items[position])


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val requestOptions = RequestOptions().placeholder(R.drawable.image_placeholder)

        companion object {
            fun create(parent: ViewGroup): ViewHolder = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
                .let { ViewHolder(it) }
        }

        fun bind(movie: Movie) {
            Glide.with(itemView).load(movie.thumbnailUrl).apply(requestOptions).into(itemView.thumbnail)
            itemView.titleTextView.text = movie.title
            itemView.genreTextView.text = movie.genres
        }

    }
}