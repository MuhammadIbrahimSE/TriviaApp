package com.example.rickandmorty.data.remote

import com.example.triviaapp.interfaces.ApiService
import com.example.triviaapp.model.QuestionAndAnswers
import com.example.triviaapp.model.QuestionAnswersResponseModel
import com.example.triviaapp.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.http.Query
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


@ExperimentalCoroutinesApi
class QuestionsRemoteDataSource @Inject constructor(
    private val characterService: ApiService
) {
    suspend fun getQuestions(
        amount: Int,
        category: Int,
        difficulty: String,
        type: String
    ): Flow<Resource<QuestionAnswersResponseModel>> = callbackFlow {
        var responseCame = false
        characterService.getAllQuestions(
            amount,
            category,
            difficulty,
            type
        ).enqueue((object : Callback<QuestionAnswersResponseModel> {
            override fun onResponse(
                call: Call<QuestionAnswersResponseModel>,
                response: Response<QuestionAnswersResponseModel>
            ) {
                if (response.isSuccessful) {
                    responseCame = true
                    offer(
                        Resource.Success(
                            response.body()!!
                        )
                    )
                }
            }

            override fun onFailure(call: Call<QuestionAnswersResponseModel>, t: Throwable) {
                responseCame = false
            }
        }))
        if (!responseCame)
            offer(
                Resource.Failure(
                    Exception("")
                )
            )

        awaitClose { close() }
    }


}