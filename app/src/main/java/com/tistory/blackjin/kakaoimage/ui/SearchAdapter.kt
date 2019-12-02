package com.tistory.blackjin.kakaoimage.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjin.kakaoimage.data.model.SearchResponse
import com.tistory.blackjin.kakaoimage.databinding.ItemImageBinding
import timber.log.Timber


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ImageHolder>() {

    private var items: MutableList<SearchResponse.Document> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageHolder(parent)

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        items[position].let { item ->
            holder.bind(item)
        }
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<SearchResponse.Document>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ImageHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(com.tistory.blackjin.kakaoimage.R.layout.item_image, parent, false)
    ) {

        private val binding: ItemImageBinding = DataBindingUtil.bind(itemView)!!

        //private val placehocafelder = ColorDrawable(Color.GRAY)

        fun bind(item: SearchResponse.Document) {
            binding.run {
                Timber.d(item.imageUrl)
                sdvItemImage.setImageURI(item.imageUrl)
                executePendingBindings()
            }
        }
    }
}