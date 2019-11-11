package com.example.biometricprompt.ui.main

import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.CancellationSignal
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.example.biometricprompt.R
import kotlinx.android.synthetic.main.start_fragment.*

class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    private lateinit var viewModel: StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)

        showAuthenticatoin()

        button_show_authentication.setOnClickListener {
            showAuthenticatoin()
        }
    }

    fun showAuthenticatoin() {
        val cancelSignal = CancellationSignal()
        val builder = BiometricPrompt.Builder(context!!)
        builder.setTitle(context!!.getString(R.string.title_biometric_authentication))
        builder.setNegativeButton(context!!.getText(R.string.cancel),
            activity!!.mainExecutor, DialogInterface.OnClickListener {
                    dialogInterface, i -> cancelSignal.cancel()
            })
        builder.build().authenticate(
            cancelSignal,
            activity!!.mainExecutor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    when (errorCode) {
                        BiometricPrompt.BIOMETRIC_ERROR_NO_BIOMETRICS ->
                            Toast.makeText(context!!, "非対応です。", Toast.LENGTH_SHORT).show()
                        else ->
                            Toast.makeText(context!!, "その他のエラーです。", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
                    throw RuntimeException("Stub!")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    findNavController().navigate(R.id.action_startFragment_to_mainFragment)
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(context!!, "認証失敗です。", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
