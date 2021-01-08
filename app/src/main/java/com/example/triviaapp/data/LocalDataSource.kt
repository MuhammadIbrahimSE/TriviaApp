package com.example.triviaapp.data

import androidx.lifecycle.LiveData
import com.example.triviaapp.interfaces.QuestionsAndAnswersDao
import com.example.triviaapp.model.QuestionAndAnswers
import com.example.triviaapp.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocalDataSource @Inject constructor(private val dataDao: QuestionsAndAnswersDao) {
    suspend fun saveData(cocktail: QuestionAndAnswers) {
        return dataDao.insertRecord(cocktail)
    }

    suspend fun getAllData(): Resource<List<QuestionAndAnswers>> {
        return Resource.Success(dataDao.getAllRecord())
    }

    suspend fun deleteAll() {
        return dataDao.deleteAll()
    }
}