package com.example.daysgame

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.lang.RuntimeException

class GameActivity : AppCompatActivity() {

    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        parseIntent()
        setupObservers()
        startGame()
    }

    private fun parseIntent(){
        val difficultyLevel = intent.getParcelableExtra<DifficultyLevel>(EXTRA_NAME)
            ?: throw RuntimeException("Exception")
        gameViewModel.setSettings(difficultyLevel)
    }

    private fun startGame(){
        gameViewModel.startGame()
    }

    private fun setupObservers(){
        val tvSum = findViewById<TextView>(R.id.tv_sum)
        val etDays = findViewById<EditText>(R.id.et_days)
        val etHours = findViewById<EditText>(R.id.et_hours)
        val tvAmountOfAnswers = findViewById<TextView>(R.id.tv_amount_of_answers)
        val button = findViewById<Button>(R.id.accept_answer)
        button.setOnClickListener {
            val days = when(etDays.text.toString()){
                ""->{
                    0
                }
                else -> etDays.text.toString().toInt()
            }
            val hours = when(etHours.text.toString()){
                ""->{
                    0
                }
                else -> etHours.text.toString().toInt()
            }
            gameViewModel.acceptAnswer(days,hours)
            etDays.text.clear()
            etHours.text.clear()
        }
        gameViewModel.gameResults.observe(this){
            tvAmountOfAnswers.text = getString(R.string.amountOfAnswers,
                it.amountOfAllAnswers.toString(),
                it.amountOfRightAnswers.toString()
            )
        }
        gameViewModel.question.observe(this){
            tvSum.text = getString(R.string.question,
                (it.firstDate/24).toString(),
                (it.firstDate%24).toString(),
                (it.secondDate/24).toString(),
                (it.secondDate%24).toString(),
            )
        }
    }

    companion object {

        private const val EXTRA_NAME="difficulty"

        fun newIntent(activity: Activity, difficultyLevel: DifficultyLevel): Intent{
            val intent = Intent(activity, GameActivity::class.java)
            intent.putExtra(EXTRA_NAME, difficultyLevel as Parcelable)
            return intent
        }
    }
}