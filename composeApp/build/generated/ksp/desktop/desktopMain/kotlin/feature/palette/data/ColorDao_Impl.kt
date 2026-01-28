package feature.palette.`data`

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import feature.palette.data.entity.ColorEntity
import kotlinx.coroutines.flow.Flow
import javax.annotation.processing.Generated
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ColorDao_Impl(
  __db: RoomDatabase,
) : ColorDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfColorEntity: EntityInsertAdapter<ColorEntity>

  private val __updateAdapterOfColorEntity: EntityDeleteOrUpdateAdapter<ColorEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfColorEntity = object : EntityInsertAdapter<ColorEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `colors` (`uid`,`palette_uid`,`value`,`created_at`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ColorEntity) {
        statement.bindText(1, entity.uid)
        statement.bindText(2, entity.paletteUid)
        statement.bindText(3, entity.value)
        statement.bindLong(4, entity.createdAt)
      }
    }
    this.__updateAdapterOfColorEntity = object : EntityDeleteOrUpdateAdapter<ColorEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `colors` SET `uid` = ?,`palette_uid` = ?,`value` = ?,`created_at` = ? WHERE `uid` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ColorEntity) {
        statement.bindText(1, entity.uid)
        statement.bindText(2, entity.paletteUid)
        statement.bindText(3, entity.value)
        statement.bindLong(4, entity.createdAt)
        statement.bindText(5, entity.uid)
      }
    }
  }

  public override suspend fun saveColor(color: ColorEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfColorEntity.insert(_connection, color)
  }

  public override suspend fun updateColor(color: ColorEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfColorEntity.handle(_connection, color)
  }

  public override suspend fun getColorsByPaletteUid(paletteUid: String): List<ColorEntity> {
    val _sql: String = "SELECT * FROM colors WHERE palette_uid = ? ORDER BY created_at"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, paletteUid)
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfPaletteUid: Int = getColumnIndexOrThrow(_stmt, "palette_uid")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _result: MutableList<ColorEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ColorEntity
          val _tmpUid: String
          _tmpUid = _stmt.getText(_columnIndexOfUid)
          val _tmpPaletteUid: String
          _tmpPaletteUid = _stmt.getText(_columnIndexOfPaletteUid)
          val _tmpValue: String
          _tmpValue = _stmt.getText(_columnIndexOfValue)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = ColorEntity(_tmpUid,_tmpPaletteUid,_tmpValue,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeColorsByPaletteUid(paletteUid: String): Flow<List<ColorEntity>> {
    val _sql: String = "SELECT * FROM colors WHERE palette_uid = ? ORDER BY created_at"
    return createFlow(__db, false, arrayOf("colors")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, paletteUid)
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfPaletteUid: Int = getColumnIndexOrThrow(_stmt, "palette_uid")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _result: MutableList<ColorEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ColorEntity
          val _tmpUid: String
          _tmpUid = _stmt.getText(_columnIndexOfUid)
          val _tmpPaletteUid: String
          _tmpPaletteUid = _stmt.getText(_columnIndexOfPaletteUid)
          val _tmpValue: String
          _tmpValue = _stmt.getText(_columnIndexOfValue)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = ColorEntity(_tmpUid,_tmpPaletteUid,_tmpValue,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteColorByUid(uid: String) {
    val _sql: String = "DELETE FROM colors WHERE uid = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, uid)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
