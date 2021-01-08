package com.example.triviaapp.utils

import android.content.Context
import android.widget.Toast

class ViewUtils(val context:Context) {

    companion object{
       public const val API_ERROR_MESSAGE = "Something went wrong ! Please try again later"
    }
    public fun showToast(message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}