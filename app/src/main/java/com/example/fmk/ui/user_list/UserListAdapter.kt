package com.example.fmk.ui.user_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.fmk.R
import com.example.fmk.data.entity.User
import com.example.fmk.databinding.ItemUserListBinding

class UserListAdapter(
    private val programItemClickListener: ((User) -> Unit)? = null
): ListAdapter<User, UserListAdapter.UserListViewHolder>(UserItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(
            ItemUserListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val item = getItem(position)

        holder.onBind(item)
        programItemClickListener?.let { listener ->
            holder.itemView.setOnClickListener { listener(item) }
        }
    }

    private class UserItemDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem.login == newItem.login
    }

    class UserListViewHolder(
        val binding: ItemUserListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: User) {
            val context = binding.root.context

            val radius = context.resources.getDimensionPixelSize(R.dimen.card_corner_radius)
            Glide.with(context)
                .load(item.avatarUrl)
                .transform(RoundedCorners(radius))
                .into(binding.avatar)

            binding.login.text = item.login
            binding.followers.text = context.getString(R.string.user_list_item_followers_count, item.followersCount)
            binding.repositories.text = context.getString(R.string.user_list_item_repository_count, item.reposCount)
        }
    }

}