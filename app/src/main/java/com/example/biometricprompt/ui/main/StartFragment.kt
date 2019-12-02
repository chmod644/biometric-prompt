package com.example.biometricprompt.ui.main

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.biometricprompt.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.start_fragment.*
import java.util.concurrent.Executor

class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    enum class CanAuthenticate(val message: String) {
        SUCCESS(message="Succeed"),
        ERROR_NO_AUTHENTICATION(message="No biometric information or sensor"),
        ERROR_AUTHENTICATION_SYSTEM(message="The authentication system has an error"),
    }

    enum class IsAuthenticated(val message: String) {
        SUCCESS(message="Succeed"),
        ERROR_AUTHENTICATION_SYSTEM(message="The authentication system has an error"),
        ERROR_AUTHENTICATION_USER(message="Authentication failed"),
        ERROR_NO_AUTHENTICATION(message="No biometric information or sensor"),
        CANCELED(message="Authentication canceled"),
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

        authenticate()
        button_show_authentication.setOnClickListener { authenticate() }
    }

    fun checkBiometric(): CanAuthenticate {
        val biometricManager = BiometricManager.from(context!!)
        return when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> CanAuthenticate.SUCCESS
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> CanAuthenticate.ERROR_AUTHENTICATION_SYSTEM
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> CanAuthenticate.ERROR_NO_AUTHENTICATION
            else -> CanAuthenticate.ERROR_AUTHENTICATION_SYSTEM
        }
    }

    fun authenticate() {
        val canAuthenticate = checkBiometric()
        if (canAuthenticate != CanAuthenticate.SUCCESS) {
            showSnackbar(canAuthenticate.message)
            return
        }

        val mainExecutor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity!!.mainExecutor
        } else {
            object: Executor {
                val handler = Handler(Looper.getMainLooper())
                override fun execute(command: Runnable) { handler.post(command) }
            }
        }

        val biometricPrompt = BiometricPrompt(
            this,
            mainExecutor,
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    val isAuthenticated = when (errorCode) {
                        BiometricPrompt.ERROR_HW_UNAVAILABLE,
                        BiometricPrompt.ERROR_UNABLE_TO_PROCESS,
                        BiometricPrompt.ERROR_TIMEOUT,
                        BiometricPrompt.ERROR_NO_SPACE,
                        BiometricPrompt.ERROR_CANCELED,
                        BiometricPrompt.ERROR_VENDOR
                        -> IsAuthenticated.ERROR_AUTHENTICATION_SYSTEM
                        BiometricPrompt.ERROR_LOCKOUT,
                        BiometricPrompt.ERROR_LOCKOUT_PERMANENT
                        -> IsAuthenticated.ERROR_AUTHENTICATION_USER
                        BiometricPrompt.ERROR_USER_CANCELED,
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON
                        -> IsAuthenticated.CANCELED
                        BiometricPrompt.ERROR_NO_BIOMETRICS,
                        BiometricPrompt.ERROR_HW_NOT_PRESENT,
                        BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL
                        -> IsAuthenticated.ERROR_NO_AUTHENTICATION
                        else -> IsAuthenticated.ERROR_AUTHENTICATION_SYSTEM
                    }
                    showSnackbar(isAuthenticated.message)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    findNavController().navigate(R.id.action_startFragment_to_mainFragment)
                }

                override fun onAuthenticationFailed() {
                    showSnackbar(IsAuthenticated.ERROR_AUTHENTICATION_SYSTEM.message)
                }
            })

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context!!.getString(R.string.title_biometric_authentication))
            .setNegativeButtonText(context!!.getText(R.string.cancel))
            .build()

        biometricPrompt.authenticate(info)
    }

    fun showSnackbar(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.ok, {}).show()
    }
}
