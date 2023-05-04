package com.hfad.guessinggame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class GameFragmentViewModel : ViewModel() {

    private val words = listOf<String>("Android", "Activity", "Fragment")
    private val secretWord = words.random().uppercase() //Word the user has to guess
    private val _secretWordDisplay = MutableLiveData<String>()
    val secretWordDisplay: LiveData<String>
        get() = _secretWordDisplay

    private var correctGuesses = ""
    private val _inCorrectGuesses = MutableLiveData<String>("")
    val inCorrectGuesses: LiveData<String>
        get() = _inCorrectGuesses

    private val _livesLeft = MutableLiveData<Int>(8)
    val livesLeft: LiveData<Int>
        get() = _livesLeft // livesLeft returns a read-only version of the _livesLeft backing property

    private val _gameOver = MutableLiveData<Boolean>(false)
    val gameOver: LiveData<Boolean>
        get() = _gameOver

    init {
        _secretWordDisplay.value = deriveSecretWordDisplay()
    }


    private fun deriveSecretWordDisplay(): String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())

        }
        return display

    }

    //Checks whether the secret word contains the letter the user has guessed .
    //If so, it returns the letter,else it returns "_"
    private fun checkLetter(str: String) = when (correctGuesses.contains(str)) {
        true -> str
        false -> "_"
    }

    fun makeGuess(guess: String) {
        if (guess.length == 1) {
            if (secretWord.contains(guess)) {
                correctGuesses += guess
                _secretWordDisplay.value = deriveSecretWordDisplay()
            } else {

                /*
                *For Each wrong guess,update incorrect guesses and lives left
                 */
                _inCorrectGuesses.value += guess
                _livesLeft.value = livesLeft.value?.minus(1)
            }
        }
        //set gameOver property value to true if the game is won or lost
        if (isWon() || isLost()) _gameOver.value = true


    }

    private fun isWon() = secretWord.equals(secretWordDisplay.value, true)
    private fun isLost() =
        (livesLeft.value ?: 0) <= 0 // Game is lost when the user runs out of lives

    fun wonLostMessage(): String {
        var message = ""
        if (isWon()) message = "You Won!"
        else if (isLost()) message = "You Lost!"
        message += "The word was $secretWord"

        return message

    }

    fun finishGame() {
        _gameOver.value = true

    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameFragmentViewModel", "ViewModel Cleared")
    }

}