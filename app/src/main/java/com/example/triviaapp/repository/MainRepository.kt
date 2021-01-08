package com.example.triviaapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.rickandmorty.data.remote.QuestionsRemoteDataSource
import com.example.triviaapp.data.LocalDataSource
import com.example.triviaapp.interfaces.ApiService
import com.example.triviaapp.interfaces.QuestionsAndAnswersDao
import com.example.triviaapp.model.QuestionAndAnswers
import com.example.triviaapp.model.QuestionAnswersResponseModel
import com.example.triviaapp.utils.Event
import com.example.triviaapp.utils.Resource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ActivityRetainedScoped
class MainRepository @Inject constructor(
    private val remoteDataSource: QuestionsRemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    private val exception_messag = MutableLiveData<Event<String>>()

    val coroutineExceptionHanlder = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        Log.e("test_coroutine", "exception ccoured")
        exception_messag.postValue(Event(throwable.localizedMessage))
    }


    fun getAllQuestions(
        amount: Int,
        category: Int,
        difficulty: String,
        type: String
    ): Flow<Resource<List<QuestionAndAnswers>>> =
        callbackFlow {
           // offer(getCachedQuestions())
            remoteDataSource.getQuestions(amount, category, difficulty, type).collect {
                when (it) {
                    is Resource.Success -> {

                        if(amount<=10){
                            deleteAllQuestions()
                        }

                        for (data in it.data.results) {
                            saveQuestion(data)
                        }


                        offer(getCachedQuestions())
                    }
                    is Resource.Failure -> {
                        offer(getCachedQuestions())
                    }
                }
            }

            awaitClose { cancel() }

        }


    suspend fun saveQuestion(data: QuestionAndAnswers) {
        localDataSource.saveData(data)
    }


    suspend fun deleteAllQuestions() {
        localDataSource.deleteAll()
    }

    suspend fun getCachedQuestions(): Resource<List<QuestionAndAnswers>> {
        return localDataSource.getAllData()
    }


}