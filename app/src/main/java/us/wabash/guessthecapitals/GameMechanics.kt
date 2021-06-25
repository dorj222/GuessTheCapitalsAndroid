package us.wabash.guessthecapitals

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import us.wabash.guessthecapitals.API.CountryAPI
import us.wabash.guessthecapitals.data.countryDataItem

val baseURL = "https://restcountries.eu/"

class GameMechanics : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_mechanics)

        val retrofitBuilder = Retrofit.Builder()

            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseURL)
            .build()
            .create(CountryAPI::class.java)
        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<countryDataItem>?> {
            override fun onResponse(
                call: Call<List<countryDataItem>?>,
                response: Response<List<countryDataItem>?>
            ) {
                val responseBody = response.body()!!
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, responseBody.toString(), duration)
                toast.show()
            }

            override fun onFailure(call: Call<List<countryDataItem>?>, t: Throwable) {

            }
        })
    }
}