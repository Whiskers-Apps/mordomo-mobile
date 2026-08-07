package org.whiskersapps.mordomo.mobile.core.features.apps

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC
import android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST
import android.content.pm.LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED
import android.net.Uri
import android.os.Process
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import org.whiskersapps.mordomo.mobile.core.features.icons.IconsRepository
import org.whiskersapps.mordomo.mobile.core.features.settings.SettingsRepository
import org.whiskersapps.mordomo.mobile.core.utils.isDefaultLauncher

class AppsRepository(
    private val context: Context,
    private val iconsRepository: IconsRepository,
    private val settingsRepository: SettingsRepository
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
                    val activityList =
                        launcherApps.getActivityList(packageName, Process.myUserHandle())

                    // This gets the name dynamically like some Morphe's Apps Change their name
                    val name = if (activityList.isNotEmpty()) {
                        activityList[0].label.toString()
                    } else {
                        val appInfo = packageManager.getApplicationInfo(packageName, 0)
                        appInfo.loadLabel(packageManager).toString()
                    }

                    val shortcutQuery = LauncherApps.ShortcutQuery().apply {
                        setQueryFlags(FLAG_MATCH_DYNAMIC or FLAG_MATCH_MANIFEST or FLAG_MATCH_PINNED)
                        setPackage(packageName)
                    }

                    val isDefaultLauncher = isDefaultLauncher(context)

                    val shortcuts: List<App.Shortcut> = if (isDefaultLauncher) {
                        try {
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
                            e.printStackTrace()
                            emptyList()
                        }
                    } else {
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

                val hiddenApps = getHiddenApps()

                _filteredApps.update { apps.filterNot { it.packageName in hiddenApps } }
            }

//            val blacklistedApps = settingsRepo.settings.value.hiddenApps
//            val displayableApps = newApps.filterNot { blacklistedApps.contains(it.packageName) }

//            _apps.update { displayableApps }

//            iconPacksRepo.fetchIconPacks()
        }
    }

    suspend fun openApp(app: App) {
        withContext(Dispatchers.Default) {
            val intent = packageManager.getLaunchIntentForPackage(app.packageName)
            intent?.let {
                context.startActivity(it)
            }
        }
    }

    suspend fun openShortcut(app: App, shortcut: App.Shortcut) {
        withContext(Dispatchers.Default) {
            launcherApps.startShortcut(
                app.packageName,
                shortcut.id,
                null,
                null,
                Process.myUserHandle()
            )
        }
    }

    suspend fun openAppInfo(app: App) {
        withContext(Dispatchers.Default) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val uri = Uri.fromParts("package", app.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
    }

    suspend fun requestUninstall(app: App) {
        withContext(Dispatchers.Default) {
            val packageUri = "package:${app.packageName}".toUri()
            val intent = Intent(Intent.ACTION_DELETE, packageUri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            context.startActivity(intent)
        }
    }

    // Returns a list with the package names of the hidden apps
    suspend fun getHiddenApps(): Set<String> = withContext(IO) {
        return@withContext settingsRepository.appSettings.value.apps.hiddenApps
    }

    suspend fun setHiddenApps(apps: Set<String>) = withContext(IO) {
        settingsRepository.setHiddenApps(apps)

        fetchApps()
    }
}