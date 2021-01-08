package com.example.triviaapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.triviaapp.interfaces.QuestionsAndAnswersDao
import com.example.triviaapp.model.QuestionAndAnswers

@Database(entities = [QuestionAndAnswers::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionAndAnswersDao(): QuestionsAndAnswersDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "trivia_game_db"
                    ).build()
                }
                return instance
            }
        }

    }
}