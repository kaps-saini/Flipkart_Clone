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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.CategoriesAdapter
import com.example.flipkartclone.adapter.ExploreAdapter
import com.example.flipkartclone.databinding.FragmentExploreBinding
import com.example.flipkartclone.helper.Helpers
import com.example.flipkartclone.utils.Resource
import com.example.flipkartclone.utils.Status
import com.example.flipkartclone.vm.FlipkartCloneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.exp

@AndroidEntryPoint
class Explore : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FlipkartCloneViewModel>()
    private lateinit var exploreAdapter: ExploreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_explore, container, false)

        exploreAdapter = ExploreAdapter{ position, itemData ->
            Helpers.makeSnackBar(requireView(),itemData.title)
        }

        setupExploreRv()

        viewModel.getAllItems()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.itemResult.collect{ response->
                    when(response){
                        is Resource.Error ->{
                            hideProgressBar()
                            if (response.message?.contains(Status.NoInternet.toString()) == true){
                                hideProgressBar()
                            }else{
                                Helpers.makeSnackBar(requireView(), response.message.toString())
                            }
                        }
                        is Resource.Loading -> {
                            showProgressBar()
                        }
                        is Resource.Success -> {
                            hideProgressBar()
                            exploreAdapter.differ.submitList(response.data)
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun setupExploreRv() {
        binding.rvExplore.apply {
            this.adapter = exploreAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2,LinearLayoutManager.VERTICAL,false)
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