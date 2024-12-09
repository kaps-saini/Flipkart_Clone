package com.example.flipkartclone.utils

import com.razorpay.PaymentData

object PaymentStatus {

    var isPaymentCompleted:((Boolean,PaymentData?) ->Unit)? = null
}