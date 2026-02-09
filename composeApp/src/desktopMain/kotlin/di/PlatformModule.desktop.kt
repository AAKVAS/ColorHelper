package di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.AppDatabase
import data.AppDatabase.Companion.DB_NAME
import feature.imageBusket.domain.ImageBusketRepository
import feature.imageBusket.domain.InMemoryImageBusketRepository
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

private val userHome get() = System.getProperty("user.home")
private const val APP_NAME = "ColorHelper"

actual fun getPlatformModule(): Module = module {
    single<AppDatabase> {
        getDatabaseBuilder().build()
    }

    single<ImageBusketRepository> { InMemoryImageBusketRepository() }
}

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val parentFolder = File(userHome + "/${APP_NAME}")
    if (!parentFolder.exists()) {
        parentFolder.mkdirs()
    }
    val databasePath = File(parentFolder, DB_NAME)

    return Room.databaseBuilder<AppDatabase>(
        name = databasePath.absolutePath,
    )
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
}