package us.wabash.guessthecapitals

import android.os.Bundle
import android.view.View
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
import kotlin.collections.ArrayList

const val baseURL = "https://restcountries.eu/"
var countriesList = mutableListOf<Int>()


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

                displayQuestionAnswer(responseResult)
            }

            override fun onFailure(call: Call<List<countryDataItem>?>, t: Throwable) {
            }
        })
    }

    private fun displayQuestionAnswer(responseResult: List<countryDataItem>) {

        val resultList = randomSequenceGenerator()
        val (countries, buttons) = setArrays(responseResult, resultList)

        //get and create buttons
        val (textView, btnNext, btnRetry) = getButtons()
        val tvAnswerStreak = findViewById<TextView>(R.id.tvAnswerStreak)
        tvAnswerStreak.setText("")

        //randomly select a question
        val randomlySelectedCountry = randomlySelectQuestion(responseResult, resultList, textView)

        for (i in buttons.indices) {
            buttons[i].setText(countries[i].name)
            buttons[i].setOnClickListener {
            val userGuessedCountry = countries[i].capital

                if(userGuessedCountry == randomlySelectedCountry){
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                    guessedCorrectly(btnNext, btnRetry, responseResult, tvAnswerStreak)

                } else{
                    Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show()
                    guessedIncorrectly(btnNext, btnRetry, responseResult, tvAnswerStreak)
                }
            }
        }
    }

    private fun randomlySelectQuestion(
        responseResult: List<countryDataItem>,
        resultList: MutableList<Int>,
        textView: TextView
    ): String {
        val randomNumber = (0..3).random()
        val randomlySelectedCountry = responseResult[resultList[randomNumber]].capital
        textView.text = "$randomlySelectedCountry?"
        return randomlySelectedCountry
    }

    private fun getButtons(): Triple<TextView, TextView, TextView> {
        val textView = findViewById<TextView>(R.id.tvQuestion2)
        val btnNext = findViewById<TextView>(R.id.btnNext)
        val btnRetry = findViewById<TextView>(R.id.btnRetry)
        btnNext.setVisibility(View.INVISIBLE)
        btnRetry.setVisibility(View.INVISIBLE)
        return Triple(textView, btnNext, btnRetry)
    }

    private fun guessedIncorrectly(
        btnNext: TextView,
        btnRetry: TextView,
        responseResult: List<countryDataItem>,
        tvAnswerStreak: TextView
    ) {
        val answerStreak = countriesList.size/4 -1
        countriesList.clear()
        tvAnswerStreak.text = "Correctly Answered: " + answerStreak.toString()
        btnNext.setVisibility(View.INVISIBLE)
        btnRetry.setVisibility(View.VISIBLE)
        btnRetry.setOnClickListener {
            displayQuestionAnswer(responseResult)
        }
    }

    private fun guessedCorrectly(
        btnNext: TextView,
        btnRetry: TextView,
        responseResult: List<countryDataItem>,
        tvAnswerStreak: TextView
    ) {
        val answerStreak = countriesList.size/4
        tvAnswerStreak.setText("Answer Streak $answerStreak")
        btnNext.setVisibility(View.VISIBLE)
        btnRetry.setVisibility(View.INVISIBLE)
        btnNext.setOnClickListener {
            btnNext.setVisibility(View.INVISIBLE)
            displayQuestionAnswer(responseResult)
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

