package org.whiskersapps.mordomo.mobile.core.features.apps

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC
import android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST
import android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED
import android.os.Process
import android.util.DisplayMetrics
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import org.whiskersapps.mordomo.mobile.core.features.icons.IconsRepository

class AppsRepository(
    private val context: Context,
    private val iconsRepository: IconsRepository
) {
    private val packageManager = context.packageManager
    private val launcherApps =
        context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

    var apps = emptyList<App>()

    private val _filteredApps = MutableStateFlow(emptyList<App>())
    val filteredApps = _filteredApps.asStateFlow()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            var sequenceNumber = 0

            while (true) {
                val changedPackages = packageManager.getChangedPackages(sequenceNumber)

                if (changedPackages != null) {
                    sequenceNumber = changedPackages.sequenceNumber
                    fetchApps()
                }

                delay(3000)
            }
        }
    }

    suspend fun fetchApps() {
        withContext(Dispatchers.Main) {
            val launcherIntent = Intent(Intent.ACTION_MAIN, null)
            launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER)


            val updatedApps =
                packageManager.queryIntentActivities(launcherIntent, 0).map { appIntent ->
                    val packageName = appIntent.activityInfo.packageName
                    val appInfo = packageManager.getApplicationInfo(packageName, 0)

                    val name = appInfo.loadLabel(packageManager).toString()

                    val shortcutQuery = LauncherApps.ShortcutQuery().apply {
                        setQueryFlags(FLAG_MATCH_DYNAMIC or FLAG_MATCH_MANIFEST or FLAG_MATCH_PINNED)
                        setPackage(packageName)
                    }

                    val shortcuts: List<App.Shortcut> = try {
                        launcherApps.getShortcuts(shortcutQuery, Process.myUserHandle())
                            ?.map { shortcut ->

                                val icon = launcherApps.getShortcutIconDrawable(
                                    shortcut,
                                    DisplayMetrics.DENSITY_MEDIUM
                                )

                                App.Shortcut(
                                    id = shortcut.id,
                                    text = shortcut.shortLabel.toString(),
                                    icon = icon?.toBitmap()
                                )
                            } ?: emptyList()
                    } catch (e: Exception) {
                        emptyList()
                    }.take(5)

                    App(
                        name = name,
                        packageName = packageName,
                        icons = App.Icons(
                            stock = iconsRepository.getStockIcon(packageName),
                            themed = iconsRepository.getThemedIcon(packageName)
                        ),
                        shortcuts = shortcuts
                    )
                }


            withContext(Dispatchers.Default) {
                apps = updatedApps.filter { it.packageName != "org.whiskersapps.mordomo.mobile" }
                    .sortedBy { it.name.lowercase() }
                    .distinctBy { it.packageName }

                _filteredApps.update { apps }

                for (app in apps) {
                    Log.d("App", app.packageName)
                }
            }

//            val blacklistedApps = settingsRepo.settings.value.hiddenApps
//            val displayableApps = newApps.filterNot { blacklistedApps.contains(it.packageName) }

//            _apps.update { displayableApps }

//            iconPacksRepo.fetchIconPacks()
        }
    }

    fun openApp(app: App) {
        CoroutineScope(Dispatchers.Default).launch {
            val intent = packageManager.getLaunchIntentForPackage(app.packageName)
            intent?.let {
                context.startActivity(it)
            }
        }
    }

    fun openShortcut(packageName: String, shortcut: App.Shortcut) {
        launcherApps.startShortcut(
            packageName,
            shortcut.id,
            null,
            null,
            Process.myUserHandle()
        )
    }
}