package com.example.biometricprompt.ui.main

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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

    fun checkBiometric(): Boolean {
        val biometricManager = BiometricManager.from(context!!)
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun authenticate() {
        // 生体認証の利用可否をチェックします。
        if (!checkBiometric()) {
            Snackbar.make(
                view!!,
                "This device can't be authenticated with biometric",
                Snackbar.LENGTH_INDEFINITE
            ).show()
            return
        }

        // BiometricPromptにわたすExecutor。
        // Android Pより前ではActivity#getMainExecutorが使えないので分岐があります。
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
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    // 認証が成功。次の画面への遷移させます。
                    findNavController().navigate(R.id.action_startFragment_to_mainFragment)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // 回復不能なエラーが発生。スナックバーを表示しておきます。
                    Snackbar.make(
                        view!!,
                        "An error occurred during biometric authentication",
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }

                override fun onAuthenticationFailed() {
                    // 回復不能なエラーが発生。認証を続けることができるのでロギングだけしておきます。
                    Log.e("StartFragment", "Failed to authenticate with biometric information")
                }
            })


        // 生体認証のプロンプトに表示するタイトルとキャンセルボタンのテキストの設定。
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context!!.getString(R.string.title_biometric_authentication))
            .setNegativeButtonText(context!!.getText(R.string.cancel))
            .build()

        // 生体認証の実行
        biometricPrompt.authenticate(info)
    }
}
