package com.example.triviaapp.interfaces

import com.example.triviaapp.model.QuestionAndAnswers
import com.example.triviaapp.model.QuestionAnswersResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("api.php")
    fun getAllQuestions(@Query("amount") amount:Int,
                           @Query("category") category:Int,
                           @Query("difficulty") difficulty:String,
                           @Query("type") type:String): Call<QuestionAnswersResponseModel>

}