package com.example.triviaapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_questions_and_answers")
data class QuestionAndAnswers(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "category")
    var category: String = "",


    @ColumnInfo(name = "type")
    var type: String = "",


    @ColumnInfo(name = "question")
    var question: String = "",

    @ColumnInfo(name = "correctAnswer")
    var correct_answer: String = "",

    @ColumnInfo(name = "difficulty")
    val difficulty: String,

    @ColumnInfo(name = "incorrect_answers")
    val incorrect_answers: ArrayList<String>

)