package com.example.triviaapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.triviaapp.interfaces.ApiInterface
import com.example.triviaapp.interfaces.QuestionsAndAnswersDao
import com.example.triviaapp.model.QuestionAndAnswers
import com.example.triviaapp.model.QuestionAnswersResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val questionsAndAnswersDao: QuestionsAndAnswersDao
) {


    private var allQuestionDataSet = MutableLiveData<List<QuestionAndAnswers>>()

    fun getAllQuestions(
        amount: Int,
        category: Int,
        difficulty: String,
        type: String
    ): LiveData<List<QuestionAndAnswers>> = liveData(Dispatchers.IO) {
        var isResponseCame = false
        apiInterface.getAllQuestions(amount, category, difficulty, type).enqueue(object :
            Callback<QuestionAnswersResponseModel> {
            override fun onResponse(
                call: Call<QuestionAnswersResponseModel>,
                response: Response<QuestionAnswersResponseModel>
            ) {
                if (response.isSuccessful) {

                    isResponseCame = true
                    allQuestionDataSet.postValue( response.body()!!.results)
                } else {
                    allQuestionDataSet =
                        questionsAndAnswersDao.getAllRecord() as MutableLiveData<List<QuestionAndAnswers>>
                }

            }

            override fun onFailure(call: Call<QuestionAnswersResponseModel>, t: Throwable) {
                allQuestionDataSet =
                    questionsAndAnswersDao.getAllRecord() as MutableLiveData<List<QuestionAndAnswers>>
            }

        })
        withContext(Dispatchers.IO) {   if (isResponseCame && amount > 10) {
            questionsAndAnswersDao.insertAll(allQuestionDataSet.value!!)
        } else {
            questionsAndAnswersDao.deleteAll()
            questionsAndAnswersDao.insertAll(allQuestionDataSet.value!!)
        }}


        emitSource(allQuestionDataSet)
    }


}