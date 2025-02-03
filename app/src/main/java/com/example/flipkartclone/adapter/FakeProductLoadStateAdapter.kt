package com.example.flipkartclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flipkartclone.databinding.PagingLoadStateBinding

class FakeProductLoadStateAdapter(
   // private val retry: () -> Unit,
    private val loading:(Boolean) ->Unit
):LoadStateAdapter<FakeProductLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(private val binding: PagingLoadStateBinding):
        RecyclerView.ViewHolder(binding.root){

            fun bind(loadState:LoadState){
               // binding.progressBarPaging.isVisible = loadState is LoadState.Loading
                binding.retryButton.isVisible = loadState is LoadState.Error
                //binding.retryButton.setOnClickListener { retry() }
            }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
        if (loadState is LoadState.Loading){
            loading(true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = PagingLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // Set layout params to span the entire width
        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
        return LoadStateViewHolder(binding)
    }
}