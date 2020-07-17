package com.dimi.themoviedb.ui.main.actors.adapters

import android.text.Layout
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.themoviedb.R
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.models.CombinedCredit
import kotlinx.android.synthetic.main.layout_credit_list_item.view.*

class CreditsListAdapter(

    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CombinedCredit>() {

        // we can check there with actorId its unique enough for DiffUtils in RecyclerViewList
        override fun areItemsTheSame(oldItem: CombinedCredit, newItem: CombinedCredit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CombinedCredit, newItem: CombinedCredit): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CastViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_credit_list_item,
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
        list: List<CombinedCredit>
    ) {
        for (credit in list) {

            if (credit.posterPath != null)
                requestManager
                    .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + credit.posterPath)
                    .preload()
        }
    }

    fun submitList(list: List<CombinedCredit>?) {
        differ.submitList(list)
    }

    class CastViewHolder
    constructor(
        itemView: View,
        private val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CombinedCredit) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            if (item.posterPath != null)
                requestManager
                    .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + item.posterPath)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.movie_image)
            else
                requestManager
                    .load(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .placeholder(R.drawable.default_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.movie_image)
            if( item.isMovie() )
                itemView.movie_title.text = item.title
            else
                itemView.movie_title.text = item.name
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: CombinedCredit)
    }
}