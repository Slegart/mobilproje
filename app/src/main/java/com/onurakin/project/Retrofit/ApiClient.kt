package com.onurakin.project.Retrofit

import com.sefikonurakin_hw2.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private var retrofit: Retrofit? = null

    fun getService(): Retrofit {
        if (retrofit == null)
            retrofit = Retrofit.Builder()
                    .baseUrl(Constants.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

        return retrofit as Retrofit
    }
    fun getTimeService(): TimeService {
        return getService().create(TimeService::class.java)
    }
}