package com.dimi.themoviedb.ui.main.tv_show.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.themoviedb.R
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.models.TVEpisode
import kotlinx.android.synthetic.main.layout_episode_list_item.view.*

class TvEpisodeListAdapter(

    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TVEpisode>() {

        // we can check there with actorId its unique enough for DiffUtils in RecyclerViewList
        override fun areItemsTheSame(oldItem: TVEpisode, newItem: TVEpisode): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TVEpisode, newItem: TVEpisode): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CastViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_episode_list_item,
                parent,
                false
            ),
            requestManager,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CastViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<TVEpisode>
    ) {
        for (episode in list) {

            if (episode.stillPath != null)
                requestManager
                    .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + episode.stillPath)
                    .preload()
        }
    }

    fun submitList(list: List<TVEpisode>?) {
        differ.submitList(list)
    }

    class CastViewHolder
    constructor(
        itemView: View,
        private val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: TVEpisode) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }


            if (item.stillPath != null)
                requestManager
                    .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + item.stillPath)
                    .placeholder(R.drawable.default_episode_image)
                    .error(R.drawable.default_episode_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.episode_image)
            else
                requestManager
                    .load(R.drawable.default_episode_image)
                    .error(R.drawable.default_episode_image)
                    .placeholder(R.drawable.default_episode_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.episode_image)
            itemView.episode_number_and_name.text = String.format("%d. %s", item.episodeNumber, item.name)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: TVEpisode)
    }
}