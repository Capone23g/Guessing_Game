package com.hfad.guessinggame


import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hfad.guessinggame.databinding.FragmentGameBinding
import androidx.lifecycle.Observer
import java.util.*


class GameFragment : Fragment() {
    lateinit var viewModel: GameFragmentViewModel

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(GameFragmentViewModel::class.java)
        binding.gameViewModel = viewModel // Assign this viewmodel to the data binding variable
        binding.lifecycleOwner = viewLifecycleOwner // This lets the layout respond to live data updates


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

        viewModel.gameOver.observe(viewLifecycleOwner, Observer { newValue ->
            if (newValue) {
                val action = GameFragmentDirections
                    .actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view.findNavController().navigate(action)


            }


        })





        binding.guessButton.setOnClickListener {


            viewModel.makeGuess(binding.guess.text.toString().uppercase())
            binding.guess.text = null


        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}