package com.example.retrofitmarvel

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Model.Results
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainActivity : AppCompatActivity() {
    lateinit var superListView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        superListView = findViewById(R.id.superListView)

        getSuperHeroes()

    }

    private fun getSuperHeroes() {
        val call: Call<List<Results?>?>? = RetrofitClient.instance?.getMyApi()?.getsuperHeroes()
        val startTime = System.currentTimeMillis()
        call?.enqueue(object : Callback<List<Results?>?> {

            override fun onResponse(call: Call<List<Results?>?>?, response: Response<List<Results?>?>) {
                val endTime = System.currentTimeMillis()
                val responseTimeInMillis = endTime - startTime
                Log.d("response_time", "response_time: $responseTimeInMillis ms ")
                val myheroList: List<Results> = response.body() as List<Results>
                val oneHeroes = arrayOfNulls<String>(myheroList.size)
                for (i in myheroList.indices) {
                    oneHeroes[i] = myheroList[i].title
                }
                superListView.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, oneHeroes)
            }

            override fun onFailure(call: Call<List<Results?>?>?, t: Throwable?) {
                Toast.makeText(applicationContext, "An error has occured", Toast.LENGTH_LONG).show()
            }
        })
    }
}

