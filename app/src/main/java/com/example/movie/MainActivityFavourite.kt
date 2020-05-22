package com.example.movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.movie.Model.movie_search
import com.example.movie.MovieActivites.MainActivity
import com.example.movie.Network.popinterface
import com.example.movie.PeopleActivities.MainActivityPeople
import com.example.movie.TvActivities.MainActivitytv
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_7.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityFavourite : AppCompatActivity() {

    val api_key: String = "acdfb45666ea6163ac24018f92ca4ab0"
    var maxLimit: Int = 996
    val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val baseURL = "https://image.tmdb.org/t/p/w780/"
    val service = retrofit.create(popinterface::class.java)
    var favList: ArrayList<movie_search> = arrayListOf<movie_search>()
    var check: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_7)
        val naview = findViewById<View>(R.id.nav) as BottomNavigationView
        var menu = naview.menu
        var menuItem = menu.getItem(3)
        menuItem.isChecked = true

        favList.clear()

        naview.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.movies -> {
                    val intent1 = Intent(this, MainActivity::class.java)
                    startActivity(intent1)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tv -> {
                    val intent2 = Intent(this, MainActivitytv::class.java)
                    startActivity(intent2)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.person -> {
                    val intent3 = Intent(this, MainActivityPeople::class.java)
                    startActivity(intent3)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.FAV -> {

                    return@setOnNavigationItemSelectedListener true
                }

                else -> return@setOnNavigationItemSelectedListener true
            }
        }

        val db: FavouriteDatabase by lazy {
            Room.databaseBuilder(
                this,
                FavouriteDatabase::class.java,
                "Fav.db"
            ).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }

        //var db=FavouriteDatabase.getfavouriteDatabase(this)
        var a = db.FavDao().getallfav()
        Log.d("test", a.size.toString())

        for (i in 0..a.lastIndex) {
            Log.d("myCHECK", "LOOP")

            service.getmovies(a[i].movie_id.toInt(), api_key)
                .enqueue(object : Callback<movie_search> {
                    override fun onFailure(call: Call<movie_search>, t: Throwable) {
                        Log.d("MoviesDagger", t.toString())
                    }

                    override fun onResponse(
                        call: Call<movie_search>,
                        response: Response<movie_search>
                    ) {

                        val data = response.body()
                        Log.d("gggg", data!!.original_title)
                        favList.add(data)
                        Log.d("myCHECK", "ADDED")

                        if (i == a.lastIndex) {
                            Log.d("myCHECK", "${favList.size}")
                            if (favList.size != 0) {
                                check = false
                            }

                            rvfav.layoutManager =
                                GridLayoutManager(
                                    this@MainActivityFavourite,
                                    2,
                                    RecyclerView.VERTICAL,
                                    false
                                )
                            rvfav.adapter =
                                favouriteAdapter(
                                    this@MainActivityFavourite,
                                    favList,
                                    check
                                )
                            rvfav.adapter!!.notifyDataSetChanged()
                        }

                    }
                })
        }
        favList.clear()

    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finishAffinity()
    }

}
