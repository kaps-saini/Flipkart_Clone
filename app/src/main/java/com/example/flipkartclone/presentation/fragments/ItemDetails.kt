package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.compose.ui.window.Dialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.ExploreAdapter
import com.example.flipkartclone.adapter.ImageSliderAdapter
import com.example.flipkartclone.adapter.ProductHighlights
import com.example.flipkartclone.adapter.ProductImageSliderAdapter
import com.example.flipkartclone.adapter.ProductReviews
import com.example.flipkartclone.data.user.UserCartItemsPref
import com.example.flipkartclone.databinding.FragmentItemDetailsBinding
import com.example.flipkartclone.domain.models.Highlight
import com.example.flipkartclone.domain.models.user.CartItems
import com.example.flipkartclone.helper.Helpers
import com.example.flipkartclone.utils.Status
import com.example.flipkartclone.vm.FlipkartCloneViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ItemDetails : Fragment() {

    private var _binding:FragmentItemDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager2: ViewPager2
    private val viewModel by viewModels<FlipkartCloneViewModel>()
    private val navArgs by navArgs<ItemDetailsArgs>()
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var productHighlights: ProductHighlights
    private lateinit var productReviews: ProductReviews

    @Inject
    lateinit var userCartItemsPref:UserCartItemsPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_item_details, container, false)

        val adapter = ProductImageSliderAdapter(navArgs.itemData.item.images) { position ->
            val data = navArgs.itemData
            val action = ItemDetailsDirections.actionItemDetailsToImageViewr(data)
            findNavController().navigate(action)
        }

        viewPager2 = binding.viewPageProductView
        viewPager2.adapter = adapter
        binding.productImageIndicator.setViewPager(viewPager2)
        exploreAdapter = ExploreAdapter(){ position, itemData ->
            Helpers.makeSnackBar(requireView(),itemData.title)
        }

        setupMoreLikeThisRv()
        fetchItemData()
        setupHighlightRv()
        setupReviewsRv()

        productHighlights.differ.submitList(navArgs.itemData.highlights)
        productReviews.differ.submitList(navArgs.itemData.reviews)

        binding.tvHighlightToggle.setOnClickListener {
            val isVisible = binding.rvProductHighlight.isVisible
            binding.rvProductHighlight.isVisible = !isVisible
            val drawableRes = if (isVisible) R.drawable.ic_action_arrow_expand else R.drawable.ic_action_arrow_collaps
            binding.tvHighlightToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0)
        }

        binding.ivBackProduct.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAddToCart.setOnClickListener{
            val itemDetail = navArgs.itemData.item
            val itemPrice = navArgs.itemData.pricing
            val rating = navArgs.itemData.ratings
            if (binding.btnAddToCart.text.contains(Status.GoToCart.toString(),false)){
                findNavController().navigate(R.id.action_itemDetails_to_cart2)
            }else if (binding.btnAddToCart.text.contains(Status.AddToCart.toString(),false)){
                userCartItemsPref.addCartItem(CartItems(itemDetail,itemPrice,rating,1))
                binding.btnAddToCart.text = Status.GoToCart.toString()
                Helpers.makeSnackBar(requireView(),"Added to cart successfully")
            }
        }

        binding.btnBuyNow.setOnClickListener{
            val itemDetail = navArgs.itemData.item
            val itemPrice = navArgs.itemData.pricing
            val rating = navArgs.itemData.ratings
            userCartItemsPref.addCartItem(CartItems(itemDetail,itemPrice,rating,1))
            findNavController().navigate(R.id.action_itemDetails_to_cart2)
        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun fetchItemData(){
        val itemData = navArgs.itemData

        binding.tvProductTitle.text = itemData.item.name
        binding.ratingsProduct.rating = itemData.ratings.average.toFloat()
        binding.tvProductRatings.text = itemData.ratings.average.toString()
        binding.tvProductRatingsCount.text = itemData.ratings.count.toString()
        val discount = itemData.pricing.discount.toString()
        binding.tvProductDiscount.text = "$discount%"
        binding.tvProductMrp.text = "₹${itemData.pricing.mrp}"
        binding.tvProductSellingPrice.text = "₹${itemData.pricing.sellingPrice}"

        val ref = inflateLayout(R.layout.layout_ratings,binding.stubRatingFrame)
        fetRatingData(ref)
    }

    private fun inflateLayout(layout: Int, tabView: FrameLayout): View {
        val viewRef =
            LayoutInflater.from(requireContext()).inflate(layout, tabView, false)
        tabView.removeAllViews()
        tabView.addView(viewRef)
        return viewRef
    }

    private fun fetRatingData(ref:View){
        val ratingStatus = ref.findViewById<TextView>(R.id.tvRateStatusOfProduct)
        val ratingCount = ref.findViewById<TextView>(R.id.tvTotalRatingsAndReview)
        val ratingAverage = ref.findViewById<RatingBar>(R.id.totalRatings)
        val fiveStarRating = ref.findViewById<LinearProgressIndicator>(R.id.li5star)
        val fourStarRating = ref.findViewById<LinearProgressIndicator>(R.id.li4Star)
        val threeStarRating = ref.findViewById<LinearProgressIndicator>(R.id.li3star)
        val twoStarRating = ref.findViewById<LinearProgressIndicator>(R.id.li2star)
        val oneStarRating = ref.findViewById<LinearProgressIndicator>(R.id.li1star)

        val fiveStarCount = ref.findViewById<TextView>(R.id.tv5StarCount)
        val fourStarCount = ref.findViewById<TextView>(R.id.tv4StarCount)
        val threeStarCount = ref.findViewById<TextView>(R.id.tvTotal3StarRating)
        val twoStarCount = ref.findViewById<TextView>(R.id.tvTotal2StarCount)
        val oneStarCount = ref.findViewById<TextView>(R.id.tvTotal1StarRating)

        val circularRating = ref.findViewById<LinearLayout>(R.id.linearLayout5)

        val ci1 = ref.findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator)
        val ci2 = ref.findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator2)
        val ci3 = ref.findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator3)
        val ci4 = ref.findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator4)
        val ci5 = ref.findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator5)

        val ciRatingAverage1 = ref.findViewById<TextView>(R.id.tvCi1Rating)
        val ciRatingAverage2 = ref.findViewById<TextView>(R.id.tvCi2Rating)
        val ciRatingAverage3 = ref.findViewById<TextView>(R.id.tvCi3Rating)
        val ciRatingAverage4 = ref.findViewById<TextView>(R.id.tvCi4Rating)
        val ciRatingAverage5 = ref.findViewById<TextView>(R.id.tvCi5Rating)

        ratingStatus.text = "Good"
        val ratingCounts = navArgs.itemData.ratings.count.toString()
        ratingCount.text = "$ratingCounts Ratings"
        ratingAverage.rating = navArgs.itemData.ratings.average.toFloat()

        val rateCountInInt = navArgs.itemData.ratings.count.toInt()
        val fiveStarCountInt = navArgs.itemData.ratings.distribution.`5star`
        val fourStarCountInt = navArgs.itemData.ratings.distribution.`4star`
        val threeStarCountInt = navArgs.itemData.ratings.distribution.`3star`
        val twoStarCountInt = navArgs.itemData.ratings.distribution.`2star`
        val oneStarCountInt = navArgs.itemData.ratings.distribution.`1star`
        val fiveStarInPercent = getRatingPercent(rateCountInInt,fiveStarCountInt)
        val fourStarInPercent = getRatingPercent(rateCountInInt,fourStarCountInt)
        val threeStarInPercent = getRatingPercent(rateCountInInt,threeStarCountInt)
        val twoStarInPercent = getRatingPercent(rateCountInInt,twoStarCountInt)
        val oneStarInPercent = getRatingPercent(rateCountInInt,oneStarCountInt)

        fiveStarRating.progress = fiveStarInPercent
        fourStarRating.progress = fourStarInPercent
        threeStarRating.progress = threeStarInPercent
        twoStarRating.progress = twoStarInPercent
        oneStarRating.progress = oneStarInPercent

        Log.i("ratingCountItem", fiveStarInPercent.toString())

        fiveStarCount.text = fiveStarCountInt.toString()
        fourStarCount.text = fourStarCountInt.toString()
        threeStarCount.text = threeStarCountInt.toString()
        twoStarCount.text = twoStarCountInt.toString()
        oneStarCount.text = oneStarCountInt.toString()


        ci1.progress = 60
        ci2.progress = 20
        ci3.progress = 80
        ci4.progress = 40
        ci5.progress = 50

        if (navArgs.itemData.ratings.details == null){
            circularRating.visibility = View.GONE
        }else{
            circularRating.visibility = View.VISIBLE
            ciRatingAverage1.text = navArgs.itemData.ratings.details?.camera.toString()
            ciRatingAverage2.text = navArgs.itemData.ratings.details?.battery.toString()
            ciRatingAverage3.text = navArgs.itemData.ratings.details?.performance.toString()
            ciRatingAverage4.text = navArgs.itemData.ratings.details?.valueForMoney.toString()
            ciRatingAverage5.text = navArgs.itemData.ratings.details?.display.toString()
        }
    }

    private fun getRatingPercent(total:Int,ratingCount:Int):Int{
        return ratingCount/total * 100
    }

    private fun setupMoreLikeThisRv(){
        binding.rvMoreLikeThis.apply {
            this.adapter = exploreAdapter
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupHighlightRv(){
        productHighlights = ProductHighlights()
        binding.rvProductHighlight.apply {
            this.adapter = productHighlights
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun setupReviewsRv(){
        productReviews = ProductReviews()
        binding.rvUserReviews.apply {
            this.adapter = productReviews
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }


}