package com.example.triviaapp.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.triviaapp.model.QuestionAndAnswers
import com.example.triviaapp.model.QuestionAnswersResponseModel
import com.example.triviaapp.repository.MainRepository
import com.example.triviaapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : ViewModel() {



    fun getAllQuestions(  amount: Int,
                                  category: Int,
                                  difficulty: String,
                                  type: String) =
        liveData<Resource<List<QuestionAndAnswers>>>(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())

            try {
                repository.getAllQuestions(amount,category,difficulty,type).collect {
                    emit(it)
                }
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }

        }


}