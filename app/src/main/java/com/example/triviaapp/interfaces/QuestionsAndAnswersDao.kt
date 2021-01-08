package com.example.triviaapp.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.triviaapp.model.QuestionAndAnswers

@Dao
interface QuestionsAndAnswersDao {
    @Insert
    suspend fun insertRecord(questionAndAnswers: QuestionAndAnswers): Long

    @Update
    suspend fun updateRecord(questionAndAnswers: QuestionAndAnswers): Int

    @Delete
    suspend fun deleteOneRecord(rquestionAndAnswers: QuestionAndAnswers): Int

    @Query("DELETE FROM tbl_questions_and_answers")
    suspend fun deleteAll(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questionAndAnswers: List<QuestionAndAnswers>): Void

    @Query("SELECT * FROM tbl_questions_and_answers")
    fun getAllRecord(): LiveData<List<QuestionAndAnswers>>
}