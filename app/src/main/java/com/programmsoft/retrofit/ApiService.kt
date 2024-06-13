package com.programmsoft.retrofit

import com.programmsoft.models.AphorismData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("11")
    suspend fun getDataWithPage(@Query("page") page: Int): Response<AphorismData>
}