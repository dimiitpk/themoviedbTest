package com.dimi.themoviedb.ui.main.tv_show.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.themoviedb.R
import com.dimi.themoviedb.api.ApiConstants.Companion.SMALL_IMAGE_URL_PREFIX
import com.dimi.themoviedb.models.TVShow
import com.dimi.themoviedb.utils.GenericViewHolder
import kotlinx.android.synthetic.main.layout_movie_list_item.view.*

class TvShowListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val TV_SHOW_ITEM = 0
    private val tvShowMarkerObject = TVShow(
        NO_MORE_RESULTS,
        "",
        0,
        0,
        0.0,
        0,
        "",
        0.0f,
        "",
        "",
        "",
        null
    )

    private val diffCallback = object : DiffUtil.ItemCallback<TVShow>() {

        override fun areItemsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
            return oldItem == newItem
        }
    }
    private val differ =
        AsyncListDiffer(
            TVShowRecyclerChangeCallback(this),
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

            TV_SHOW_ITEM -> {
                return TvShowViewHolder(
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
                return TvShowViewHolder(
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

    internal inner class TVShowRecyclerChangeCallback(
        private val adapter: TvShowListAdapter
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
            is TvShowViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    // returns id of tvShow and if there is -1, meaning no more results, triggers on onCreateViewHolder
    override fun getItemViewType(position: Int): Int {
        if (differ.currentList[position].id > -1) {
            return TV_SHOW_ITEM
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
        list: List<TVShow>
    ) {
        for (tvShow in list) {
            requestManager
                .load(SMALL_IMAGE_URL_PREFIX + tvShow.posterPath)
                .preload()
        }
    }

    // if query is exhausted and there is no more results
    // creating new dummy object with id -1
    // that id will be triggered in getViewType
    fun submitList(
        tvShowList: List<TVShow>?,
        isQueryExhausted: Boolean
    ) {
        val newList = tvShowList?.toMutableList()

        if (isQueryExhausted)
            newList?.add(tvShowMarkerObject)

        val commitCallback = Runnable {
            // if process died must restore list position
            // very annoying
            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class TvShowViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: TVShow) = with(itemView) {
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

        fun onItemSelected(position: Int, item: TVShow)

        fun restoreListPosition()
    }
}