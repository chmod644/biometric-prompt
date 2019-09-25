package com.example.exampleforegroundservice.ui.main

import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.exampleforegroundservice.R
import com.example.exampleforegroundservice.service.SimpleIntentService
import com.example.exampleforegroundservice.service.DefaultIntentService
import com.example.exampleforegroundservice.service.HighIntentService
import com.example.exampleforegroundservice.service.LowIntentService
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // Set view listener
        buttonDataUpload.setOnClickListener {
            Intent(requireContext(), SimpleIntentService::class.java).also { intent ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireActivity().startForegroundService(intent)
                } else {
                    requireActivity().startService(intent)
                }
            }
        }

        buttonDataUploadLow.setOnClickListener {
            Intent(requireContext(), LowIntentService::class.java).also { intent ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireActivity().startForegroundService(intent)
                } else {
                    requireActivity().startService(intent)
                }
            }
        }
        buttonDataUploadDefault.setOnClickListener {
            Intent(requireContext(), DefaultIntentService::class.java).also { intent ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireActivity().startForegroundService(intent)
                } else {
                    requireActivity().startService(intent)
                }
            }
        }
        buttonDataUploadHigh.setOnClickListener {
            Intent(requireContext(), HighIntentService::class.java).also { intent ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireActivity().startForegroundService(intent)
                } else {
                    requireActivity().startService(intent)
                }
            }
        }
    }

}
