package com.wasilyk.app.apod.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wasilyk.app.apod.databinding.FragmentApodBinding
import com.wasilyk.app.apod.viewmodel.ApodViewModel
import com.wasilyk.app.apod.viewmodel.ApodViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ApodFragment : DaggerFragment() {

    private var _binding: FragmentApodBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var apodViewModelFactory: ApodViewModelFactory
    @Inject
    lateinit var apodsAdapter: ApodsAdapter

    companion object {
        fun newInstance(): Fragment =
            ApodFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentApodBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initApodsRecyclerView()
        subscribeApodsFlow()
    }

    private fun initApodsRecyclerView() {
        binding.apodsRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            adapter = apodsAdapter.withLoadStateHeaderAndFooter(
                header = ApodsLoadStateAdapter { apodsAdapter.retry() },
                footer = ApodsLoadStateAdapter { apodsAdapter.retry() }
            )
            setHasFixedSize(true)
            setApodsAdapterLoadStateListener()
        }
    }

    private fun setApodsAdapterLoadStateListener() {
        apodsAdapter.addLoadStateListener { state: CombinedLoadStates ->
            when(val refreshState = state.refresh) {
                is LoadState.Loading -> {
                    binding.apodsLoadingView.isVisible = true
                    binding.apodsErrorView.isVisible = false
                }
                is LoadState.Error -> {
                    binding.apodsLoadingView.isVisible = false
                    binding.apodsErrorView.isVisible = true
                    binding.apodsErrorTextView.text =
                        refreshState.error.localizedMessage ?: "Unknown error"
                    binding.retryButton.setOnClickListener { apodsAdapter.retry() }
                }
                is LoadState.NotLoading -> {
                    binding.apodsLoadingView.isVisible = false
                    binding.apodsErrorView.isVisible = false
                }
            }
            /*binding.apodsRecyclerView.isVisible = refreshState != LoadState.Loading
            binding.apodsLoadStateView.isVisible = refreshState == LoadState.Loading
            if (refreshState is LoadState.Error) {
                Snackbar.make(
                    binding.root,
                    refreshState.error.localizedMessage ?: "Unknown error",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }*/
        }
    }

    private fun subscribeApodsFlow() {
        val viewModel =
            ViewModelProvider(this, apodViewModelFactory).get(ApodViewModel::class.java)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.apodsFlow.collectLatest { pagingData ->
                apodsAdapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}