package com.example.flipkartclone.helper

import android.view.View
import com.google.android.material.snackbar.Snackbar

object Helpers {

    fun makeSnackBar(view:View,msg:String){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show()
    }

}