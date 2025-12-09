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
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.trupercontrolEdwin.app.data.entities.Factura;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
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
public final class FacturaDao_Impl implements FacturaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Factura> __insertionAdapterOfFactura;

  private final EntityDeletionOrUpdateAdapter<Factura> __deletionAdapterOfFactura;

  private final EntityDeletionOrUpdateAdapter<Factura> __updateAdapterOfFactura;

  public FacturaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFactura = new EntityInsertionAdapter<Factura>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `facturas` (`id`,`folioId`,`folioFactura`,`subtotal`,`iva`,`total`,`estado`,`motivoCancelacion`,`fechaCreacion`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Factura entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFolioId());
        statement.bindString(3, entity.getFolioFactura());
        statement.bindDouble(4, entity.getSubtotal());
        statement.bindDouble(5, entity.getIva());
        statement.bindDouble(6, entity.getTotal());
        statement.bindString(7, entity.getEstado());
        if (entity.getMotivoCancelacion() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMotivoCancelacion());
        }
        statement.bindLong(9, entity.getFechaCreacion());
      }
    };
    this.__deletionAdapterOfFactura = new EntityDeletionOrUpdateAdapter<Factura>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `facturas` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Factura entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfFactura = new EntityDeletionOrUpdateAdapter<Factura>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `facturas` SET `id` = ?,`folioId` = ?,`folioFactura` = ?,`subtotal` = ?,`iva` = ?,`total` = ?,`estado` = ?,`motivoCancelacion` = ?,`fechaCreacion` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Factura entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFolioId());
        statement.bindString(3, entity.getFolioFactura());
        statement.bindDouble(4, entity.getSubtotal());
        statement.bindDouble(5, entity.getIva());
        statement.bindDouble(6, entity.getTotal());
        statement.bindString(7, entity.getEstado());
        if (entity.getMotivoCancelacion() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMotivoCancelacion());
        }
        statement.bindLong(9, entity.getFechaCreacion());
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Factura factura, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFactura.insert(factura);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Factura factura, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFactura.handle(factura);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Factura factura, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFactura.handle(factura);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Factura>> getByFolio(final long folioId) {
    final String _sql = "SELECT * FROM facturas WHERE folioId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, folioId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"facturas"}, new Callable<List<Factura>>() {
      @Override
      @NonNull
      public List<Factura> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFolioId = CursorUtil.getColumnIndexOrThrow(_cursor, "folioId");
          final int _cursorIndexOfFolioFactura = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFactura");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfIva = CursorUtil.getColumnIndexOrThrow(_cursor, "iva");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfMotivoCancelacion = CursorUtil.getColumnIndexOrThrow(_cursor, "motivoCancelacion");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final List<Factura> _result = new ArrayList<Factura>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Factura _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFolioId;
            _tmpFolioId = _cursor.getLong(_cursorIndexOfFolioId);
            final String _tmpFolioFactura;
            _tmpFolioFactura = _cursor.getString(_cursorIndexOfFolioFactura);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpIva;
            _tmpIva = _cursor.getDouble(_cursorIndexOfIva);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpMotivoCancelacion;
            if (_cursor.isNull(_cursorIndexOfMotivoCancelacion)) {
              _tmpMotivoCancelacion = null;
            } else {
              _tmpMotivoCancelacion = _cursor.getString(_cursorIndexOfMotivoCancelacion);
            }
            final long _tmpFechaCreacion;
            _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
            _item = new Factura(_tmpId,_tmpFolioId,_tmpFolioFactura,_tmpSubtotal,_tmpIva,_tmpTotal,_tmpEstado,_tmpMotivoCancelacion,_tmpFechaCreacion);
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
  public Object getAllSimple(final Continuation<? super List<Factura>> $completion) {
    final String _sql = "SELECT * FROM facturas";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Factura>>() {
      @Override
      @NonNull
      public List<Factura> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFolioId = CursorUtil.getColumnIndexOrThrow(_cursor, "folioId");
          final int _cursorIndexOfFolioFactura = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFactura");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfIva = CursorUtil.getColumnIndexOrThrow(_cursor, "iva");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfMotivoCancelacion = CursorUtil.getColumnIndexOrThrow(_cursor, "motivoCancelacion");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final List<Factura> _result = new ArrayList<Factura>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Factura _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFolioId;
            _tmpFolioId = _cursor.getLong(_cursorIndexOfFolioId);
            final String _tmpFolioFactura;
            _tmpFolioFactura = _cursor.getString(_cursorIndexOfFolioFactura);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpIva;
            _tmpIva = _cursor.getDouble(_cursorIndexOfIva);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpMotivoCancelacion;
            if (_cursor.isNull(_cursorIndexOfMotivoCancelacion)) {
              _tmpMotivoCancelacion = null;
            } else {
              _tmpMotivoCancelacion = _cursor.getString(_cursorIndexOfMotivoCancelacion);
            }
            final long _tmpFechaCreacion;
            _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
            _item = new Factura(_tmpId,_tmpFolioId,_tmpFolioFactura,_tmpSubtotal,_tmpIva,_tmpTotal,_tmpEstado,_tmpMotivoCancelacion,_tmpFechaCreacion);
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
  public Object getPendientes(final Continuation<? super List<Factura>> $completion) {
    final String _sql = "SELECT * FROM facturas WHERE estado = 'Pendiente'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Factura>>() {
      @Override
      @NonNull
      public List<Factura> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFolioId = CursorUtil.getColumnIndexOrThrow(_cursor, "folioId");
          final int _cursorIndexOfFolioFactura = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFactura");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfIva = CursorUtil.getColumnIndexOrThrow(_cursor, "iva");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfMotivoCancelacion = CursorUtil.getColumnIndexOrThrow(_cursor, "motivoCancelacion");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final List<Factura> _result = new ArrayList<Factura>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Factura _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFolioId;
            _tmpFolioId = _cursor.getLong(_cursorIndexOfFolioId);
            final String _tmpFolioFactura;
            _tmpFolioFactura = _cursor.getString(_cursorIndexOfFolioFactura);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpIva;
            _tmpIva = _cursor.getDouble(_cursorIndexOfIva);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpMotivoCancelacion;
            if (_cursor.isNull(_cursorIndexOfMotivoCancelacion)) {
              _tmpMotivoCancelacion = null;
            } else {
              _tmpMotivoCancelacion = _cursor.getString(_cursorIndexOfMotivoCancelacion);
            }
            final long _tmpFechaCreacion;
            _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
            _item = new Factura(_tmpId,_tmpFolioId,_tmpFolioFactura,_tmpSubtotal,_tmpIva,_tmpTotal,_tmpEstado,_tmpMotivoCancelacion,_tmpFechaCreacion);
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
  public Object getSumSubtotalInPeriod(final long inicio, final long fin,
      final Continuation<? super Double> $completion) {
    final String _sql = "SELECT SUM(subtotal) FROM facturas WHERE fechaCreacion BETWEEN ? AND ? AND estado != 'Cancelado'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, inicio);
    _argIndex = 2;
    _statement.bindLong(_argIndex, fin);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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
  public Object getSumIvaInPeriod(final long inicio, final long fin,
      final Continuation<? super Double> $completion) {
    final String _sql = "SELECT SUM(iva) FROM facturas WHERE fechaCreacion BETWEEN ? AND ? AND estado != 'Cancelado'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, inicio);
    _argIndex = 2;
    _statement.bindLong(_argIndex, fin);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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
  public Object buscarPorFolioFactura(final String query,
      final Continuation<? super List<Factura>> $completion) {
    final String _sql = "SELECT * FROM facturas WHERE folioFactura LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Factura>>() {
      @Override
      @NonNull
      public List<Factura> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFolioId = CursorUtil.getColumnIndexOrThrow(_cursor, "folioId");
          final int _cursorIndexOfFolioFactura = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFactura");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfIva = CursorUtil.getColumnIndexOrThrow(_cursor, "iva");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfMotivoCancelacion = CursorUtil.getColumnIndexOrThrow(_cursor, "motivoCancelacion");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final List<Factura> _result = new ArrayList<Factura>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Factura _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFolioId;
            _tmpFolioId = _cursor.getLong(_cursorIndexOfFolioId);
            final String _tmpFolioFactura;
            _tmpFolioFactura = _cursor.getString(_cursorIndexOfFolioFactura);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpIva;
            _tmpIva = _cursor.getDouble(_cursorIndexOfIva);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpMotivoCancelacion;
            if (_cursor.isNull(_cursorIndexOfMotivoCancelacion)) {
              _tmpMotivoCancelacion = null;
            } else {
              _tmpMotivoCancelacion = _cursor.getString(_cursorIndexOfMotivoCancelacion);
            }
            final long _tmpFechaCreacion;
            _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
            _item = new Factura(_tmpId,_tmpFolioId,_tmpFolioFactura,_tmpSubtotal,_tmpIva,_tmpTotal,_tmpEstado,_tmpMotivoCancelacion,_tmpFechaCreacion);
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
  public Flow<Factura> getById(final long facturaId) {
    final String _sql = "SELECT * FROM facturas WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, facturaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"facturas"}, new Callable<Factura>() {
      @Override
      @Nullable
      public Factura call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFolioId = CursorUtil.getColumnIndexOrThrow(_cursor, "folioId");
          final int _cursorIndexOfFolioFactura = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFactura");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfIva = CursorUtil.getColumnIndexOrThrow(_cursor, "iva");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfMotivoCancelacion = CursorUtil.getColumnIndexOrThrow(_cursor, "motivoCancelacion");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final Factura _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFolioId;
            _tmpFolioId = _cursor.getLong(_cursorIndexOfFolioId);
            final String _tmpFolioFactura;
            _tmpFolioFactura = _cursor.getString(_cursorIndexOfFolioFactura);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpIva;
            _tmpIva = _cursor.getDouble(_cursorIndexOfIva);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpMotivoCancelacion;
            if (_cursor.isNull(_cursorIndexOfMotivoCancelacion)) {
              _tmpMotivoCancelacion = null;
            } else {
              _tmpMotivoCancelacion = _cursor.getString(_cursorIndexOfMotivoCancelacion);
            }
            final long _tmpFechaCreacion;
            _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
            _result = new Factura(_tmpId,_tmpFolioId,_tmpFolioFactura,_tmpSubtotal,_tmpIva,_tmpTotal,_tmpEstado,_tmpMotivoCancelacion,_tmpFechaCreacion);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
