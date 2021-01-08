package com.example.triviaapp.di

import android.content.Context
import com.example.triviaapp.db.AppDatabase
import com.example.triviaapp.interfaces.ApiInterface
import com.example.triviaapp.interfaces.QuestionsAndAnswersDao
import com.example.triviaapp.repository.MainRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS).build()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getInstance(appContext)

    @Singleton
    @Provides
    fun provideQuestionAndAnswerDao(db: AppDatabase) = db.questionAndAnswersDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource:ApiInterface,localDataSource:QuestionsAndAnswersDao) =
        MainRepository(remoteDataSource, localDataSource)



    @Provides
    fun provideAppService(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)
}