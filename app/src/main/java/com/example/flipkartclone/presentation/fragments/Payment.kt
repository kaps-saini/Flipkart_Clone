package com.example.flipkartclone.presentation.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.RecentlyViewedItems
import com.example.flipkartclone.adapter.RecommendedItems
import com.example.flipkartclone.data.user.OrderedItemPref
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.databinding.FragmentPaymentBinding
import com.example.flipkartclone.domain.models.Item
import com.example.flipkartclone.domain.models.Ratings
import com.example.flipkartclone.helper.Helpers
import com.example.flipkartclone.utils.Resource
import com.example.flipkartclone.utils.Status
import com.example.flipkartclone.vm.FlipkartCloneViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.OutputStream
import javax.inject.Inject

@AndroidEntryPoint
class Payment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userOrderedItemPref: OrderedItemPref
    @Inject
    lateinit var userDetailsPref: UserDetailsPref
    private val viewModel by viewModels<FlipkartCloneViewModel>()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var recommendedItems: RecommendedItems
    private val args by navArgs<PaymentArgs>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
        fetchRecommendedItems()
        userEvents()
    }

    private fun fetchRecommendedItems() {
        recommendedItems = RecommendedItems(
            onClick = {position, itemData ->
                val action = PaymentDirections.actionPaymentToItemDetails(itemData)
                findNavController().navigate(action)
            }
        )

        binding.rvPayment.apply {
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            this.adapter = recommendedItems
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.droppedItems.collect { response ->
                    when (response) {
                        is Resource.Error -> {
                           // showRecentlyViewPB()
                            //binding.appbar.visibility = View.GONE
                            if (response.message?.contains(Status.NoInternet.toString()) == true){
                                Helpers.makeSnackBar(requireView(),"No Internet")
                            }else{
                                Helpers.makeSnackBar(requireView(), response.message.toString())
                                Log.e("dashboard",response.message.toString())
                            }
                        }
                        is Resource.Loading -> {
                            //showRecentlyViewPB()
                        }
                        is Resource.Success -> {
                           // showRecentlyViewRv()
                            recommendedItems.differ.submitList(response.data)
                        }
                    }
                }
            }
        }
    }

    private fun fetchData() {
        binding.tvTitle.text = args.OrderedItem.item?.name
        binding.tvItemDesc.text = args.OrderedItem.item?.description
        binding.tvListPrice.text = "₹${args.OrderedItem.price.toString()}"
        binding.tvDiscountPricePayment.text = "₹${args.OrderedItem.discount.toString()}"
        binding.tvSellingPricePayment.text = "₹${args.OrderedItem.price.toString()}"
        binding.tvTotalAmountPayment.text = "₹${args.OrderedItem.billingAmount.toString()}"
        binding.tvOrderId.text = "Order ID - ${args.OrderedItem.orderId}"
        binding.tvPrice.text = "₹${args.OrderedItem.price.toString()}"
        binding.tvAddressNamePayment.text = auth.currentUser?.displayName.toString()
        binding.tvAddressDetails.text = args.OrderedItem.userAddress
        Glide.with(requireContext()).load(args.OrderedItem.item?.images?.get(0)).into(binding.ivOrderImage)
    }

    private fun userEvents() {
        binding.ivBackPayment.setOnClickListener {
            findNavController().navigate(R.id.action_payment_to_dashboard)
        }

        binding.tvDownloadInvoice.setOnClickListener {
            with(args.OrderedItem) {
                generateAndShowInvoicePdf(requireContext(),
                    orderId,paymentId,item,price,ratings,quantity,billingAmount,discount,userAddress,userContact)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }


    private fun generateAndShowInvoicePdf(
        context: Context,
        orderId: String,
        paymentId: String,
        item: Item?,
        price: Int,
        ratings: Ratings?,
        quantity: Int,
        billingAmount: Int,
        discount: Int,
        userAddress: String,
        userContact: String
    ): Boolean {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.textSize = 12f

        var yPosition = 20

        // Title
        paint.textSize = 16f
        paint.isFakeBoldText = true
        canvas.drawText("Invoice", 100f, yPosition.toFloat(), paint)
        paint.isFakeBoldText = false
        yPosition += 20

        // Order Details
        canvas.drawText("Order ID: $orderId", 10f, yPosition.toFloat(), paint)
        yPosition += 20
        canvas.drawText("Payment ID: $paymentId", 10f, yPosition.toFloat(), paint)
        yPosition += 20

        // Item Details
        canvas.drawText("Item: ${item?.name ?: "N/A"}", 10f, yPosition.toFloat(), paint)
        yPosition += 20
        canvas.drawText("Price: ₹$price", 10f, yPosition.toFloat(), paint)
        yPosition += 20
        canvas.drawText("Quantity: $quantity", 10f, yPosition.toFloat(), paint)
        yPosition += 20
        canvas.drawText("Total Price: ₹${price * quantity}", 10f, yPosition.toFloat(), paint)
        yPosition += 20
        canvas.drawText("Discount: ₹$discount", 10f, yPosition.toFloat(), paint)
        yPosition += 20
        canvas.drawText("Billing Amount: ₹$billingAmount", 10f, yPosition.toFloat(), paint)
        yPosition += 20

        // User Details
        canvas.drawText("User Address: $userAddress", 10f, yPosition.toFloat(), paint)
        yPosition += 20
        canvas.drawText("Contact: $userContact", 10f, yPosition.toFloat(), paint)
        yPosition += 20

        pdfDocument.finishPage(page)

        // Save and show the PDF
        return try {
            val fileName = "Invoice_$orderId.pdf"
            val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentResolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                }
                val fileUri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                fileUri?.let { uri ->
                    contentResolver.openOutputStream(uri)?.use {
                        pdfDocument.writeTo(it)
                    }
                }
                fileUri
            } else {
                val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val file = File(directory, fileName)
                file.outputStream().use {
                    pdfDocument.writeTo(it)
                }
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
            }

            pdfDocument.close()

            // Show the PDF
            uri?.let {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(it, "application/pdf")
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(intent)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            false
        }
    }

}