package com.example.retrofitmarvel

import com.example.Model.Results
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import retrofit2.Call
import retrofit2.http.GET

interface Api{

    @GET("products")
    fun getsuperHeroes(): Call<List<Results?>?>?

//    suspend fun getsuperHeroesKtor(
//    ): Call<List<Results?>?>? = client.get("BASE_URL")



    companion object {
//        const val BASE_URL = "https://simplifiedcoding.net/demos/"
        const val BASE_URL = "https://fakestoreapi.com/"
        const val PRODUCTS = "${BASE_URL}products/"
    }
}


