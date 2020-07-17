package com.dimi.themoviedb.ui.main.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.api.ApiConstants.Companion.SMALL_IMAGE_URL_PREFIX
import com.dimi.themoviedb.utils.GenericViewHolder
import kotlinx.android.synthetic.main.layout_movie_list_item.view.*

class MovieListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val MOVIE_ITEM = 0
    private val movieMarkerObject = Movie(
        NO_MORE_RESULTS,
        "",
        0.0,
        0,
        "",
        0.0f,
        "",
        "",
        null
    )

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            MovieRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(diffCallback).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {

            NO_MORE_RESULTS -> {
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_no_more_results,
                        parent,
                        false
                    )
                )
            }

            MOVIE_ITEM -> {
                return MovieViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_movie_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                return MovieViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_movie_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
        }
    }

    internal inner class MovieRecyclerChangeCallback(
        private val adapter: MovieListAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    // returns id of movie and if there is -1, meaning no more results, triggers on onCreateViewHolder
    override fun getItemViewType(position: Int): Int {
        if (differ.currentList[position].id > -1) {
            return MOVIE_ITEM
        }
        return differ.currentList[position].id
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Prepare the images that will be displayed in the RecyclerView.
    // This also ensures if the network connection is lost, they will be in the cache
    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<Movie>
    ) {
        for (movie in list) {
            requestManager
                .load(SMALL_IMAGE_URL_PREFIX + movie.posterPath)
                .preload()
        }
    }

    // if query is exhausted and there is no more results
    // creating new dummy object with id -1
    // that id will be triggered in getViewType
    fun submitList(
        movieList: List<Movie>?,
        isQueryExhausted: Boolean
    ) {
        val newList = movieList?.toMutableList()

        if (isQueryExhausted)
            newList?.add(movieMarkerObject)

        val commitCallback = Runnable {
            // if process died must restore list position
            // very annoying
            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class MovieViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            requestManager
                .load(SMALL_IMAGE_URL_PREFIX + item.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.movie_image)
            itemView.movie_title.text = item.title
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Movie)

        fun restoreListPosition()
    }
}