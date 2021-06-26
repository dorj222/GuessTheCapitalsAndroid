package us.wabash.guessthecapitals

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import us.wabash.guessthecapitals.API.CountryAPI
import us.wabash.guessthecapitals.data.countryDataItem
import java.util.*
import kotlin.collections.ArrayList

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

                displayQuestionAnswer(responseResult, resultList)
            }

            override fun onFailure(call: Call<List<countryDataItem>?>, t: Throwable) {

            }
        })
    }

    private fun displayQuestionAnswer(responseResult: List<countryDataItem>,
                                      resultList: MutableList<Int>
    ) {

        val (countries, buttons) = setArrays(responseResult, resultList)
        val textView = findViewById<TextView>(R.id.tvQuestion2)

        //randomly select a question
        val randomNumber = (0..3).random()
        val randomlySelectedCountry = responseResult[resultList[randomNumber]].capital
//        var userGuessedCountry: String

        textView.text = "$randomlySelectedCountry?"

        for (i in buttons.indices) {
            buttons[i].setText(countries[i].name)
            buttons[i].setOnClickListener {
            val userGuessedCountry = countries[i].capital

                if(userGuessedCountry == randomlySelectedCountry){
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setArrays(
        responseResult: List<countryDataItem>,
        resultList: MutableList<Int>
    ): Pair<ArrayList<countryDataItem>, ArrayList<Button>> {

        val countries = arrayListOf<countryDataItem>(
            responseResult[resultList[0]],
            responseResult[resultList[1]],
            responseResult[resultList[2]],
            responseResult[resultList[3]]
        )

        val buttons = arrayListOf<Button>(
            findViewById<Button>(R.id.btnAnswer1),
            findViewById<Button>(R.id.btnAnswer2),
            findViewById<Button>(R.id.btnAnswer3),
            findViewById<Button>(R.id.btnAnswer4)
        )
        return Pair(countries, buttons)
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

