package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.BrandsForYouController
import com.example.flipkartclone.adapter.CategoriesAdapter
import com.example.flipkartclone.adapter.GiftingAdapter
import com.example.flipkartclone.adapter.ImageSliderAdapter
import com.example.flipkartclone.adapter.JustDroppedDeals
import com.example.flipkartclone.adapter.SponsoredAdapter
import com.example.flipkartclone.databinding.FragmentDashboardBinding
import com.example.flipkartclone.helper.Helpers
import com.example.flipkartclone.domain.models.CategoriesDataModel
import com.example.flipkartclone.utils.Resource
import com.example.flipkartclone.utils.Status
import com.example.flipkartclone.vm.FlipkartCloneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Dashboard : Fragment() {

    private var _binding:FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var sliderRunnable: Runnable
    private lateinit var viewPager2: ViewPager2
    private var sliderHandler = Handler(Looper.getMainLooper())
    private val viewModel by viewModels<FlipkartCloneViewModel>()

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var brandsForYouController: BrandsForYouController
    private lateinit var giftingAdapter: GiftingAdapter
    private lateinit var justDroppedDeals: JustDroppedDeals
    private lateinit var sponsoredAdapter: SponsoredAdapter

    var rvState :Parcelable? = null

    private val imageList = listOf(
        "https://firebasestorage.googleapis.com/v0/b/taskmanager-6f187.appspot.com/o/images%2F4878cb0e-18be-45cb-bf23-e48f99af8d6c.jpg?alt=media&token=4dca47e0-145c-4e58-ba2e-b5463ca95ba4",
        "https://firebasestorage.googleapis.com/v0/b/taskmanager-6f187.appspot.com/o/images%2F4878cb0e-18be-45cb-bf23-e48f99af8d6c.jpg?alt=media&token=4dca47e0-145c-4e58-ba2e-b5463ca95ba4",
        "https://firebasestorage.googleapis.com/v0/b/taskmanager-6f187.appspot.com/o/images%2F4878cb0e-18be-45cb-bf23-e48f99af8d6c.jpg?alt=media&token=4dca47e0-145c-4e58-ba2e-b5463ca95ba4",
    )

    private val categoriesList = listOf(
        CategoriesDataModel(R.drawable.ic_launcher_background,"Mobiles"),
        CategoriesDataModel(R.drawable.ic_launcher_background,"Electronics"),
        CategoriesDataModel(R.drawable.ic_launcher_background,"Home Essentials"),
        CategoriesDataModel(R.drawable.ic_launcher_background,"Furniture"),
        CategoriesDataModel(R.drawable.ic_launcher_background,"Flights"),
        CategoriesDataModel(R.drawable.ic_launcher_background,"TVs"),
        CategoriesDataModel(R.drawable.ic_launcher_background,"Grocery"),
        CategoriesDataModel(R.drawable.ic_launcher_background,"Fashion"),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_dashboard, container, false)

        val adapter = ImageSliderAdapter(imageList) { position ->
            onImageClick(position)
        }

        viewPager2 = binding.viewPager2
        viewPager2.adapter = adapter
        binding.indicator.setViewPager(viewPager2)

        sliderRunnable = Runnable {
            val nextSlide = (viewPager2.currentItem + 1) % imageList.size
            viewPager2.currentItem = nextSlide
        }

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable,3000)
            }
        })

        categoriesAdapter = CategoriesAdapter(categoriesList){ click,data ->
            Helpers.makeSnackBar(requireView(),data)
        }

        val brandsForYouController = BrandsForYouController{ data->
            Helpers.makeSnackBar(requireView(),data.title)
        }

        binding.rvBrandsForYou.setController(brandsForYouController)
        binding.rvBrandsForYou.addItemDecoration(DividerItemDecoration(requireActivity(),RecyclerView.VERTICAL))

        giftingAdapter = GiftingAdapter(){ position, itemData ->
            Helpers.makeSnackBar(requireView(),itemData.title)
        }

        justDroppedDeals = JustDroppedDeals{ position, itemData ->
            val action = DashboardDirections.actionDashboardToItemDetails(itemData)
           findNavController().navigate(action)
        }

        sponsoredAdapter = SponsoredAdapter{ position, itemData ->
            Helpers.makeSnackBar(requireView(),itemData.title)
        }

        rvState = savedInstanceState?.getParcelable("rvState")

        setupCategoryRv()
        //setupBrandsForYouRv()
        setupGiftingRv()
        setupJustDroppedDealsRv()
        setupSponsoredRv()

        viewModel.getAllItems()
        viewLifecycleOwner.lifecycleScope.launch {
           // viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.itemResult.collect { response ->
                    when (response) {
                        is Resource.Error -> {
                            hideProgressBar()
                            binding.appbar.visibility = View.GONE
                            if (response.message?.contains(Status.NoInternet.toString()) == true){
                                binding.viewNoInternet.visibility = View.VISIBLE
                                binding.btnRetry.setOnClickListener {
                                    viewModel.getAllItems()
                                }
                            }else{
                                Helpers.makeSnackBar(requireView(), response.message.toString())
                            }
                        }
                        is Resource.Loading -> {
                            showProgressBar()
                        }
                        is Resource.Success -> {
                            binding.viewNoInternet.visibility = View.GONE
                            binding.appbar.visibility = View.VISIBLE
                            hideProgressBar()
                            brandsForYouController.brandsData = response.data!!
                           // brandsForYouAdapter.differ.submitList(response.data)
                            giftingAdapter.differ.submitList(response.data?.subList(0, 4))
                            sponsoredAdapter.differ.submitList(response.data)
                        }
                    }
                }
           // }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.droppedItems.collect { response ->
                    when (response) {
                        is Resource.Error -> {
                            hideProgressBar()
                            binding.appbar.visibility = View.GONE
                            if (response.message?.contains(Status.NoInternet.toString()) == true){
                                binding.viewNoInternet.visibility = View.VISIBLE
                                binding.btnRetry.setOnClickListener {
                                    viewModel.getAllItems()
                                }
                            }else{
                                Helpers.makeSnackBar(requireView(), response.message.toString())
                                Log.e("dashboard",response.message.toString())
                            }
                        }
                        is Resource.Loading -> {
                            showProgressBar()
                        }
                        is Resource.Success -> {
                            binding.viewNoInternet.visibility = View.GONE
                            binding.appbar.visibility = View.VISIBLE
                            hideProgressBar()
                            justDroppedDeals.differ.submitList(response.data)
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun onImageClick(position: Int) {
        Helpers.makeSnackBar(requireView(),"clicked")
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable,3000)
       binding.rvBrandsForYou.layoutManager?.onRestoreInstanceState(rvState)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
        rvState = binding.rvBrandsForYou.layoutManager?.onSaveInstanceState()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("rvState",rvState)
    }

    private fun setupCategoryRv(){
        binding.rvCategory.apply {
            this.adapter = categoriesAdapter
            this.layoutManager = LinearLayoutManager(
                requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

//    private fun setupBrandsForYouRv(){
//        binding.rvBrandsForYou.apply {
//            this.adapter = brandsForYouAdapter
//            this.layoutManager = LinearLayoutManager(
//                requireContext(),LinearLayoutManager.HORIZONTAL,false)
//        }
//    }

    private fun setupGiftingRv(){
        binding.rvGifting.apply {
            this.adapter = giftingAdapter
            this.layoutManager = GridLayoutManager(requireContext(),2)
        }
    }

    private fun setupJustDroppedDealsRv(){
        binding.rvJustDropped.apply {
            this.adapter = justDroppedDeals
            this.layoutManager = LinearLayoutManager(
                requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupSponsoredRv(){
        binding.rvSponsored.apply {
            this.adapter = sponsoredAdapter
            this.layoutManager = LinearLayoutManager(
                requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun showProgressBar(){
        binding.nestedScrollView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.nestedScrollView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

}