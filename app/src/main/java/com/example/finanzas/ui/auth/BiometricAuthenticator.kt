package com.example.finanzas.ui.auth

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class BiometricAuthenticator(
    // Necesitamos la actividad para mostrar el diálogo de autenticación
    private val activity: AppCompatActivity
) {
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var biometricPrompt: BiometricPrompt

    private val executor = ContextCompat.getMainExecutor(activity)

    // Función para verificar si la autenticación está disponible en el dispositivo
    fun isBiometricAuthAvailable(): Boolean {
        val biometricManager = BiometricManager.from(activity)
        val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        return biometricManager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }

    // Función para mostrar el diálogo de autenticación
    fun promptBiometricAuth(
        title: String,
        subtitle: String,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, CharSequence) -> Unit
    ) {
        // Configuramos el texto que verá el usuario
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        // Creamos la instancia del prompt con los callbacks de resultado
        biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                // Se llama cuando la autenticación es exitosa
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result)
                }

                // Se llama si hay un error o el usuario cancela
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString)
                }
            }
        )

        // Mostramos el diálogo al usuario
        biometricPrompt.authenticate(promptInfo)
    }
}