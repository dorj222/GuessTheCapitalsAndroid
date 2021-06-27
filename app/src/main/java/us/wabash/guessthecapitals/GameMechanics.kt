package us.wabash.guessthecapitals

import android.graphics.Color
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

        //enable buttons
        enableButtons(buttons)

        //get and create buttons
        val (tvQuestion2, btnNext, btnRetry) = getButtons()
        val tvAnswerStreak = findViewById<TextView>(R.id.tvAnswerStreak)
        tvAnswerStreak.setText("")

        //randomly select a question
        val randomNumber = (0..3).random()
        val randomlySelectedCountry = randomlySelectQuestion(responseResult, resultList, tvQuestion2, randomNumber)

        for (i in buttons.indices) {
            buttons[i].setText(countries[i].name)
            buttons[i].setOnClickListener {
                colorButtons(buttons, randomNumber)
                val userGuessedCountry = countries[i].capital

                if(userGuessedCountry == randomlySelectedCountry){
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                    guessedCorrectly(btnNext, buttons, btnRetry, responseResult, tvAnswerStreak)

                } else{
                    Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show()
                    guessedIncorrectly(btnNext, buttons,btnRetry, responseResult, tvAnswerStreak)
                }
            }
        }
    }

    private fun colorButtons(
        buttons: ArrayList<Button>,
        randomNumber: Int
    ) {
        for(i in buttons.indices){
            if(randomNumber==i){
                buttons[i].setBackgroundColor(Color.parseColor("#148F77"))
            }else{
                buttons[i].setBackgroundColor(Color.parseColor("#E6B0AA"))
            }
        }
    }

    private fun randomlySelectQuestion(
        responseResult: List<countryDataItem>,
        resultList: MutableList<Int>,
        tvQuestion2: TextView,
        randomNumber: Int
    ): String {

        val randomlySelectedCountry = responseResult[resultList[randomNumber]].capital
        val tvQuestion = findViewById<TextView>(R.id.tvQuestion)
        tvQuestion.text = "WHICH COUNTRY'S CAPITAL"
        tvQuestion2.text = "CITY IS NAMED $randomlySelectedCountry?"

        return randomlySelectedCountry
    }

    private fun getButtons(): Triple<TextView, TextView, TextView> {
        val tvQuestion2 = findViewById<TextView>(R.id.tvQuestion2)
        val btnNext = findViewById<TextView>(R.id.btnNext)
        val btnRetry = findViewById<TextView>(R.id.btnRetry)
        btnNext.setVisibility(View.INVISIBLE)
        btnRetry.setVisibility(View.INVISIBLE)
        return Triple(tvQuestion2, btnNext, btnRetry)
    }

    private fun guessedIncorrectly(
        btnNext: TextView,
        buttons: ArrayList<Button>,
        btnRetry: TextView,
        responseResult: List<countryDataItem>,
        tvAnswerStreak: TextView
    ) {
        val answerStreak = countriesList.size/4 -1
        countriesList.clear()
        tvAnswerStreak.text = "Correctly Answered: " + answerStreak.toString()

        disableButtons(buttons)
        btnNext.setVisibility(View.INVISIBLE)
        btnRetry.setVisibility(View.VISIBLE)
        btnRetry.setOnClickListener {
            displayQuestionAnswer(responseResult)
        }
    }

    private fun guessedCorrectly(
        btnNext: TextView,
        buttons: ArrayList<Button>,
        btnRetry: TextView,
        responseResult: List<countryDataItem>,
        tvAnswerStreak: TextView
    ) {

        val answerStreak = countriesList.size/4
        tvAnswerStreak.setText("Answer Streak $answerStreak")
        disableButtons(buttons)
        btnNext.setVisibility(View.VISIBLE)
        btnRetry.setVisibility(View.INVISIBLE)
        btnNext.setOnClickListener {
            btnNext.setVisibility(View.INVISIBLE)
            displayQuestionAnswer(responseResult)
        }
    }

    private fun disableButtons(buttons: ArrayList<Button>) {
        for (i in buttons.indices) {
            buttons[i].isEnabled = false
        }
    }

    private fun enableButtons(buttons: ArrayList<Button>) {
        for (i in buttons.indices) {
            buttons[i].isEnabled = true
            buttons[i].setBackgroundColor(Color.parseColor("#F6C6EA"))
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

