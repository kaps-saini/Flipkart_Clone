package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.ImageSliderAdapter
import com.example.flipkartclone.databinding.FragmentImageViewrBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageViewr : Fragment() {

    private var _binding: FragmentImageViewrBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager2: ViewPager2
    private val navArgs by navArgs<ImageViewrArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_viewr, container, false)

        val adapter = ImageSliderAdapter(navArgs.ImageList.item.images) { position ->

        }

        viewPager2 = binding.vpImageViewer
        viewPager2.adapter = adapter
        binding.indicatorImages.setViewPager(viewPager2)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}