package com.example.flipkartclone.helper

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Helpers {

    fun makeSnackBar(view:View,msg:String){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show()
    }

    fun showDeleteAccountDialog(context: Context, callback:()->Unit){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Account Deletion")
        builder.setMessage("Are you sure you want to delete your account? This can't be undone")

        builder.setPositiveButton("Delete"){ dialog,_->
            callback()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel"){ dialog,_->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()  // Get current date and time
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")  // Define the format
        return currentDateTime.format(formatter)  // Return the formatted date and time
    }

}