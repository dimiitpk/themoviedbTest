package com.dimi.themoviedb.ui.main.actors.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.api.ApiConstants.Companion.SMALL_IMAGE_URL_PREFIX
import com.dimi.themoviedb.utils.GenericViewHolder
import kotlinx.android.synthetic.main.layout_movie_list_item.view.*

class ActorListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val ACTOR_ITEM = 0
    private val actorMarkerObject = Actor(
        NO_MORE_RESULTS,
        0,
        "",
        "",
        "",
        "",
        0.0f,
        "",
        null
    )

    private val diffCallback = object : DiffUtil.ItemCallback<Actor>() {

        override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            ActorRecyclerChangeCallback(this),
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

            ACTOR_ITEM -> {
                return ActorViewHolder(
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
                return ActorViewHolder(
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

    internal inner class ActorRecyclerChangeCallback(
        private val adapter: ActorListAdapter
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
            is ActorViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    // returns id of actor and if there is -1, meaning no more results, triggers on onCreateViewHolder
    override fun getItemViewType(position: Int): Int {
        if (differ.currentList[position].id > -1) {
            return ACTOR_ITEM
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
        list: List<Actor>
    ) {
        for (actor in list) {
            requestManager
                .load(SMALL_IMAGE_URL_PREFIX + actor.profilePath)
                .preload()
        }
    }

    // if query is exhausted and there is no more results
    // creating new dummy object with id -1
    // that id will be triggered in getViewType
    fun submitList(
        actorList: List<Actor>?,
        isQueryExhausted: Boolean
    ) {
        val newList = actorList?.toMutableList()

        if (isQueryExhausted)
            newList?.add(actorMarkerObject)

        val commitCallback = Runnable {
            // if process died must restore list position
            // very annoying
            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class ActorViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Actor) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            requestManager
                .load(SMALL_IMAGE_URL_PREFIX + item.profilePath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.movie_image)
            itemView.movie_title.text = item.name
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Actor)

        fun restoreListPosition()
    }
}