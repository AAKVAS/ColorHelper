package data

import androidx.room.Database
import androidx.room.RoomDatabase
import data.AppDatabase.Companion.VERSION
import feature.palette.data.ColorDao
import feature.palette.data.PaletteDao
import feature.palette.data.entity.ColorEntity
import feature.palette.data.entity.ColorPaletteEntity

@Database(
    entities = [
        ColorPaletteEntity::class,
        ColorEntity::class
    ],
    version = VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPaletteDao(): PaletteDao
    abstract fun getColorDao(): ColorDao

    companion object {
        const val VERSION = 1
        const val DB_NAME = "colorHelper.db"
    }
}