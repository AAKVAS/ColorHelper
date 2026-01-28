package `data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import feature.palette.`data`.ColorDao
import feature.palette.`data`.ColorDao_Impl
import feature.palette.`data`.PaletteDao
import feature.palette.`data`.PaletteDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _paletteDao: Lazy<PaletteDao> = lazy {
    PaletteDao_Impl(this)
  }

  private val _colorDao: Lazy<ColorDao> = lazy {
    ColorDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "850c7e31ce0b5bf307edfd4849008817", "e66e8315509dad3bfedaf469a31799eb") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `color_palette` (`uid` TEXT NOT NULL, `name` TEXT NOT NULL, `created_at` INTEGER NOT NULL, PRIMARY KEY(`uid`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `colors` (`uid` TEXT NOT NULL, `palette_uid` TEXT NOT NULL, `value` TEXT NOT NULL, `created_at` INTEGER NOT NULL, PRIMARY KEY(`uid`), FOREIGN KEY(`palette_uid`) REFERENCES `color_palette`(`uid`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_colors_palette_uid` ON `colors` (`palette_uid`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '850c7e31ce0b5bf307edfd4849008817')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `color_palette`")
        connection.execSQL("DROP TABLE IF EXISTS `colors`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsColorPalette: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsColorPalette.put("uid", TableInfo.Column("uid", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsColorPalette.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsColorPalette.put("created_at", TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysColorPalette: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesColorPalette: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoColorPalette: TableInfo = TableInfo("color_palette", _columnsColorPalette, _foreignKeysColorPalette, _indicesColorPalette)
        val _existingColorPalette: TableInfo = read(connection, "color_palette")
        if (!_infoColorPalette.equals(_existingColorPalette)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |color_palette(feature.palette.data.entity.ColorPaletteEntity).
              | Expected:
              |""".trimMargin() + _infoColorPalette + """
              |
              | Found:
              |""".trimMargin() + _existingColorPalette)
        }
        val _columnsColors: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsColors.put("uid", TableInfo.Column("uid", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsColors.put("palette_uid", TableInfo.Column("palette_uid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsColors.put("value", TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsColors.put("created_at", TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysColors: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysColors.add(TableInfo.ForeignKey("color_palette", "CASCADE", "NO ACTION", listOf("palette_uid"), listOf("uid")))
        val _indicesColors: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesColors.add(TableInfo.Index("index_colors_palette_uid", false, listOf("palette_uid"), listOf("ASC")))
        val _infoColors: TableInfo = TableInfo("colors", _columnsColors, _foreignKeysColors, _indicesColors)
        val _existingColors: TableInfo = read(connection, "colors")
        if (!_infoColors.equals(_existingColors)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |colors(feature.palette.data.entity.ColorEntity).
              | Expected:
              |""".trimMargin() + _infoColors + """
              |
              | Found:
              |""".trimMargin() + _existingColors)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "color_palette", "colors")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(PaletteDao::class, PaletteDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ColorDao::class, ColorDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun getPaletteDao(): PaletteDao = _paletteDao.value

  public override fun getColorDao(): ColorDao = _colorDao.value
}
