package org.whiskersapps.mordomo.mobile.core.features.lock

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsValues

class LockRepository(
    private val context: Context,
    private val settingsRepository: SettingsRepository,
) {
    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

        return accessibilityManager
            .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            .any { service -> service.resolveInfo.serviceInfo.packageName == context.packageName }
    }

    fun openAccessibilitySettings() {
        val component = ComponentName(context, LockService::class.java).flattenToString()

        val args = Bundle().apply {
            putString(":settings:fragment_args_key", component)
        }

        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            putExtra(":settings:fragment_args_key", component)
            putExtra(":settings:show_fragment_args", args)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("Lock", "Failed to open accessibility settings", e)
        }

    }

    suspend fun canLockScreen(): Boolean = withContext(Dispatchers.IO) {
        val method = settingsRepository.appSettings.value.home.lockScreenMethod

        return@withContext when (method) {
            SettingsValues.LockScreenMethod.ROOT -> {
                Shell.getShell().isRoot
            }

            SettingsValues.LockScreenMethod.ACCESSIBILITY -> {
                isAccessibilityServiceEnabled()
            }

            else -> false
        }
    }

    suspend fun lockScreen() = withContext(Dispatchers.IO) {
        val canLockScreen = canLockScreen()
        val method = settingsRepository.appSettings.value.home.lockScreenMethod

        try {
            if (!canLockScreen) {
                when (method) {
                    SettingsValues.LockScreenMethod.ROOT -> {
                        Shell.getShell().isRoot
                    }

                    SettingsValues.LockScreenMethod.ACCESSIBILITY -> {
                        openAccessibilitySettings()
                    }
                }

                return@withContext
            }


            when (method) {
                SettingsValues.LockScreenMethod.ROOT -> {
                    Shell.cmd("input keyevent 223").exec()
                }

                SettingsValues.LockScreenMethod.ACCESSIBILITY -> {
                    Log.d("Flag", "${context.packageName}.LOCK")
                    val intent = Intent(
                        "${context.packageName}.LOCK",
                        null,
                        context,
                        LockService::class.java
                    )

                    context.startService(intent)
                }

                else -> return@withContext
            }
        } catch (e: Exception) {
            Log.e("Lock", "Failed to lock the device", e)
        }
    }
}