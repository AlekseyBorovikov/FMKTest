package com.example.fmk.ui.user_list

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
import com.example.fmk.R
import com.example.fmk.data.entity.LoadUserDetailsState
import com.example.fmk.databinding.FragmentUserListBinding
import com.example.fmk.utils.UserDetailsDownloader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserListFragment: Fragment() {

    @Inject lateinit var userDetailsDownloader: UserDetailsDownloader<UserListAdapter.UserListViewHolder>

    private var _binding: FragmentUserListBinding? = null
    private val binding: FragmentUserListBinding get() = _binding!!
    private val viewModel: UserListViewModel by viewModels()
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
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userRecycler.adapter = adapter
        binding.userRecycler.layoutManager = GridLayoutManager(context, 2)
        binding.swipeContainer.setOnRefreshListener { viewModel.loadUserList() }
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



    private fun handleState(state: UserListViewState) {
        if (state.error == null) {
            hideError()
            if (!state.loading) {
                adapter.submitList(state.userList)
            }
            binding.swipeContainer.isRefreshing = state.loading
        } else {
            showError(state.error)
        }
    }

    private fun showError(message: String) {
        binding.error.text = message
        binding.error.isVisible = true
        binding.reloadButton.isVisible = true
    }

    private fun hideError() {
        binding.error.isVisible = false
        binding.reloadButton.isVisible = false
    }
}