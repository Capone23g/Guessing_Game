package com.hfad.guessinggame


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController


class GameFragment : Fragment() {
    lateinit var viewModel: GameFragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        viewModel = ViewModelProvider(this).get(GameFragmentViewModel::class.java)

        viewModel.gameOver.observe(viewLifecycleOwner, Observer { newValue ->
            if (newValue) {
                val action = GameFragmentDirections
                    .actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view?.findNavController()?.navigate(action)


            }


        })
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {

            setContent {
                MaterialTheme {
                    Surface {
                        GameFragmentContent(viewModel)

                    }
                }
            }


        }
    }


    /* viewModel.inCorrectGuesses.observe(viewLifecycleOwner, Observer { newValue ->
         binding.incorrectGuesses.text = "Incorrect guesses: $newValue"

     })

     viewModel.livesLeft.observe(viewLifecycleOwner, Observer { newValue ->
         binding.lives.text = "You have $newValue lives left."


     })
     viewModel.secretWordDisplay.observe(viewLifecycleOwner, Observer { newValue ->
         binding.word.text = newValue





     })
       */


}









@Composable
fun GameFragmentContent(viewModel: GameFragmentViewModel) {
    val guess = remember { mutableStateOf("") }


    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            SecretWordDisplay(viewModel = viewModel)

        }
        LivesLeftText(viewModel = viewModel)
        IncorrectGuessesText(viewModel = viewModel)
        EnterGuess(guess.value) { guess.value = it }



        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {


            GuessButton {
                viewModel.makeGuess(guess.value.uppercase())
                guess.value = ""   //Clear the TextField

            }
            FinishGameButton {
                viewModel.finishGame()

            }

        }
    }

}

@Composable
fun GuessButton(clicked: () -> Unit) {
    Button(onClick = clicked) {
        Text(text = "Guess")

    }

}

@Composable
fun EnterGuess(guess: String, changed: (String) -> Unit) {
    TextField(value = guess,
        label = { Text("Guess a letter ") },
        onValueChange = changed
    )

}

@Composable
fun FinishGameButton(clicked: () -> Unit) {
    Button(onClick = clicked) {
        Text("Finish Game")

    }
}

@Composable
fun IncorrectGuessesText(viewModel: GameFragmentViewModel) {
    val incorrectGuesses = viewModel.inCorrectGuesses.observeAsState()
    incorrectGuesses.value?.let {
        Text(stringResource(R.string.incorrect_guesses, it))
        /*
        the function stringResource() lets you use String resources with composables, and pass arguments to them

         */


    }

}

@Composable
fun LivesLeftText(viewModel: GameFragmentViewModel) {
    val livesLeft = viewModel.livesLeft.observeAsState()
    livesLeft.value?.let {

        Text(stringResource(R.string.lives_left, it))
    }


}

@Composable
fun SecretWordDisplay(viewModel: GameFragmentViewModel) {
    val display = viewModel.secretWordDisplay.observeAsState()
    display.value?.let {

        Text(text = it,
            letterSpacing = 0.1.em,
            fontSize = 36.sp)
    }


}
