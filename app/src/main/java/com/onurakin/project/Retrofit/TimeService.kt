package com.onurakin.project.Retrofit

import retrofit2.Call
import retrofit2.http.GET

interface TimeService {
    @GET("https://worldtimeapi.org/api/timezone/Europe/Istanbul/")
    fun getTime(): Call<TimeResponse>
}
