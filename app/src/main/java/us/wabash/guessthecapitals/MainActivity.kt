package us.wabash.guessthecapitals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btnStart)
        button.setOnClickListener {
            startGame()
        }

    }

    fun startGame() {

        val intent = Intent(this, GameMechanics::class.java).apply {

        }
        startActivity(intent)
    }

}