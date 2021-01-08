package com.example.triviaapp.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.triviaapp.model.QuestionAndAnswers
import com.example.triviaapp.utils.Resource

@Dao
interface QuestionsAndAnswersDao {
    @Insert
    suspend fun insertRecord(questionAndAnswers: QuestionAndAnswers)

    @Update
    suspend fun updateRecord(questionAndAnswers: QuestionAndAnswers): Int

    @Delete
    suspend fun deleteOneRecord(questionAndAnswers: QuestionAndAnswers): Int

    @Query("DELETE FROM tbl_questions_and_answers")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(questionAndAnswers: List<QuestionAndAnswers>)

    @Query("SELECT * FROM tbl_questions_and_answers")
    suspend fun getAllRecord(): List<QuestionAndAnswers>
}