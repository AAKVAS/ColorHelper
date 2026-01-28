package feature.palette.`data`

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import feature.palette.data.entity.ColorPaletteEntity
import kotlinx.coroutines.flow.Flow
import javax.annotation.processing.Generated
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PaletteDao_Impl(
  __db: RoomDatabase,
) : PaletteDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfColorPaletteEntity: EntityInsertAdapter<ColorPaletteEntity>

  private val __updateAdapterOfColorPaletteEntity: EntityDeleteOrUpdateAdapter<ColorPaletteEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfColorPaletteEntity = object : EntityInsertAdapter<ColorPaletteEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `color_palette` (`uid`,`name`,`created_at`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ColorPaletteEntity) {
        statement.bindText(1, entity.uid)
        statement.bindText(2, entity.name)
        statement.bindLong(3, entity.createdAt)
      }
    }
    this.__updateAdapterOfColorPaletteEntity = object : EntityDeleteOrUpdateAdapter<ColorPaletteEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `color_palette` SET `uid` = ?,`name` = ?,`created_at` = ? WHERE `uid` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ColorPaletteEntity) {
        statement.bindText(1, entity.uid)
        statement.bindText(2, entity.name)
        statement.bindLong(3, entity.createdAt)
        statement.bindText(4, entity.uid)
      }
    }
  }

  public override suspend fun savePalette(palette: ColorPaletteEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfColorPaletteEntity.insert(_connection, palette)
  }

  public override suspend fun updatePalette(palette: ColorPaletteEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfColorPaletteEntity.handle(_connection, palette)
  }

  public override suspend fun getPalettes(): List<ColorPaletteEntity> {
    val _sql: String = "SELECT * FROM color_palette ORDER BY created_at"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _result: MutableList<ColorPaletteEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ColorPaletteEntity
          val _tmpUid: String
          _tmpUid = _stmt.getText(_columnIndexOfUid)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = ColorPaletteEntity(_tmpUid,_tmpName,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observePalettes(): Flow<List<ColorPaletteEntity>> {
    val _sql: String = "SELECT * FROM color_palette ORDER BY created_at"
    return createFlow(__db, false, arrayOf("color_palette")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _result: MutableList<ColorPaletteEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ColorPaletteEntity
          val _tmpUid: String
          _tmpUid = _stmt.getText(_columnIndexOfUid)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = ColorPaletteEntity(_tmpUid,_tmpName,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getPaletteByUid(uid: String): ColorPaletteEntity? {
    val _sql: String = "SELECT * FROM color_palette WHERE uid = ? ORDER BY created_at"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, uid)
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _result: ColorPaletteEntity?
        if (_stmt.step()) {
          val _tmpUid: String
          _tmpUid = _stmt.getText(_columnIndexOfUid)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result = ColorPaletteEntity(_tmpUid,_tmpName,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observePalette(uid: String): Flow<ColorPaletteEntity?> {
    val _sql: String = "SELECT * FROM color_palette WHERE uid = ? ORDER BY created_at"
    return createFlow(__db, false, arrayOf("color_palette")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, uid)
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _result: ColorPaletteEntity?
        if (_stmt.step()) {
          val _tmpUid: String
          _tmpUid = _stmt.getText(_columnIndexOfUid)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result = ColorPaletteEntity(_tmpUid,_tmpName,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deletePalette(uid: String) {
    val _sql: String = "DELETE FROM color_palette WHERE uid = ?"
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
