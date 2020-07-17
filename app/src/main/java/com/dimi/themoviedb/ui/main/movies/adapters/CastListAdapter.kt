package com.dimi.themoviedb.ui.main.movies.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.Cast
import com.dimi.themoviedb.api.ApiConstants
import kotlinx.android.synthetic.main.layout_cast_list_item.view.*

class CastListAdapter(

    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cast>() {

        // we can check there with actorId its unique enough for DiffUtils in RecyclerViewList
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.actorId == newItem.actorId
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CastViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_cast_list_item,
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
        list: List<Cast>
    ) {
        for (cast in list) {

            if (cast.profile_path != null)
                requestManager
                    .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + cast.profile_path)
                    .preload()
        }
    }

    fun submitList(list: List<Cast>?) {
        differ.submitList(list)
    }

    class CastViewHolder
    constructor(
        itemView: View,
        private val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Cast) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            if (item.profile_path != null)
                requestManager
                    .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + item.profile_path)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.cast_image)
            else
                requestManager
                    .load(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .placeholder(R.drawable.default_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.cast_image)
            itemView.cast_name.text = item.name
            itemView.cast_character.text = String.format("as %s", item.character)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Cast)
    }
}