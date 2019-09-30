package com.example.materialtheme.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.materialtheme.R
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
