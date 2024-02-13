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
import com.example.fmk.data.entity.User
import com.example.fmk.databinding.FragmentUserDetailBinding
import com.example.fmk.ui.user_list.UserListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailFragment: Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding: FragmentUserDetailBinding get() = _binding!!
    private var user: User? = null
    private val viewModel: UserDetailViewModel by viewModels()
    private val adapter by lazy {
        UserListAdapter {  user ->
            findNavController().navigate(R.id.userDetailFragment, Bundle().apply { putSerializable("User", user) })
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

        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("User", User::class.java)
        } else {
            arguments?.getSerializable("User") as User?
        } ?: return

        binding.backButtonImageView.setOnClickListener { findNavController().popBackStack() }

        val radius = view.context.resources.getDimensionPixelSize(R.dimen.card_corner_radius)
        Glide.with(view.context)
            .load(user?.avatarUrl)
            .transform(RoundedCorners(radius))
            .into(binding.userAvatarImageView)

        val login = user?.login
        if(login != null) {
            setupNestedScroll(login)
            binding.userLoginTextView.text = login
        }

        binding.userNameTextView.text = user?.login
        binding.userCompanyTextView.text = user?.company
        binding.userEmailTextView.text = user?.email
        binding.userBlogTextView.text = user?.blog
        binding.userLocationTextView.text = user?.location

        if (user?.bio != null) {
            binding.userBioTextView.text = user?.bio
            binding.infoTextView.isVisible = true
            binding.userBioTextView.isVisible = true
        } else {
            binding.infoTextView.isVisible = false
            binding.userBioTextView.isVisible = false
        }

        binding.subsUserRecyclerView.adapter = adapter
        binding.subsUserRecyclerView.layoutManager = GridLayoutManager(context, 2)

        user?.login?.also { viewModel.loadFollowerList(it) }
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            viewModel.uiState.collect { handleState(it) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    private fun handleState(state: UserDetailViewState) {
        if (state.error == null) {
            hideError()
            if (!state.loading) {
                hideLoader()
                adapter.submitList(state.followerList)
            } else showLoader()
        } else showError(state.error)
    }

    private fun showLoader() {
        binding.subsUserRecyclerView.isVisible = false
        binding.loader.isVisible = true
    }

    private fun hideLoader() {
        binding.subsUserRecyclerView.isVisible = true
        binding.loader.isVisible = false
    }

    private fun showError(message: String) {
        binding.subsUserRecyclerView.isVisible = false
        binding.error.text = message
        binding.error.isVisible = true
        binding.reloadButton.isVisible = true
    }

    private fun hideError() {
        binding.subsUserRecyclerView.isVisible = true
        binding.error.isVisible = false
        binding.reloadButton.isVisible = false
    }

    private fun setupNestedScroll(login: String) {
        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.nestedScrollView.scrollY
            val followersTextViewY = binding.followersTextView.y

            if (scrollY > followersTextViewY) {
                binding.userLoginTextView.text = getString(R.string.user_detail_followers_title)
            } else {
                binding.userLoginTextView.text = login
            }
        }
    }

}