package org.whiskersapps.mordomo.mobile.core.features.lock

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class LockService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action.toString() == "org.whiskersapps.mordomo.mobile.LOCK"
                || intent?.action.toString() == "org.whiskersapps.mordomo.mobile.debug.LOCK"

        if (action)
            performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)

        return START_STICKY
    }
}