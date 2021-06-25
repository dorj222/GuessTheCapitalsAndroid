package us.wabash.guessthecapitals

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import us.wabash.guessthecapitals.API.CountryAPI
import us.wabash.guessthecapitals.data.countryDataItem

const val baseURL = "https://restcountries.eu/"
val countriesList = mutableListOf<Int>()


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
                val responseResult = response.body()!!
                val resultList = randomSequenceGenerator()

                val country1 = responseResult[resultList[0]]
                val country2 = responseResult[resultList[1]]
                val country3 = responseResult[resultList[2]]
                val country4 = responseResult[resultList[3]]

                val textView = findViewById<TextView>(R.id.tvQuestion2)
                textView.text = country1.name

                val button1 = findViewById<Button>(R.id.btnAnswer1)
                button1.text = country1.capital

                val button2 = findViewById<Button>(R.id.btnAnswer2)
                button2.text = country2.capital

                val button3 = findViewById<Button>(R.id.btnAnswer3)
                button3.text = country3.capital

                val button4 = findViewById<Button>(R.id.btnAnswer4)
                button4.text = country4.capital


                Log.d("tag5", country1.capital)

            }

            override fun onFailure(call: Call<List<countryDataItem>?>, t: Throwable) {

            }
        })
    }

    private fun randomSequenceGenerator(): MutableList<Int> {
        val responseList = mutableListOf<Int>()

        while(responseList.size<4){
         isUniqueNumber(responseList)
        }
        return responseList
    }

    private fun isUniqueNumber (responseList: MutableList<Int>) {

        val randomValue = (0..249).random()
        if (!countriesList.contains(randomValue)) {
            countriesList.add(randomValue)
            responseList.add(randomValue)
        } else{
            isUniqueNumber(responseList)
        }
    }
}