package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.epoxy.MainEpoxyController
import com.example.flipkartclone.adapter.epoxy.SampleModel
import com.example.flipkartclone.adapter.epoxy.getSampleData
import com.example.flipkartclone.databinding.FragmentCategoriesBinding
import com.example.flipkartclone.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Categories : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_categories, container, false)

        val sampleData = getSampleData()

        val epoxyController = MainEpoxyController()

        binding.epRv.setController(epoxyController)
        binding.epRv.addItemDecoration(DividerItemDecoration(requireActivity(),RecyclerView.VERTICAL))

        epoxyController.sampleData = sampleData

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}