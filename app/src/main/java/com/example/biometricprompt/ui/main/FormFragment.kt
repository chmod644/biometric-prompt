package com.example.biometricprompt.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.biometricprompt.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.form_fragment.*

class FormFragment : Fragment() {

    companion object {
        fun newInstance() = FormFragment()
    }

    private lateinit var viewModel: FormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.form_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FormViewModel::class.java)

        button_show_dialog.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Warning")
                .setMessage("this is sample of material alert dialog")
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .show()
            return@setOnClickListener

        }
    }

}
