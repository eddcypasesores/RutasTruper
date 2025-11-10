package com.trupercontrolEdwin.app.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.trupercontrolEdwin.app.data.dao.FacturaDao;
import com.trupercontrolEdwin.app.data.dao.FacturaDao_Impl;
import com.trupercontrolEdwin.app.data.dao.FolioDao;
import com.trupercontrolEdwin.app.data.dao.FolioDao_Impl;
import com.trupercontrolEdwin.app.data.dao.PagoDao;
import com.trupercontrolEdwin.app.data.dao.PagoDao_Impl;
import com.trupercontrolEdwin.app.data.dao.RutaDao;
import com.trupercontrolEdwin.app.data.dao.RutaDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile RutaDao _rutaDao;

  private volatile FolioDao _folioDao;

  private volatile FacturaDao _facturaDao;

  private volatile PagoDao _pagoDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `rutas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `fecha` TEXT, `fotoListadoUri` TEXT, `foliosEsperados` TEXT, `foliosRecibidosPdf` TEXT, `notas` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `folios` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `rutaId` INTEGER NOT NULL, `folioTruper` TEXT NOT NULL, `nombreEstablecimiento` TEXT, `direccion` TEXT, `tipoFachada` TEXT, `m2Reportados` REAL, `m2Final` REAL, `figuras` INTEGER, `tarifaTipo` TEXT, `estado` TEXT NOT NULL, `observaciones` TEXT, `solicitudPdfUri` TEXT, `facturaPdfUri` TEXT, `facturaXmlUri` TEXT, `validacionMensaje` TEXT, `validacionFotosUris` TEXT, `cambioTexto` TEXT, `facturacionExcelUri` TEXT, `acuseCancelacionUri` TEXT, `documentoPagoUri` TEXT, `listadoCoincide` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `facturas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `folioId` INTEGER NOT NULL, `folioFactura` TEXT NOT NULL, `folioFacturaNum` INTEGER, `fechaEmision` TEXT, `subtotal` REAL NOT NULL, `iva` REAL NOT NULL, `total` REAL NOT NULL, `estatus` TEXT NOT NULL, `motivoCancelacion` TEXT, `folioReemplaza` TEXT, `pdfUri` TEXT, `xmlUri` TEXT, `observaciones` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pagos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `facturaId` INTEGER NOT NULL, `fechaPago` TEXT, `monto` REAL NOT NULL, `documentoPagoUri` TEXT, `referenciaBanco` TEXT, `tipo` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '18d80bba1e268aa8d8fa572cd1977b39')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `rutas`");
        db.execSQL("DROP TABLE IF EXISTS `folios`");
        db.execSQL("DROP TABLE IF EXISTS `facturas`");
        db.execSQL("DROP TABLE IF EXISTS `pagos`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsRutas = new HashMap<String, TableInfo.Column>(7);
        _columnsRutas.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRutas.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRutas.put("fecha", new TableInfo.Column("fecha", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRutas.put("fotoListadoUri", new TableInfo.Column("fotoListadoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRutas.put("foliosEsperados", new TableInfo.Column("foliosEsperados", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRutas.put("foliosRecibidosPdf", new TableInfo.Column("foliosRecibidosPdf", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRutas.put("notas", new TableInfo.Column("notas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRutas = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRutas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRutas = new TableInfo("rutas", _columnsRutas, _foreignKeysRutas, _indicesRutas);
        final TableInfo _existingRutas = TableInfo.read(db, "rutas");
        if (!_infoRutas.equals(_existingRutas)) {
          return new RoomOpenHelper.ValidationResult(false, "rutas(com.trupercontrolEdwin.app.data.entities.Ruta).\n"
                  + " Expected:\n" + _infoRutas + "\n"
                  + " Found:\n" + _existingRutas);
        }
        final HashMap<String, TableInfo.Column> _columnsFolios = new HashMap<String, TableInfo.Column>(22);
        _columnsFolios.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("rutaId", new TableInfo.Column("rutaId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("folioTruper", new TableInfo.Column("folioTruper", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("nombreEstablecimiento", new TableInfo.Column("nombreEstablecimiento", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("direccion", new TableInfo.Column("direccion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("tipoFachada", new TableInfo.Column("tipoFachada", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("m2Reportados", new TableInfo.Column("m2Reportados", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("m2Final", new TableInfo.Column("m2Final", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("figuras", new TableInfo.Column("figuras", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("tarifaTipo", new TableInfo.Column("tarifaTipo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("estado", new TableInfo.Column("estado", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("observaciones", new TableInfo.Column("observaciones", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("solicitudPdfUri", new TableInfo.Column("solicitudPdfUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("facturaPdfUri", new TableInfo.Column("facturaPdfUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("facturaXmlUri", new TableInfo.Column("facturaXmlUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("validacionMensaje", new TableInfo.Column("validacionMensaje", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("validacionFotosUris", new TableInfo.Column("validacionFotosUris", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("cambioTexto", new TableInfo.Column("cambioTexto", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("facturacionExcelUri", new TableInfo.Column("facturacionExcelUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("acuseCancelacionUri", new TableInfo.Column("acuseCancelacionUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("documentoPagoUri", new TableInfo.Column("documentoPagoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolios.put("listadoCoincide", new TableInfo.Column("listadoCoincide", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFolios = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFolios = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFolios = new TableInfo("folios", _columnsFolios, _foreignKeysFolios, _indicesFolios);
        final TableInfo _existingFolios = TableInfo.read(db, "folios");
        if (!_infoFolios.equals(_existingFolios)) {
          return new RoomOpenHelper.ValidationResult(false, "folios(com.trupercontrolEdwin.app.data.entities.Folio).\n"
                  + " Expected:\n" + _infoFolios + "\n"
                  + " Found:\n" + _existingFolios);
        }
        final HashMap<String, TableInfo.Column> _columnsFacturas = new HashMap<String, TableInfo.Column>(14);
        _columnsFacturas.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("folioId", new TableInfo.Column("folioId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("folioFactura", new TableInfo.Column("folioFactura", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("folioFacturaNum", new TableInfo.Column("folioFacturaNum", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("fechaEmision", new TableInfo.Column("fechaEmision", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("subtotal", new TableInfo.Column("subtotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("iva", new TableInfo.Column("iva", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("total", new TableInfo.Column("total", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("estatus", new TableInfo.Column("estatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("motivoCancelacion", new TableInfo.Column("motivoCancelacion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("folioReemplaza", new TableInfo.Column("folioReemplaza", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("pdfUri", new TableInfo.Column("pdfUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("xmlUri", new TableInfo.Column("xmlUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacturas.put("observaciones", new TableInfo.Column("observaciones", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFacturas = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFacturas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFacturas = new TableInfo("facturas", _columnsFacturas, _foreignKeysFacturas, _indicesFacturas);
        final TableInfo _existingFacturas = TableInfo.read(db, "facturas");
        if (!_infoFacturas.equals(_existingFacturas)) {
          return new RoomOpenHelper.ValidationResult(false, "facturas(com.trupercontrolEdwin.app.data.entities.Factura).\n"
                  + " Expected:\n" + _infoFacturas + "\n"
                  + " Found:\n" + _existingFacturas);
        }
        final HashMap<String, TableInfo.Column> _columnsPagos = new HashMap<String, TableInfo.Column>(7);
        _columnsPagos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPagos.put("facturaId", new TableInfo.Column("facturaId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPagos.put("fechaPago", new TableInfo.Column("fechaPago", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPagos.put("monto", new TableInfo.Column("monto", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPagos.put("documentoPagoUri", new TableInfo.Column("documentoPagoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPagos.put("referenciaBanco", new TableInfo.Column("referenciaBanco", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPagos.put("tipo", new TableInfo.Column("tipo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPagos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPagos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPagos = new TableInfo("pagos", _columnsPagos, _foreignKeysPagos, _indicesPagos);
        final TableInfo _existingPagos = TableInfo.read(db, "pagos");
        if (!_infoPagos.equals(_existingPagos)) {
          return new RoomOpenHelper.ValidationResult(false, "pagos(com.trupercontrolEdwin.app.data.entities.Pago).\n"
                  + " Expected:\n" + _infoPagos + "\n"
                  + " Found:\n" + _existingPagos);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "18d80bba1e268aa8d8fa572cd1977b39", "54997bde6d990ba847c434514da974b4");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "rutas","folios","facturas","pagos");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `rutas`");
      _db.execSQL("DELETE FROM `folios`");
      _db.execSQL("DELETE FROM `facturas`");
      _db.execSQL("DELETE FROM `pagos`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RutaDao.class, RutaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FolioDao.class, FolioDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FacturaDao.class, FacturaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PagoDao.class, PagoDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public RutaDao rutaDao() {
    if (_rutaDao != null) {
      return _rutaDao;
    } else {
      synchronized(this) {
        if(_rutaDao == null) {
          _rutaDao = new RutaDao_Impl(this);
        }
        return _rutaDao;
      }
    }
  }

  @Override
  public FolioDao folioDao() {
    if (_folioDao != null) {
      return _folioDao;
    } else {
      synchronized(this) {
        if(_folioDao == null) {
          _folioDao = new FolioDao_Impl(this);
        }
        return _folioDao;
      }
    }
  }

  @Override
  public FacturaDao facturaDao() {
    if (_facturaDao != null) {
      return _facturaDao;
    } else {
      synchronized(this) {
        if(_facturaDao == null) {
          _facturaDao = new FacturaDao_Impl(this);
        }
        return _facturaDao;
      }
    }
  }

  @Override
  public PagoDao pagoDao() {
    if (_pagoDao != null) {
      return _pagoDao;
    } else {
      synchronized(this) {
        if(_pagoDao == null) {
          _pagoDao = new PagoDao_Impl(this);
        }
        return _pagoDao;
      }
    }
  }
}
