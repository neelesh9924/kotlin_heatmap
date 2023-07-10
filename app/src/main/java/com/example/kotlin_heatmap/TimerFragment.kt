package com.example.kotlin_heatmap

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kotlin_heatmap.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    lateinit var binding: FragmentTimerBinding

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        object : CountDownTimer(5000, 1000) { //todo: change to 45000
            override fun onTick(millisUntilFinished: Long) {
                binding.textViewTime.text =
                    "Please wait for ${(millisUntilFinished / 1000)} seconds to complete the scan."
            }

            override fun onFinish() {

                try {
                    navController.navigate(R.id.action_timerFragment_to_heatMapFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }.start()

    }
}