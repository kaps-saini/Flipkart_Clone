package com.example.flipkartclone.utils

import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

class GeneratePdf {

//    fun generatePdfReceipt(paymentDetails: PaymentDetailsResponse?) {
//        if (paymentDetails == null) return
//
//        val pdfDocument = PdfDocument()
//        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
//        val page = pdfDocument.startPage(pageInfo)
//
//        val canvas = page.canvas
//        val paint = Paint()
//
//        // Write receipt details on the PDF
//        paint.textSize = 16f
//        canvas.drawText("Receipt", 80f, 50f, paint)
//
//        paint.textSize = 12f
//        canvas.drawText("Payment ID: ${paymentDetails.id}", 10f, 100f, paint)
//        canvas.drawText("Amount: ${paymentDetails.amount / 100} ${paymentDetails.currency}", 10f, 130f, paint)
//        canvas.drawText("Status: ${paymentDetails.status}", 10f, 160f, paint)
//        canvas.drawText("Date: ${paymentDetails.created_at}", 10f, 190f, paint)
//
//        pdfDocument.finishPage(page)
//
//        // Save PDF to external storage
//        val filePath = Environment.getExternalStorageDirectory().absolutePath + "/Download/receipt.pdf"
//        val file = File(filePath)
//        try {
//            val fileOutputStream = FileOutputStream(file)
//            pdfDocument.writeTo(fileOutputStream)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        pdfDocument.close()
//    }

}