package com.example.kotlin_heatmap

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kotlin_heatmap.databinding.FragmentHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class HistoryFragment : Fragment() {

    lateinit var binding: FragmentHistoryBinding

    lateinit var navController: NavController

    private val database by lazy {
        AppDatabase.getDatabase(requireContext()).roomDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        observeData();

    }

    private fun observeData() {
        lifecycleScope.launch {
            database.getAll().collect {
                if (it.isNotEmpty()) {
                   Log.i("databaseSize", it.size.toString())
                }
            }
        }
    }
}