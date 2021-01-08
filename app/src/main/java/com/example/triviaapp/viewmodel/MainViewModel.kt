package com.example.triviaapp.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.model.QuestionAndAnswers
import com.example.triviaapp.model.QuestionAnswersResponseModel
import com.example.triviaapp.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private var questionsMutableLiveData = MutableLiveData<List<QuestionAndAnswers>>()

    fun callQuestionApi(amount:Int, category:Int, difficulty:String, type:String) =viewModelScope.launch{
        questionsMutableLiveData =
            repository.getAllQuestions(amount, category,difficulty,type) as MutableLiveData<List<QuestionAndAnswers>>
    }

    fun getAllQuestions(): LiveData<List<QuestionAndAnswers>> {
        return questionsMutableLiveData
    }

}