package com.trupercontrolEdwin.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.trupercontrolEdwin.app.data.entities.Folio;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FolioDao_Impl implements FolioDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Folio> __insertionAdapterOfFolio;

  private final EntityDeletionOrUpdateAdapter<Folio> __updateAdapterOfFolio;

  private final SharedSQLiteStatement __preparedStmtOfUpdateEstado;

  public FolioDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFolio = new EntityInsertionAdapter<Folio>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `folios` (`id`,`rutaId`,`folioTruper`,`nombreEstablecimiento`,`direccion`,`tipoFachada`,`m2Reportados`,`m2Final`,`figuras`,`tarifaTipo`,`estado`,`observaciones`,`cambioTexto`,`tipoSolicitud`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Folio entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getRutaId());
        statement.bindString(3, entity.getFolioTruper());
        if (entity.getNombreEstablecimiento() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNombreEstablecimiento());
        }
        if (entity.getDireccion() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDireccion());
        }
        if (entity.getTipoFachada() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTipoFachada());
        }
        if (entity.getM2Reportados() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getM2Reportados());
        }
        if (entity.getM2Final() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getM2Final());
        }
        if (entity.getFiguras() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getFiguras());
        }
        if (entity.getTarifaTipo() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTarifaTipo());
        }
        statement.bindString(11, entity.getEstado());
        if (entity.getObservaciones() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getObservaciones());
        }
        if (entity.getCambioTexto() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getCambioTexto());
        }
        if (entity.getTipoSolicitud() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getTipoSolicitud());
        }
      }
    };
    this.__updateAdapterOfFolio = new EntityDeletionOrUpdateAdapter<Folio>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `folios` SET `id` = ?,`rutaId` = ?,`folioTruper` = ?,`nombreEstablecimiento` = ?,`direccion` = ?,`tipoFachada` = ?,`m2Reportados` = ?,`m2Final` = ?,`figuras` = ?,`tarifaTipo` = ?,`estado` = ?,`observaciones` = ?,`cambioTexto` = ?,`tipoSolicitud` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Folio entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getRutaId());
        statement.bindString(3, entity.getFolioTruper());
        if (entity.getNombreEstablecimiento() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNombreEstablecimiento());
        }
        if (entity.getDireccion() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDireccion());
        }
        if (entity.getTipoFachada() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTipoFachada());
        }
        if (entity.getM2Reportados() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getM2Reportados());
        }
        if (entity.getM2Final() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getM2Final());
        }
        if (entity.getFiguras() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getFiguras());
        }
        if (entity.getTarifaTipo() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTarifaTipo());
        }
        statement.bindString(11, entity.getEstado());
        if (entity.getObservaciones() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getObservaciones());
        }
        if (entity.getCambioTexto() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getCambioTexto());
        }
        if (entity.getTipoSolicitud() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getTipoSolicitud());
        }
        statement.bindLong(15, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateEstado = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE folios SET estado = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Folio folio, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFolio.insertAndReturnId(folio);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Folio folio, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __updateAdapterOfFolio.handle(folio);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEstado(final long folioId, final String estado,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateEstado.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, estado);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, folioId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateEstado.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Folio>> getByRuta(final long rutaId) {
    final String _sql = "SELECT * FROM folios WHERE rutaId = ? ORDER BY folioTruper ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rutaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"folios"}, new Callable<List<Folio>>() {
      @Override
      @NonNull
      public List<Folio> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRutaId = CursorUtil.getColumnIndexOrThrow(_cursor, "rutaId");
          final int _cursorIndexOfFolioTruper = CursorUtil.getColumnIndexOrThrow(_cursor, "folioTruper");
          final int _cursorIndexOfNombreEstablecimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEstablecimiento");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTipoFachada = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoFachada");
          final int _cursorIndexOfM2Reportados = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Reportados");
          final int _cursorIndexOfM2Final = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Final");
          final int _cursorIndexOfFiguras = CursorUtil.getColumnIndexOrThrow(_cursor, "figuras");
          final int _cursorIndexOfTarifaTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tarifaTipo");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCambioTexto = CursorUtil.getColumnIndexOrThrow(_cursor, "cambioTexto");
          final int _cursorIndexOfTipoSolicitud = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoSolicitud");
          final List<Folio> _result = new ArrayList<Folio>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Folio _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpRutaId;
            _tmpRutaId = _cursor.getLong(_cursorIndexOfRutaId);
            final String _tmpFolioTruper;
            _tmpFolioTruper = _cursor.getString(_cursorIndexOfFolioTruper);
            final String _tmpNombreEstablecimiento;
            if (_cursor.isNull(_cursorIndexOfNombreEstablecimiento)) {
              _tmpNombreEstablecimiento = null;
            } else {
              _tmpNombreEstablecimiento = _cursor.getString(_cursorIndexOfNombreEstablecimiento);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTipoFachada;
            if (_cursor.isNull(_cursorIndexOfTipoFachada)) {
              _tmpTipoFachada = null;
            } else {
              _tmpTipoFachada = _cursor.getString(_cursorIndexOfTipoFachada);
            }
            final Double _tmpM2Reportados;
            if (_cursor.isNull(_cursorIndexOfM2Reportados)) {
              _tmpM2Reportados = null;
            } else {
              _tmpM2Reportados = _cursor.getDouble(_cursorIndexOfM2Reportados);
            }
            final Double _tmpM2Final;
            if (_cursor.isNull(_cursorIndexOfM2Final)) {
              _tmpM2Final = null;
            } else {
              _tmpM2Final = _cursor.getDouble(_cursorIndexOfM2Final);
            }
            final Integer _tmpFiguras;
            if (_cursor.isNull(_cursorIndexOfFiguras)) {
              _tmpFiguras = null;
            } else {
              _tmpFiguras = _cursor.getInt(_cursorIndexOfFiguras);
            }
            final String _tmpTarifaTipo;
            if (_cursor.isNull(_cursorIndexOfTarifaTipo)) {
              _tmpTarifaTipo = null;
            } else {
              _tmpTarifaTipo = _cursor.getString(_cursorIndexOfTarifaTipo);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpCambioTexto;
            if (_cursor.isNull(_cursorIndexOfCambioTexto)) {
              _tmpCambioTexto = null;
            } else {
              _tmpCambioTexto = _cursor.getString(_cursorIndexOfCambioTexto);
            }
            final String _tmpTipoSolicitud;
            if (_cursor.isNull(_cursorIndexOfTipoSolicitud)) {
              _tmpTipoSolicitud = null;
            } else {
              _tmpTipoSolicitud = _cursor.getString(_cursorIndexOfTipoSolicitud);
            }
            _item = new Folio(_tmpId,_tmpRutaId,_tmpFolioTruper,_tmpNombreEstablecimiento,_tmpDireccion,_tmpTipoFachada,_tmpM2Reportados,_tmpM2Final,_tmpFiguras,_tmpTarifaTipo,_tmpEstado,_tmpObservaciones,_tmpCambioTexto,_tmpTipoSolicitud);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getByRutaSimple(final long rutaId,
      final Continuation<? super List<Folio>> $completion) {
    final String _sql = "SELECT * FROM folios WHERE rutaId = ? ORDER BY folioTruper ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rutaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Folio>>() {
      @Override
      @NonNull
      public List<Folio> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRutaId = CursorUtil.getColumnIndexOrThrow(_cursor, "rutaId");
          final int _cursorIndexOfFolioTruper = CursorUtil.getColumnIndexOrThrow(_cursor, "folioTruper");
          final int _cursorIndexOfNombreEstablecimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEstablecimiento");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTipoFachada = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoFachada");
          final int _cursorIndexOfM2Reportados = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Reportados");
          final int _cursorIndexOfM2Final = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Final");
          final int _cursorIndexOfFiguras = CursorUtil.getColumnIndexOrThrow(_cursor, "figuras");
          final int _cursorIndexOfTarifaTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tarifaTipo");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCambioTexto = CursorUtil.getColumnIndexOrThrow(_cursor, "cambioTexto");
          final int _cursorIndexOfTipoSolicitud = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoSolicitud");
          final List<Folio> _result = new ArrayList<Folio>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Folio _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpRutaId;
            _tmpRutaId = _cursor.getLong(_cursorIndexOfRutaId);
            final String _tmpFolioTruper;
            _tmpFolioTruper = _cursor.getString(_cursorIndexOfFolioTruper);
            final String _tmpNombreEstablecimiento;
            if (_cursor.isNull(_cursorIndexOfNombreEstablecimiento)) {
              _tmpNombreEstablecimiento = null;
            } else {
              _tmpNombreEstablecimiento = _cursor.getString(_cursorIndexOfNombreEstablecimiento);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTipoFachada;
            if (_cursor.isNull(_cursorIndexOfTipoFachada)) {
              _tmpTipoFachada = null;
            } else {
              _tmpTipoFachada = _cursor.getString(_cursorIndexOfTipoFachada);
            }
            final Double _tmpM2Reportados;
            if (_cursor.isNull(_cursorIndexOfM2Reportados)) {
              _tmpM2Reportados = null;
            } else {
              _tmpM2Reportados = _cursor.getDouble(_cursorIndexOfM2Reportados);
            }
            final Double _tmpM2Final;
            if (_cursor.isNull(_cursorIndexOfM2Final)) {
              _tmpM2Final = null;
            } else {
              _tmpM2Final = _cursor.getDouble(_cursorIndexOfM2Final);
            }
            final Integer _tmpFiguras;
            if (_cursor.isNull(_cursorIndexOfFiguras)) {
              _tmpFiguras = null;
            } else {
              _tmpFiguras = _cursor.getInt(_cursorIndexOfFiguras);
            }
            final String _tmpTarifaTipo;
            if (_cursor.isNull(_cursorIndexOfTarifaTipo)) {
              _tmpTarifaTipo = null;
            } else {
              _tmpTarifaTipo = _cursor.getString(_cursorIndexOfTarifaTipo);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpCambioTexto;
            if (_cursor.isNull(_cursorIndexOfCambioTexto)) {
              _tmpCambioTexto = null;
            } else {
              _tmpCambioTexto = _cursor.getString(_cursorIndexOfCambioTexto);
            }
            final String _tmpTipoSolicitud;
            if (_cursor.isNull(_cursorIndexOfTipoSolicitud)) {
              _tmpTipoSolicitud = null;
            } else {
              _tmpTipoSolicitud = _cursor.getString(_cursorIndexOfTipoSolicitud);
            }
            _item = new Folio(_tmpId,_tmpRutaId,_tmpFolioTruper,_tmpNombreEstablecimiento,_tmpDireccion,_tmpTipoFachada,_tmpM2Reportados,_tmpM2Final,_tmpFiguras,_tmpTarifaTipo,_tmpEstado,_tmpObservaciones,_tmpCambioTexto,_tmpTipoSolicitud);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllSimple(final Continuation<? super List<Folio>> $completion) {
    final String _sql = "SELECT * FROM folios";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Folio>>() {
      @Override
      @NonNull
      public List<Folio> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRutaId = CursorUtil.getColumnIndexOrThrow(_cursor, "rutaId");
          final int _cursorIndexOfFolioTruper = CursorUtil.getColumnIndexOrThrow(_cursor, "folioTruper");
          final int _cursorIndexOfNombreEstablecimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEstablecimiento");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTipoFachada = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoFachada");
          final int _cursorIndexOfM2Reportados = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Reportados");
          final int _cursorIndexOfM2Final = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Final");
          final int _cursorIndexOfFiguras = CursorUtil.getColumnIndexOrThrow(_cursor, "figuras");
          final int _cursorIndexOfTarifaTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tarifaTipo");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCambioTexto = CursorUtil.getColumnIndexOrThrow(_cursor, "cambioTexto");
          final int _cursorIndexOfTipoSolicitud = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoSolicitud");
          final List<Folio> _result = new ArrayList<Folio>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Folio _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpRutaId;
            _tmpRutaId = _cursor.getLong(_cursorIndexOfRutaId);
            final String _tmpFolioTruper;
            _tmpFolioTruper = _cursor.getString(_cursorIndexOfFolioTruper);
            final String _tmpNombreEstablecimiento;
            if (_cursor.isNull(_cursorIndexOfNombreEstablecimiento)) {
              _tmpNombreEstablecimiento = null;
            } else {
              _tmpNombreEstablecimiento = _cursor.getString(_cursorIndexOfNombreEstablecimiento);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTipoFachada;
            if (_cursor.isNull(_cursorIndexOfTipoFachada)) {
              _tmpTipoFachada = null;
            } else {
              _tmpTipoFachada = _cursor.getString(_cursorIndexOfTipoFachada);
            }
            final Double _tmpM2Reportados;
            if (_cursor.isNull(_cursorIndexOfM2Reportados)) {
              _tmpM2Reportados = null;
            } else {
              _tmpM2Reportados = _cursor.getDouble(_cursorIndexOfM2Reportados);
            }
            final Double _tmpM2Final;
            if (_cursor.isNull(_cursorIndexOfM2Final)) {
              _tmpM2Final = null;
            } else {
              _tmpM2Final = _cursor.getDouble(_cursorIndexOfM2Final);
            }
            final Integer _tmpFiguras;
            if (_cursor.isNull(_cursorIndexOfFiguras)) {
              _tmpFiguras = null;
            } else {
              _tmpFiguras = _cursor.getInt(_cursorIndexOfFiguras);
            }
            final String _tmpTarifaTipo;
            if (_cursor.isNull(_cursorIndexOfTarifaTipo)) {
              _tmpTarifaTipo = null;
            } else {
              _tmpTarifaTipo = _cursor.getString(_cursorIndexOfTarifaTipo);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpCambioTexto;
            if (_cursor.isNull(_cursorIndexOfCambioTexto)) {
              _tmpCambioTexto = null;
            } else {
              _tmpCambioTexto = _cursor.getString(_cursorIndexOfCambioTexto);
            }
            final String _tmpTipoSolicitud;
            if (_cursor.isNull(_cursorIndexOfTipoSolicitud)) {
              _tmpTipoSolicitud = null;
            } else {
              _tmpTipoSolicitud = _cursor.getString(_cursorIndexOfTipoSolicitud);
            }
            _item = new Folio(_tmpId,_tmpRutaId,_tmpFolioTruper,_tmpNombreEstablecimiento,_tmpDireccion,_tmpTipoFachada,_tmpM2Reportados,_tmpM2Final,_tmpFiguras,_tmpTarifaTipo,_tmpEstado,_tmpObservaciones,_tmpCambioTexto,_tmpTipoSolicitud);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendientes(final Continuation<? super List<Folio>> $completion) {
    final String _sql = "SELECT * FROM folios WHERE estado NOT IN ('Pagado', 'Cancelado')";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Folio>>() {
      @Override
      @NonNull
      public List<Folio> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRutaId = CursorUtil.getColumnIndexOrThrow(_cursor, "rutaId");
          final int _cursorIndexOfFolioTruper = CursorUtil.getColumnIndexOrThrow(_cursor, "folioTruper");
          final int _cursorIndexOfNombreEstablecimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEstablecimiento");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTipoFachada = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoFachada");
          final int _cursorIndexOfM2Reportados = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Reportados");
          final int _cursorIndexOfM2Final = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Final");
          final int _cursorIndexOfFiguras = CursorUtil.getColumnIndexOrThrow(_cursor, "figuras");
          final int _cursorIndexOfTarifaTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tarifaTipo");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCambioTexto = CursorUtil.getColumnIndexOrThrow(_cursor, "cambioTexto");
          final int _cursorIndexOfTipoSolicitud = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoSolicitud");
          final List<Folio> _result = new ArrayList<Folio>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Folio _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpRutaId;
            _tmpRutaId = _cursor.getLong(_cursorIndexOfRutaId);
            final String _tmpFolioTruper;
            _tmpFolioTruper = _cursor.getString(_cursorIndexOfFolioTruper);
            final String _tmpNombreEstablecimiento;
            if (_cursor.isNull(_cursorIndexOfNombreEstablecimiento)) {
              _tmpNombreEstablecimiento = null;
            } else {
              _tmpNombreEstablecimiento = _cursor.getString(_cursorIndexOfNombreEstablecimiento);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTipoFachada;
            if (_cursor.isNull(_cursorIndexOfTipoFachada)) {
              _tmpTipoFachada = null;
            } else {
              _tmpTipoFachada = _cursor.getString(_cursorIndexOfTipoFachada);
            }
            final Double _tmpM2Reportados;
            if (_cursor.isNull(_cursorIndexOfM2Reportados)) {
              _tmpM2Reportados = null;
            } else {
              _tmpM2Reportados = _cursor.getDouble(_cursorIndexOfM2Reportados);
            }
            final Double _tmpM2Final;
            if (_cursor.isNull(_cursorIndexOfM2Final)) {
              _tmpM2Final = null;
            } else {
              _tmpM2Final = _cursor.getDouble(_cursorIndexOfM2Final);
            }
            final Integer _tmpFiguras;
            if (_cursor.isNull(_cursorIndexOfFiguras)) {
              _tmpFiguras = null;
            } else {
              _tmpFiguras = _cursor.getInt(_cursorIndexOfFiguras);
            }
            final String _tmpTarifaTipo;
            if (_cursor.isNull(_cursorIndexOfTarifaTipo)) {
              _tmpTarifaTipo = null;
            } else {
              _tmpTarifaTipo = _cursor.getString(_cursorIndexOfTarifaTipo);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpCambioTexto;
            if (_cursor.isNull(_cursorIndexOfCambioTexto)) {
              _tmpCambioTexto = null;
            } else {
              _tmpCambioTexto = _cursor.getString(_cursorIndexOfCambioTexto);
            }
            final String _tmpTipoSolicitud;
            if (_cursor.isNull(_cursorIndexOfTipoSolicitud)) {
              _tmpTipoSolicitud = null;
            } else {
              _tmpTipoSolicitud = _cursor.getString(_cursorIndexOfTipoSolicitud);
            }
            _item = new Folio(_tmpId,_tmpRutaId,_tmpFolioTruper,_tmpNombreEstablecimiento,_tmpDireccion,_tmpTipoFachada,_tmpM2Reportados,_tmpM2Final,_tmpFiguras,_tmpTarifaTipo,_tmpEstado,_tmpObservaciones,_tmpCambioTexto,_tmpTipoSolicitud);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findByFolioTruper(final String folioTruper,
      final Continuation<? super Folio> $completion) {
    final String _sql = "SELECT * FROM folios WHERE folioTruper = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, folioTruper);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Folio>() {
      @Override
      @Nullable
      public Folio call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRutaId = CursorUtil.getColumnIndexOrThrow(_cursor, "rutaId");
          final int _cursorIndexOfFolioTruper = CursorUtil.getColumnIndexOrThrow(_cursor, "folioTruper");
          final int _cursorIndexOfNombreEstablecimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEstablecimiento");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTipoFachada = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoFachada");
          final int _cursorIndexOfM2Reportados = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Reportados");
          final int _cursorIndexOfM2Final = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Final");
          final int _cursorIndexOfFiguras = CursorUtil.getColumnIndexOrThrow(_cursor, "figuras");
          final int _cursorIndexOfTarifaTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tarifaTipo");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCambioTexto = CursorUtil.getColumnIndexOrThrow(_cursor, "cambioTexto");
          final int _cursorIndexOfTipoSolicitud = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoSolicitud");
          final Folio _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpRutaId;
            _tmpRutaId = _cursor.getLong(_cursorIndexOfRutaId);
            final String _tmpFolioTruper;
            _tmpFolioTruper = _cursor.getString(_cursorIndexOfFolioTruper);
            final String _tmpNombreEstablecimiento;
            if (_cursor.isNull(_cursorIndexOfNombreEstablecimiento)) {
              _tmpNombreEstablecimiento = null;
            } else {
              _tmpNombreEstablecimiento = _cursor.getString(_cursorIndexOfNombreEstablecimiento);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTipoFachada;
            if (_cursor.isNull(_cursorIndexOfTipoFachada)) {
              _tmpTipoFachada = null;
            } else {
              _tmpTipoFachada = _cursor.getString(_cursorIndexOfTipoFachada);
            }
            final Double _tmpM2Reportados;
            if (_cursor.isNull(_cursorIndexOfM2Reportados)) {
              _tmpM2Reportados = null;
            } else {
              _tmpM2Reportados = _cursor.getDouble(_cursorIndexOfM2Reportados);
            }
            final Double _tmpM2Final;
            if (_cursor.isNull(_cursorIndexOfM2Final)) {
              _tmpM2Final = null;
            } else {
              _tmpM2Final = _cursor.getDouble(_cursorIndexOfM2Final);
            }
            final Integer _tmpFiguras;
            if (_cursor.isNull(_cursorIndexOfFiguras)) {
              _tmpFiguras = null;
            } else {
              _tmpFiguras = _cursor.getInt(_cursorIndexOfFiguras);
            }
            final String _tmpTarifaTipo;
            if (_cursor.isNull(_cursorIndexOfTarifaTipo)) {
              _tmpTarifaTipo = null;
            } else {
              _tmpTarifaTipo = _cursor.getString(_cursorIndexOfTarifaTipo);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpCambioTexto;
            if (_cursor.isNull(_cursorIndexOfCambioTexto)) {
              _tmpCambioTexto = null;
            } else {
              _tmpCambioTexto = _cursor.getString(_cursorIndexOfCambioTexto);
            }
            final String _tmpTipoSolicitud;
            if (_cursor.isNull(_cursorIndexOfTipoSolicitud)) {
              _tmpTipoSolicitud = null;
            } else {
              _tmpTipoSolicitud = _cursor.getString(_cursorIndexOfTipoSolicitud);
            }
            _result = new Folio(_tmpId,_tmpRutaId,_tmpFolioTruper,_tmpNombreEstablecimiento,_tmpDireccion,_tmpTipoFachada,_tmpM2Reportados,_tmpM2Final,_tmpFiguras,_tmpTarifaTipo,_tmpEstado,_tmpObservaciones,_tmpCambioTexto,_tmpTipoSolicitud);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findByFolioAndRuta(final String folioTruper, final long rutaId,
      final Continuation<? super Folio> $completion) {
    final String _sql = "SELECT * FROM folios WHERE folioTruper = ? AND rutaId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, folioTruper);
    _argIndex = 2;
    _statement.bindLong(_argIndex, rutaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Folio>() {
      @Override
      @Nullable
      public Folio call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRutaId = CursorUtil.getColumnIndexOrThrow(_cursor, "rutaId");
          final int _cursorIndexOfFolioTruper = CursorUtil.getColumnIndexOrThrow(_cursor, "folioTruper");
          final int _cursorIndexOfNombreEstablecimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEstablecimiento");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTipoFachada = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoFachada");
          final int _cursorIndexOfM2Reportados = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Reportados");
          final int _cursorIndexOfM2Final = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Final");
          final int _cursorIndexOfFiguras = CursorUtil.getColumnIndexOrThrow(_cursor, "figuras");
          final int _cursorIndexOfTarifaTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tarifaTipo");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCambioTexto = CursorUtil.getColumnIndexOrThrow(_cursor, "cambioTexto");
          final int _cursorIndexOfTipoSolicitud = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoSolicitud");
          final Folio _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpRutaId;
            _tmpRutaId = _cursor.getLong(_cursorIndexOfRutaId);
            final String _tmpFolioTruper;
            _tmpFolioTruper = _cursor.getString(_cursorIndexOfFolioTruper);
            final String _tmpNombreEstablecimiento;
            if (_cursor.isNull(_cursorIndexOfNombreEstablecimiento)) {
              _tmpNombreEstablecimiento = null;
            } else {
              _tmpNombreEstablecimiento = _cursor.getString(_cursorIndexOfNombreEstablecimiento);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTipoFachada;
            if (_cursor.isNull(_cursorIndexOfTipoFachada)) {
              _tmpTipoFachada = null;
            } else {
              _tmpTipoFachada = _cursor.getString(_cursorIndexOfTipoFachada);
            }
            final Double _tmpM2Reportados;
            if (_cursor.isNull(_cursorIndexOfM2Reportados)) {
              _tmpM2Reportados = null;
            } else {
              _tmpM2Reportados = _cursor.getDouble(_cursorIndexOfM2Reportados);
            }
            final Double _tmpM2Final;
            if (_cursor.isNull(_cursorIndexOfM2Final)) {
              _tmpM2Final = null;
            } else {
              _tmpM2Final = _cursor.getDouble(_cursorIndexOfM2Final);
            }
            final Integer _tmpFiguras;
            if (_cursor.isNull(_cursorIndexOfFiguras)) {
              _tmpFiguras = null;
            } else {
              _tmpFiguras = _cursor.getInt(_cursorIndexOfFiguras);
            }
            final String _tmpTarifaTipo;
            if (_cursor.isNull(_cursorIndexOfTarifaTipo)) {
              _tmpTarifaTipo = null;
            } else {
              _tmpTarifaTipo = _cursor.getString(_cursorIndexOfTarifaTipo);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpCambioTexto;
            if (_cursor.isNull(_cursorIndexOfCambioTexto)) {
              _tmpCambioTexto = null;
            } else {
              _tmpCambioTexto = _cursor.getString(_cursorIndexOfCambioTexto);
            }
            final String _tmpTipoSolicitud;
            if (_cursor.isNull(_cursorIndexOfTipoSolicitud)) {
              _tmpTipoSolicitud = null;
            } else {
              _tmpTipoSolicitud = _cursor.getString(_cursorIndexOfTipoSolicitud);
            }
            _result = new Folio(_tmpId,_tmpRutaId,_tmpFolioTruper,_tmpNombreEstablecimiento,_tmpDireccion,_tmpTipoFachada,_tmpM2Reportados,_tmpM2Final,_tmpFiguras,_tmpTarifaTipo,_tmpEstado,_tmpObservaciones,_tmpCambioTexto,_tmpTipoSolicitud);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Folio> getById(final long folioId) {
    final String _sql = "SELECT * FROM folios WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, folioId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"folios"}, new Callable<Folio>() {
      @Override
      @Nullable
      public Folio call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRutaId = CursorUtil.getColumnIndexOrThrow(_cursor, "rutaId");
          final int _cursorIndexOfFolioTruper = CursorUtil.getColumnIndexOrThrow(_cursor, "folioTruper");
          final int _cursorIndexOfNombreEstablecimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEstablecimiento");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTipoFachada = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoFachada");
          final int _cursorIndexOfM2Reportados = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Reportados");
          final int _cursorIndexOfM2Final = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Final");
          final int _cursorIndexOfFiguras = CursorUtil.getColumnIndexOrThrow(_cursor, "figuras");
          final int _cursorIndexOfTarifaTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tarifaTipo");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCambioTexto = CursorUtil.getColumnIndexOrThrow(_cursor, "cambioTexto");
          final int _cursorIndexOfTipoSolicitud = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoSolicitud");
          final Folio _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpRutaId;
            _tmpRutaId = _cursor.getLong(_cursorIndexOfRutaId);
            final String _tmpFolioTruper;
            _tmpFolioTruper = _cursor.getString(_cursorIndexOfFolioTruper);
            final String _tmpNombreEstablecimiento;
            if (_cursor.isNull(_cursorIndexOfNombreEstablecimiento)) {
              _tmpNombreEstablecimiento = null;
            } else {
              _tmpNombreEstablecimiento = _cursor.getString(_cursorIndexOfNombreEstablecimiento);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTipoFachada;
            if (_cursor.isNull(_cursorIndexOfTipoFachada)) {
              _tmpTipoFachada = null;
            } else {
              _tmpTipoFachada = _cursor.getString(_cursorIndexOfTipoFachada);
            }
            final Double _tmpM2Reportados;
            if (_cursor.isNull(_cursorIndexOfM2Reportados)) {
              _tmpM2Reportados = null;
            } else {
              _tmpM2Reportados = _cursor.getDouble(_cursorIndexOfM2Reportados);
            }
            final Double _tmpM2Final;
            if (_cursor.isNull(_cursorIndexOfM2Final)) {
              _tmpM2Final = null;
            } else {
              _tmpM2Final = _cursor.getDouble(_cursorIndexOfM2Final);
            }
            final Integer _tmpFiguras;
            if (_cursor.isNull(_cursorIndexOfFiguras)) {
              _tmpFiguras = null;
            } else {
              _tmpFiguras = _cursor.getInt(_cursorIndexOfFiguras);
            }
            final String _tmpTarifaTipo;
            if (_cursor.isNull(_cursorIndexOfTarifaTipo)) {
              _tmpTarifaTipo = null;
            } else {
              _tmpTarifaTipo = _cursor.getString(_cursorIndexOfTarifaTipo);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpCambioTexto;
            if (_cursor.isNull(_cursorIndexOfCambioTexto)) {
              _tmpCambioTexto = null;
            } else {
              _tmpCambioTexto = _cursor.getString(_cursorIndexOfCambioTexto);
            }
            final String _tmpTipoSolicitud;
            if (_cursor.isNull(_cursorIndexOfTipoSolicitud)) {
              _tmpTipoSolicitud = null;
            } else {
              _tmpTipoSolicitud = _cursor.getString(_cursorIndexOfTipoSolicitud);
            }
            _result = new Folio(_tmpId,_tmpRutaId,_tmpFolioTruper,_tmpNombreEstablecimiento,_tmpDireccion,_tmpTipoFachada,_tmpM2Reportados,_tmpM2Final,_tmpFiguras,_tmpTarifaTipo,_tmpEstado,_tmpObservaciones,_tmpCambioTexto,_tmpTipoSolicitud);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getByIdSimple(final long folioId, final Continuation<? super Folio> $completion) {
    final String _sql = "SELECT * FROM folios WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, folioId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Folio>() {
      @Override
      @Nullable
      public Folio call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRutaId = CursorUtil.getColumnIndexOrThrow(_cursor, "rutaId");
          final int _cursorIndexOfFolioTruper = CursorUtil.getColumnIndexOrThrow(_cursor, "folioTruper");
          final int _cursorIndexOfNombreEstablecimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEstablecimiento");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTipoFachada = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoFachada");
          final int _cursorIndexOfM2Reportados = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Reportados");
          final int _cursorIndexOfM2Final = CursorUtil.getColumnIndexOrThrow(_cursor, "m2Final");
          final int _cursorIndexOfFiguras = CursorUtil.getColumnIndexOrThrow(_cursor, "figuras");
          final int _cursorIndexOfTarifaTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tarifaTipo");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCambioTexto = CursorUtil.getColumnIndexOrThrow(_cursor, "cambioTexto");
          final int _cursorIndexOfTipoSolicitud = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoSolicitud");
          final Folio _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpRutaId;
            _tmpRutaId = _cursor.getLong(_cursorIndexOfRutaId);
            final String _tmpFolioTruper;
            _tmpFolioTruper = _cursor.getString(_cursorIndexOfFolioTruper);
            final String _tmpNombreEstablecimiento;
            if (_cursor.isNull(_cursorIndexOfNombreEstablecimiento)) {
              _tmpNombreEstablecimiento = null;
            } else {
              _tmpNombreEstablecimiento = _cursor.getString(_cursorIndexOfNombreEstablecimiento);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTipoFachada;
            if (_cursor.isNull(_cursorIndexOfTipoFachada)) {
              _tmpTipoFachada = null;
            } else {
              _tmpTipoFachada = _cursor.getString(_cursorIndexOfTipoFachada);
            }
            final Double _tmpM2Reportados;
            if (_cursor.isNull(_cursorIndexOfM2Reportados)) {
              _tmpM2Reportados = null;
            } else {
              _tmpM2Reportados = _cursor.getDouble(_cursorIndexOfM2Reportados);
            }
            final Double _tmpM2Final;
            if (_cursor.isNull(_cursorIndexOfM2Final)) {
              _tmpM2Final = null;
            } else {
              _tmpM2Final = _cursor.getDouble(_cursorIndexOfM2Final);
            }
            final Integer _tmpFiguras;
            if (_cursor.isNull(_cursorIndexOfFiguras)) {
              _tmpFiguras = null;
            } else {
              _tmpFiguras = _cursor.getInt(_cursorIndexOfFiguras);
            }
            final String _tmpTarifaTipo;
            if (_cursor.isNull(_cursorIndexOfTarifaTipo)) {
              _tmpTarifaTipo = null;
            } else {
              _tmpTarifaTipo = _cursor.getString(_cursorIndexOfTarifaTipo);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpCambioTexto;
            if (_cursor.isNull(_cursorIndexOfCambioTexto)) {
              _tmpCambioTexto = null;
            } else {
              _tmpCambioTexto = _cursor.getString(_cursorIndexOfCambioTexto);
            }
            final String _tmpTipoSolicitud;
            if (_cursor.isNull(_cursorIndexOfTipoSolicitud)) {
              _tmpTipoSolicitud = null;
            } else {
              _tmpTipoSolicitud = _cursor.getString(_cursorIndexOfTipoSolicitud);
            }
            _result = new Folio(_tmpId,_tmpRutaId,_tmpFolioTruper,_tmpNombreEstablecimiento,_tmpDireccion,_tmpTipoFachada,_tmpM2Reportados,_tmpM2Final,_tmpFiguras,_tmpTarifaTipo,_tmpEstado,_tmpObservaciones,_tmpCambioTexto,_tmpTipoSolicitud);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
