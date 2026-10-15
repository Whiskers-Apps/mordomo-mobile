package org.whiskersapps.mordomo.mobile.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.Bookmark
import org.whiskersapps.mordomo.mobile.core.features.bookmarks.BookmarkDao
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEngine
import org.whiskersapps.mordomo.mobile.core.features.search_engines.SearchEnginesDao
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDo
import org.whiskersapps.mordomo.mobile.core.features.todo.ToDoDao

@Database(
    entities = [
        SearchEngine::class,
        Bookmark::class,
        ToDo::class
    ],
    version = 2
)
abstract class AppDB : RoomDatabase() {
    abstract fun searchEnginesDao(): SearchEnginesDao
    abstract fun bookmarksDao(): BookmarkDao
    abstract fun toDoDao(): ToDoDao
}

@Volatile
private var INSTANCE: AppDB? = null

fun getDB(context: Context): AppDB {
    return INSTANCE ?: synchronized(context) {
        val instance = Room.databaseBuilder(context.applicationContext, AppDB::class.java, "app")
            .addMigrations(MIGRATION_1_2)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        INSTANCE?.searchEnginesDao()?.insertMultiple(
                            listOf(
                                SearchEngine(
                                    name = "Google",
                                    query = "https://www.google.com/search?q=%s"
                                ),
                                SearchEngine(
                                    name = "DuckDuckGo",
                                    query = "https://duckduckgo.com/?q=%s"
                                ),
                                SearchEngine(
                                    name = "Brave",
                                    query = "https://search.brave.com/search?q=%s"
                                ),
                                SearchEngine(
                                    name = "StartPage",
                                    query = "https://www.startpage.com/sp/search?query=%s"
                                ),
                                SearchEngine(
                                    name = "Qwant",
                                    query = "https://www.qwant.com/?q=%s"
                                ),
                                SearchEngine(
                                    name = "Ecosia",
                                    query = "https://www.ecosia.org/search?q=%s",
                                ),
                            )
                        )
                    }
                }
            }).build()

        INSTANCE = instance
        instance
    }
}