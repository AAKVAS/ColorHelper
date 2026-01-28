package feature.palette.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.palette.data.entity.ColorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorDao {
    @Query("SELECT * FROM colors WHERE palette_uid = :paletteUid ORDER BY created_at")
    suspend fun getColorsByPaletteUid(paletteUid: String): List<ColorEntity>

    @Query("SELECT * FROM colors WHERE palette_uid = :paletteUid ORDER BY created_at")
    fun observeColorsByPaletteUid(paletteUid: String): Flow<List<ColorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveColor(color: ColorEntity)

    @Update
    suspend fun updateColor(color: ColorEntity)

    @Query("DELETE FROM colors WHERE uid = :uid")
    suspend fun deleteColorByUid(uid: String)
}