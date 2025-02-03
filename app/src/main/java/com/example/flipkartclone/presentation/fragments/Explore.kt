package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.CategoriesAdapter
import com.example.flipkartclone.adapter.ExploreAdapter
import com.example.flipkartclone.adapter.FakeProductAdapter
import com.example.flipkartclone.adapter.FakeProductLoadStateAdapter
import com.example.flipkartclone.databinding.FragmentExploreBinding
import com.example.flipkartclone.helper.Helpers
import com.example.flipkartclone.utils.CheckNetwork
import com.example.flipkartclone.utils.Resource
import com.example.flipkartclone.utils.Status
import com.example.flipkartclone.vm.FlipkartCloneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.exp

@AndroidEntryPoint
class Explore : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FlipkartCloneViewModel>()
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var fakeProductAdapter: FakeProductAdapter
    private lateinit var fakeProductLoadStateAdapter :FakeProductLoadStateAdapter
    @Inject
    lateinit var network: CheckNetwork

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_explore, container, false)

        exploreAdapter = ExploreAdapter{ position, itemData ->
            Helpers.makeSnackBar(requireView(),itemData.title)
        }

        fakeProductAdapter = FakeProductAdapter()
        fakeProductLoadStateAdapter = FakeProductLoadStateAdapter {
            showPagingLoading()
        }
        setupExploreRv()

        if (network.hasInternetConnection(requireContext())){
            lifecycleScope.launch {
                viewModel.fakeProducts.collectLatest { pagingData ->
                    fakeProductAdapter.submitData(pagingData)
                }
            }

            lifecycleScope.launch {
                fakeProductAdapter.loadStateFlow.collectLatest { loadState ->
                    val isLoading = loadState.refresh is LoadState.Loading
                    val isError = loadState.refresh is LoadState.Error
                    binding.progressBar2.visibility = View.GONE
                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    binding.viewNoInternet.visibility = if (isError) View.VISIBLE else View.GONE

                    if (isError) {
                        Helpers.makeSnackBar(requireView(), "Some error occurred!")
                    }
                }
            }
        }else{
            hideProgressBar()
            binding.viewNoInternet.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun showPagingLoading() {
        binding.progressBar2.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun setupExploreRv() {
        binding.rvExplore.apply {
            val gridLayoutManager = GridLayoutManager(context, 2)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter?.getItemViewType(position)) {
                        R.layout.paging_load_state -> 2 // Span the entire width for the load state footer
                        else -> 1 // Normal item span
                    }
                }
            }
            layoutManager = gridLayoutManager
            adapter = fakeProductAdapter.withLoadStateFooter(fakeProductLoadStateAdapter)
        }
    }


    private fun showProgressBar(){
        binding.rvExplore.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.rvExplore.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

}