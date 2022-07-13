package com.example.mystoryapp.ui.story.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mystoryapp.api.response.ListStoryItem
import com.example.mystoryapp.databinding.RvItemRowsBinding

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(RvItemRowsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val t = getItem(position)
        if (t != null) holder.bind(t)
        
//        holder.binding.rvRowname.text = storyList[position].name
//        holder.binding.rvRowdate.text = storyList[position].createdAt.toString().substring(0,10)

//        Glide.with(holder.binding.root.context)
//            .load(storyList[position].photoUrl)
//            .into(holder.binding.rvRowimagebanner)

//        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(storyList[holder.adapterPosition])}
    }
    
//    override fun getItemCount(): Int = storyList.size

    fun notifyDatasetChangedHelper(){
        notifyDataSetChanged()
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    class ListViewHolder(var binding: RvItemRowsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: ListStoryItem) {
            binding.rvRowname.text = data.name
            binding.rvRowdate.text = data.createdAt
            

            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(binding.root.context)
                .load(data.photoUrl)
                .apply(requestOptions)
                .into(binding.rvRowimagebanner)
        }
    }
    
}