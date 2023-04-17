package com.cradle.application

import android.app.Application
import com.cradle.repository.QuoteRepository

import com.cradle.api.RetrofitHelper
import com.cradle.api.RetrofitService

class ApplicationClass : Application() {
    lateinit var repository: QuoteRepository
    //lateinit var quoteDataBase: QuoteDataBase
    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        repository= QuoteRepository(quoteService,applicationContext)

    }


}