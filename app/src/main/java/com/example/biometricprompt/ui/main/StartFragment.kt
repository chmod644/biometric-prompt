package com.example.biometricprompt.ui.main

import android.content.DialogInterface
import android.os.*
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.arch.core.executor.DefaultTaskExecutor
import androidx.navigation.fragment.findNavController

import com.example.biometricprompt.R
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.start_fragment.*
import java.util.concurrent.Executor

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
        val mainExecutor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity!!.mainExecutor
        } else {
            object: Executor {
                val handler = Handler(Looper.getMainLooper())
                override fun execute(command: Runnable) {
                    handler.post(command)
                }
            }
        }

        val biometricPrompt = BiometricPrompt(
            this,
            mainExecutor,
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    when (errorCode) {
                        BiometricPrompt.ERROR_NO_BIOMETRICS ->
                            Toast.makeText(context!!, "非対応です。", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    findNavController().navigate(R.id.action_startFragment_to_mainFragment)
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(context!!, "認証失敗です。", Toast.LENGTH_SHORT).show()
                }

            })

        val info = BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(context!!.getString(R.string.title_biometric_authentication))
            setNegativeButtonText(context!!.getText(R.string.cancel))
        }.build()

        biometricPrompt.authenticate(info)
    }
}
