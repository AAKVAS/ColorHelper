package feature.palette.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.palette.data.entity.ColorPaletteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaletteDao {
    @Query("SELECT * FROM color_palette ORDER BY created_at")
    suspend fun getPalettes(): List<ColorPaletteEntity>

    @Query("SELECT * FROM color_palette ORDER BY created_at")
    fun observePalettes(): Flow<List<ColorPaletteEntity>>

    @Query("SELECT * FROM color_palette WHERE uid = :uid ORDER BY created_at")
    suspend fun getPaletteByUid(uid: String): ColorPaletteEntity?

    @Query("SELECT * FROM color_palette WHERE uid = :uid ORDER BY created_at")
    fun observePalette(uid: String): Flow<ColorPaletteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePalette(palette: ColorPaletteEntity)

    @Update
    suspend fun updatePalette(palette: ColorPaletteEntity)

    @Query("DELETE FROM color_palette WHERE uid = :uid")
    suspend fun deletePalette(uid: String)
}