package com.example.triviaapp.di

import android.content.Context
import com.example.rickandmorty.data.remote.QuestionsRemoteDataSource
import com.example.triviaapp.data.LocalDataSource
import com.example.triviaapp.db.AppDatabase
import com.example.triviaapp.interfaces.ApiService
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
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getInstance(appContext)

    @Singleton
    @Provides
    fun provideQuestionAndAnswerDao(db: AppDatabase) = db.questionAndAnswersDao()

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: QuestionsRemoteDataSource,
        localDataSource: LocalDataSource
    ) =
        MainRepository(remoteDataSource, localDataSource)


    @Singleton
    @Provides
    fun provideDataSource(remoteDataSource: ApiService) =
        QuestionsRemoteDataSource(remoteDataSource)

    @Singleton
    @Provides
    fun provideLocalDataSource(localDao: QuestionsAndAnswersDao) =
        LocalDataSource(localDao)


    @Provides
    fun provideAppService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}