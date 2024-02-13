package com.example.fmk.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fmk.R
import com.example.fmk.databinding.FragmentUserListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment: Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding: FragmentUserListBinding get() = _binding!!
    private val viewModel: UserListViewModel by viewModels()
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