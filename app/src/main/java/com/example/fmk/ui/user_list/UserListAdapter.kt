package com.example.fmk.ui.user_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.fmk.R
import com.example.fmk.data.entity.User
import com.example.fmk.databinding.ItemUserListBinding
import com.example.fmk.utils.UserDetailsDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListAdapter(
    private val userDetailsDownloader: UserDetailsDownloader<UserListViewHolder>,
    private val programItemClickListener: ((User) -> Unit) = {}
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

        CoroutineScope(Dispatchers.Main).launch {
            userDetailsDownloader.queueThumbnail(holder, item.login)
        }

        holder.itemView.setOnClickListener { programItemClickListener(item) }
    }

    private class UserItemDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem.login == newItem.login
                    && oldItem.followersCount == newItem.followersCount
                    && oldItem.reposCount == newItem.reposCount
    }

    class UserListViewHolder(
        val binding: ItemUserListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: User) {
            binding.run {
                val context = root.context

                val radius = context.resources.getDimensionPixelSize(R.dimen.card_corner_radius)
                Glide.with(root.context)
                    .load(item.avatarUrl)
                    .transform(RoundedCorners(radius))
                    .into(avatar)

                login.text = item.login

                if (item.hasDetails())
                    showDetails(
                        followersField = context.getString(R.string.user_list_item_followers_count, item.followersCount),
                        reposField = context.getString(R.string.user_list_item_repository_count, item.reposCount),
                    )
                else showLoadingMessage()
            }
        }

        fun showLoadingMessage() {
            binding.run {
                loadStateLabel.text = "Подождите"
                loadStateLabel.isVisible = true
                followers.isVisible = false
                repositories.isVisible = false
            }
        }

        fun showErrorMessage() {
            binding.run {
                loadStateLabel.text = "Ошибка"
                loadStateLabel.isVisible = true
                followers.isVisible = false
                repositories.isVisible = false
            }
        }

        fun showDetails(followersField: String, reposField: String) {
            binding.run {
                loadStateLabel.isVisible = false
                followers.text = followersField
                repositories.text = reposField
                followers.isVisible = true
                repositories.isVisible = true
            }
        }
    }

}