package com.example.fmk.ui.user_detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.fmk.R
import com.example.fmk.data.entity.LoadUserDetailsState
import com.example.fmk.data.entity.User
import com.example.fmk.databinding.FragmentUserDetailBinding
import com.example.fmk.ui.user_list.UserListAdapter
import com.example.fmk.utils.UserDetailsDownloader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailFragment: Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding: FragmentUserDetailBinding get() = _binding!!
    @Inject
    lateinit var userDetailsDownloader: UserDetailsDownloader<UserListAdapter.UserListViewHolder>

    private val viewModel: UserDetailViewModel by viewModels()
    private val adapter by lazy {
        UserListAdapter(userDetailsDownloader) {  user ->
            findNavController().navigate(R.id.userDetailFragment, Bundle().apply { putSerializable("User", user) })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(userDetailsDownloader)
        userDetailsDownloader.setListener { holder, state ->
            when(state) {
                is LoadUserDetailsState.Error -> holder.showErrorMessage()
                is LoadUserDetailsState.Loaded -> holder.onBind(state.user)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("User", User::class.java)
        } else {
            arguments?.getSerializable("User") as User?
        } ?: return

        binding.backButtonImageView.setOnClickListener { findNavController().popBackStack() }

        val radius = view.context.resources.getDimensionPixelSize(R.dimen.card_corner_radius)
        Glide.with(view.context)
            .load(user.avatarUrl)
            .transform(RoundedCorners(radius))
            .into(binding.userAvatarImageView)

        setupNestedScroll(user.login)
        binding.loginTitle.text = user.login
        binding.userName.text = user.login

        viewModel.setUser(user)
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            viewModel.uiState.collect { handleState(it) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(userDetailsDownloader)
        _binding = null
    }



    private fun handleState(state: UserDetailViewState) {
        if (state.error == null) {
            hideError()
            if (!state.loading) {
                hideLoader()
                if (state.user != null) showUserDetails(state.user)
                adapter.submitList(state.followerList)
            } else showLoader()
        } else showError(state.error)
    }

    private fun showUserDetails(user: User) {
        binding.run {
            company.text = user.company
            email.text = user.email
            blog.text = user.blog
            location.text = user.location

            if (user.bio != null) {
                bio.text = user.bio
                infoLabel.isVisible = true
                bio.isVisible = true
            } else {
                infoLabel.isVisible = false
                bio.isVisible = false
            }

            followersRecyclerView.adapter = adapter
            followersRecyclerView.layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun showLoader() {
        binding.followersRecyclerView.isVisible = false
        binding.loader.isVisible = true
    }

    private fun hideLoader() {
        binding.followersRecyclerView.isVisible = true
        binding.loader.isVisible = false
    }

    private fun showError(message: String) {
        binding.followersRecyclerView.isVisible = false
        binding.error.text = message
        binding.error.isVisible = true
        binding.reloadButton.isVisible = true
    }

    private fun hideError() {
        binding.followersRecyclerView.isVisible = true
        binding.error.isVisible = false
        binding.reloadButton.isVisible = false
    }

    private fun setupNestedScroll(login: String) {
        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.nestedScrollView.scrollY
            val followersTextViewY = binding.followersLabel.y

            if (scrollY > followersTextViewY) {
                binding.loginTitle.text = getString(R.string.user_detail_followers_title)
            } else {
                binding.loginTitle.text = login
            }
        }
    }

}