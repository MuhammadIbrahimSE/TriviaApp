package com.example.triviaapp.model

data class QuestionAnswersResponseModel(
    val response_code: Int,
    val results: List<QuestionAndAnswers>
)
