package feature.palette.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "colors",
    foreignKeys = [
        ForeignKey(
            entity = ColorPaletteEntity::class,
            parentColumns = ["uid"],
            childColumns = ["palette_uid"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("palette_uid")]
)
data class ColorEntity(
    @PrimaryKey(autoGenerate = false)
    val uid: String,
    @ColumnInfo(name = "palette_uid")
    val paletteUid: String,
    val value: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
