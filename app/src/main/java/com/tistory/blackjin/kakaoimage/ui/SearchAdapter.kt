package com.tistory.blackjin.kakaoimage.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjin.kakaoimage.R
import com.tistory.blackjin.kakaoimage.data.model.Document
import com.tistory.blackjin.kakaoimage.databinding.ItemImageBinding
import com.tistory.blackjin.kakaoimage.ui.detail.DetailActivity

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ImageHolder>() {

    private var items: MutableList<Document> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImageHolder(parent).apply {
            itemView.setOnClickListener {

                val item = items[adapterPosition]
                val aspect = item.width.toFloat() / item.height.toFloat()
                DetailActivity.startDetailActivity(
                    parent.context, item.imageUrl, aspect
                )
            }
        }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        items[position].let { item ->
            holder.bind(item)
        }
    }

    override fun getItemCount() = items.size

    fun replaceAll(items: List<Document>) {
        val diffCallback = DocumentDiffCallback(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    class ImageHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
    ) {

        private val binding: ItemImageBinding = DataBindingUtil.bind(itemView)!!

        fun bind(item: Document) {
            binding.run {

                val aspect = item.width.toFloat() / item.height.toFloat()
                sdvItemImage.aspectRatio = aspect
                sdvItemImage.setImageURI(item.imageUrl)

            }
        }
    }

    class DocumentDiffCallback(
        private val mOldEmployeeList: List<Document>,
        private val mNewEmployeeList: List<Document>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return mOldEmployeeList.size
        }

        override fun getNewListSize(): Int {
            return mNewEmployeeList.size
        }

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return mOldEmployeeList[oldItemPosition] === mNewEmployeeList[newItemPosition]
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return mOldEmployeeList[oldItemPosition] === mNewEmployeeList[newItemPosition]
        }

    }
}