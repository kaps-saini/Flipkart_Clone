package com.example.flipkartclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AuthBottomSheet : BottomSheetDialogFragment() {

//    private var _binding:FragmentAuthBottomSheetBinding? = null
//    private val binding get() = _binding!!

    private lateinit var flBtn:FrameLayout
    private lateinit var scrollView:ScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_auth_bottom_sheet, container, false)

        flBtn = view.findViewById(R.id.flBtn)
        scrollView = view.findViewById(R.id.scrollView)

        //return binding.root
        return  view.rootView
    }

    override fun onStart() {
        super.onStart()

        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        flBtn.post {
            val flBtnHeight = flBtn.height

            // Calculate half of the remaining height
            val remainingHeight = (screenHeight - flBtnHeight) / 1.4

            // Set half of the remaining height as the top margin
            val layoutParams = flBtn.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = remainingHeight.toInt()
            flBtn.layoutParams = layoutParams

            // Scroll to the button position
            scrollView.smoothScrollTo(0, flBtn.top)
        }
    }



}