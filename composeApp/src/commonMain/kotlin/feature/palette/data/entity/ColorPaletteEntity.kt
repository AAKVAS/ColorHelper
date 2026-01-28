package feature.palette.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "color_palette")
data class ColorPaletteEntity(
    @PrimaryKey(autoGenerate = false)
    val uid: String,
    val name: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)