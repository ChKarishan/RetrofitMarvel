package com.example.retrofitmarvel

import android.util.Log
import com.example.retrofitmarvel.Api.Companion.BASE_URL
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


var onlineInterceptor = Interceptor { chain ->
    val response = chain.proceed(chain.request())
    val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
    Log.d("response_time", "onlineInterceptor ")
    response.newBuilder()
        .header("Cache-Control", "public, max-age=$maxAge")
        .removeHeader("Pragma")
        .build()
}

var offlineInterceptor = Interceptor { chain ->
    var request: Request = chain.request()
    if (!utils.hasNetwork(MyApp.instance.applicationContext)) {
        val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
        Log.d("response_time", "offlineInterceptor ")
        request = request.newBuilder()
            .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
            .removeHeader("Pragma")
            .build()
    }
    chain.proceed(request)
}



val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB cache size
val cacheDir  = MyApp.instance.applicationContext.cacheDir
val cache = Cache(cacheDir, cacheSize)

class RetrofitClient private constructor() {
    private val myApi: Api

    init {
        val httpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
//            .addInterceptor { chain ->
//                var request = chain.request()
//                if(utils.hasNetwork(MyApp.instance.applicationContext)){
//                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
//                    Log.d("response_time", "has internet")
//                }else{
//                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
//                }
//
//                val startTime = System.nanoTime()
//                val response = chain.proceed(request)
//                val endTime = System.nanoTime()
//                val latencyInMillis = (endTime - startTime) / 1_000_000
//
//                // Log the latency or use it as per your requirement
//                Log.d("response_time", "Latency: $latencyInMillis ms")
//
//                response
//            }
            .build()

        val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
        myApi = retrofit.create(Api::class.java)
    }

    fun getMyApi(): Api {
        return myApi
    }

    companion object {
        @get:Synchronized
        var instance: RetrofitClient? = null
            get() {
                if (field == null) {
                    field = RetrofitClient()
                }
                return field
            }
            private set
    }
}