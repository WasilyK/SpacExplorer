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
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.wasilyk.app.apod.databinding.FragmentApodBinding
import com.wasilyk.app.apod.model.entitities.Apod
import com.wasilyk.app.apod.viewmodel.ApodViewModel
import com.wasilyk.app.apod.viewmodel.ApodViewModelFactory
import com.wasilyk.app.core.Error
import com.wasilyk.app.core.Loading
import com.wasilyk.app.core.Success
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject

class ApodFragment : DaggerFragment() {

    private var _binding: FragmentApodBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var apodViewModelFactory: ApodViewModelFactory

    @Inject
    lateinit var apodsAdapter: ApodsAdapter

    private val viewModel: ApodViewModel by lazy {
        ViewModelProvider(this, apodViewModelFactory).get(ApodViewModel::class.java)
    }

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
        subscribeApodFlow()
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
            when (val refreshState = state.refresh) {
                is LoadState.Loading -> {
                    binding.apply {
                        apodsLoadingView.isVisible = true
                        apodsErrorView.isVisible = false
                        apodsRecyclerView.isVisible = false
                    }
                }
                is LoadState.Error -> {
                    binding.apply {
                        apodsLoadingView.isVisible = false
                        apodsErrorView.isVisible = true
                        apodsRecyclerView.isVisible = false
                        apodsErrorTextView.text =
                            refreshState.error.localizedMessage ?: "Unknown error"
                        retryButton.setOnClickListener { apodsAdapter.retry() }
                    }
                }
                is LoadState.NotLoading -> {
                    binding.apply {
                        apodsLoadingView.isVisible = false
                        apodsErrorView.isVisible = false
                        apodsRecyclerView.isVisible = true
                    }
                }
            }
        }
    }

    private fun subscribeApodsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.apodsFlow
                .collectLatest { pagingData ->
                    apodsAdapter.submitData(pagingData)
                }
        }
    }

    private fun subscribeApodFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.apodFlow
                .collectLatest { appState ->
                    when (appState) {
                        is Loading -> {
                            binding.apply {
                                apodLoadingView.isVisible = true
                                apodErrorView.isVisible = false
                            }
                        }
                        is Success<*> -> {
                            val apod = (appState as Success<List<Apod>>).data[0] as Apod
                            binding.apply {
                                apodLoadingView.isVisible = false
                                apodErrorView.isVisible = false
                                imageView.load(apod.url)
                                titleView.text = apod.title
                                dateView.text = apod.date
                                explanationView.text = apod.explanation
                                copyrightView.text = apod.copyright
                            }

                        }
                        is Error -> {
                            binding.apply {
                                apodLoadingView.isVisible = false
                                apodErrorView.isVisible = true
                                apodsErrorTextView.text =
                                    appState.throwable.localizedMessage
                                        ?: "Unknown message"
                                apodRetryButton.setOnClickListener {}
                            }
                        }
                    }

                }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}