package org.whiskersapps.mordomo.mobile.core.features.fingerprint

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.fragment.app.FragmentActivity

fun requestFingerprint(
//    fragmentActivity: FragmentActivity,
    context: Context,
    title: String,
    message: String,
    onSuccess: () -> Unit,
) {

    val promptInfo = PromptInfo.Builder()
        .setTitle(title)
        .setSubtitle(message)
        .setNegativeButtonText("Cancel")
        .build()

    val prompt =
        BiometricPrompt(
            (context as FragmentActivity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)

                    if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                        onSuccess()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
            })

    prompt.authenticate(promptInfo)
}