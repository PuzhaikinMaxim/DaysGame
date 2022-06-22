package com.example.daysgame

import android.app.Activity
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private lateinit var gameSettings: GameSettings

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _gameResults = MutableLiveData<GameResults>()
    val gameResults: LiveData<GameResults>
        get() = _gameResults

    fun startGame(){
        setupGameResults()
        generateQuestion()
    }

    fun generateQuestion(){
        val answer = Random.nextInt(0, gameSettings.maxDaysAmount) * 24 + Random.nextInt(0,24)
        val firstDate = Random.nextInt(0, answer)
        val secondDate = answer - firstDate
        _question.value = Question(
            firstDate,
            secondDate,
            answer
        )
    }

    fun acceptAnswer(days: Int, hours: Int){
        val total = days * 24 + hours
        if(total == _question.value?.answer){
            println("yes")
            _gameResults.value = _gameResults.value?.copy(
                amountOfRightAnswers = _gameResults.value?.amountOfRightAnswers!!.plus(1)
            )
        }
        _gameResults.value = _gameResults.value?.copy(
            amountOfAllAnswers = _gameResults.value?.amountOfAllAnswers!!.plus(1)
        )
        generateQuestion()
    }

    fun setSettings(difficultyLevel: DifficultyLevel){
        gameSettings = getGameSettings(difficultyLevel)
    }

    private fun setupGameResults(){
        _gameResults.value = GameResults(
            0,
            0,
            0
        )
    }

    companion object {

        private fun getGameSettings(difficultyLevel: DifficultyLevel): GameSettings {
            return when(difficultyLevel) {
                DifficultyLevel.CHILD -> {
                    GameSettings(
                        1,
                        5,
                    )
                }
                DifficultyLevel.EASY -> {
                    GameSettings(
                        3,
                        10,
                    )
                }
                DifficultyLevel.NORMAL -> {
                    GameSettings(
                        5,
                        20,
                    )
                }
                DifficultyLevel.HARD -> {
                    GameSettings(
                        7,
                        30,
                    )
                }
            }
        }
    }
}