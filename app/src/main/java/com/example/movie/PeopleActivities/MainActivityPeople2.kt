package com.example.movie.PeopleActivities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.movie.Model.people
import com.example.movie.Network.popinterface
import com.example.movie.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_6.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivityPeople2 : AppCompatActivity() {

    val api_key: String = "acdfb45666ea6163ac24018f92ca4ab0"
    var maxLimit: Int = 996
    val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val baseURL = "https://image.tmdb.org/t/p/w780/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_6)
        val id = intent.getStringExtra("id").toInt()

        val type = intent.getStringExtra("type")
        val service = retrofit.create(popinterface::class.java)

        service.getpeopledetail(id, api_key).enqueue(object : Callback<people> {
            override fun onFailure(call: Call<people>, t: Throwable) {
                Log.d("MoviesDagger", t.toString())
            }

            override fun onResponse(call: Call<people>, response: Response<people>) {
                val data = response.body()
                if (data != null) {
                    Picasso.get().load(baseURL + data.profile_path).resize(120, 170)
                        .into(imagepeople)

                }
                peoplename.text = data?.name
                peoplepop.text = data?.popularity.toString()
                peopledate.text = data?.birthday
                peoplebio.text = data?.biography
                //tvoverview.text=data?.overview
            }
        })

    }
}
