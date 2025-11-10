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
import com.trupercontrolEdwin.app.data.entities.Factura;
import java.lang.Class;
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
public final class FacturaDao_Impl implements FacturaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Factura> __insertionAdapterOfFactura;

  private final EntityDeletionOrUpdateAdapter<Factura> __updateAdapterOfFactura;

  private final SharedSQLiteStatement __preparedStmtOfUpdateEstatus;

  public FacturaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFactura = new EntityInsertionAdapter<Factura>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `facturas` (`id`,`folioId`,`folioFactura`,`folioFacturaNum`,`fechaEmision`,`subtotal`,`iva`,`total`,`estatus`,`motivoCancelacion`,`folioReemplaza`,`pdfUri`,`xmlUri`,`observaciones`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Factura entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFolioId());
        statement.bindString(3, entity.getFolioFactura());
        if (entity.getFolioFacturaNum() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getFolioFacturaNum());
        }
        if (entity.getFechaEmision() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFechaEmision());
        }
        statement.bindDouble(6, entity.getSubtotal());
        statement.bindDouble(7, entity.getIva());
        statement.bindDouble(8, entity.getTotal());
        statement.bindString(9, entity.getEstatus());
        if (entity.getMotivoCancelacion() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getMotivoCancelacion());
        }
        if (entity.getFolioReemplaza() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getFolioReemplaza());
        }
        if (entity.getPdfUri() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPdfUri());
        }
        if (entity.getXmlUri() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getXmlUri());
        }
        if (entity.getObservaciones() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getObservaciones());
        }
      }
    };
    this.__updateAdapterOfFactura = new EntityDeletionOrUpdateAdapter<Factura>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `facturas` SET `id` = ?,`folioId` = ?,`folioFactura` = ?,`folioFacturaNum` = ?,`fechaEmision` = ?,`subtotal` = ?,`iva` = ?,`total` = ?,`estatus` = ?,`motivoCancelacion` = ?,`folioReemplaza` = ?,`pdfUri` = ?,`xmlUri` = ?,`observaciones` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Factura entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFolioId());
        statement.bindString(3, entity.getFolioFactura());
        if (entity.getFolioFacturaNum() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getFolioFacturaNum());
        }
        if (entity.getFechaEmision() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFechaEmision());
        }
        statement.bindDouble(6, entity.getSubtotal());
        statement.bindDouble(7, entity.getIva());
        statement.bindDouble(8, entity.getTotal());
        statement.bindString(9, entity.getEstatus());
        if (entity.getMotivoCancelacion() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getMotivoCancelacion());
        }
        if (entity.getFolioReemplaza() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getFolioReemplaza());
        }
        if (entity.getPdfUri() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPdfUri());
        }
        if (entity.getXmlUri() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getXmlUri());
        }
        if (entity.getObservaciones() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getObservaciones());
        }
        statement.bindLong(15, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateEstatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE facturas SET estatus = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Factura factura, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFactura.insertAndReturnId(factura);
          __db.setTransactionSuccessful();
          return _result;
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
  public Object updateEstatus(final long facturaId, final String estatus,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateEstatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, estatus);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, facturaId);
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
          __preparedStmtOfUpdateEstatus.release(_stmt);
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
          final int _cursorIndexOfFolioFacturaNum = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFacturaNum");
          final int _cursorIndexOfFechaEmision = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaEmision");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfIva = CursorUtil.getColumnIndexOrThrow(_cursor, "iva");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstatus = CursorUtil.getColumnIndexOrThrow(_cursor, "estatus");
          final int _cursorIndexOfMotivoCancelacion = CursorUtil.getColumnIndexOrThrow(_cursor, "motivoCancelacion");
          final int _cursorIndexOfFolioReemplaza = CursorUtil.getColumnIndexOrThrow(_cursor, "folioReemplaza");
          final int _cursorIndexOfPdfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "pdfUri");
          final int _cursorIndexOfXmlUri = CursorUtil.getColumnIndexOrThrow(_cursor, "xmlUri");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final List<Factura> _result = new ArrayList<Factura>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Factura _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFolioId;
            _tmpFolioId = _cursor.getLong(_cursorIndexOfFolioId);
            final String _tmpFolioFactura;
            _tmpFolioFactura = _cursor.getString(_cursorIndexOfFolioFactura);
            final Integer _tmpFolioFacturaNum;
            if (_cursor.isNull(_cursorIndexOfFolioFacturaNum)) {
              _tmpFolioFacturaNum = null;
            } else {
              _tmpFolioFacturaNum = _cursor.getInt(_cursorIndexOfFolioFacturaNum);
            }
            final String _tmpFechaEmision;
            if (_cursor.isNull(_cursorIndexOfFechaEmision)) {
              _tmpFechaEmision = null;
            } else {
              _tmpFechaEmision = _cursor.getString(_cursorIndexOfFechaEmision);
            }
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpIva;
            _tmpIva = _cursor.getDouble(_cursorIndexOfIva);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstatus;
            _tmpEstatus = _cursor.getString(_cursorIndexOfEstatus);
            final String _tmpMotivoCancelacion;
            if (_cursor.isNull(_cursorIndexOfMotivoCancelacion)) {
              _tmpMotivoCancelacion = null;
            } else {
              _tmpMotivoCancelacion = _cursor.getString(_cursorIndexOfMotivoCancelacion);
            }
            final String _tmpFolioReemplaza;
            if (_cursor.isNull(_cursorIndexOfFolioReemplaza)) {
              _tmpFolioReemplaza = null;
            } else {
              _tmpFolioReemplaza = _cursor.getString(_cursorIndexOfFolioReemplaza);
            }
            final String _tmpPdfUri;
            if (_cursor.isNull(_cursorIndexOfPdfUri)) {
              _tmpPdfUri = null;
            } else {
              _tmpPdfUri = _cursor.getString(_cursorIndexOfPdfUri);
            }
            final String _tmpXmlUri;
            if (_cursor.isNull(_cursorIndexOfXmlUri)) {
              _tmpXmlUri = null;
            } else {
              _tmpXmlUri = _cursor.getString(_cursorIndexOfXmlUri);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            _item = new Factura(_tmpId,_tmpFolioId,_tmpFolioFactura,_tmpFolioFacturaNum,_tmpFechaEmision,_tmpSubtotal,_tmpIva,_tmpTotal,_tmpEstatus,_tmpMotivoCancelacion,_tmpFolioReemplaza,_tmpPdfUri,_tmpXmlUri,_tmpObservaciones);
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
          final int _cursorIndexOfFolioFacturaNum = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFacturaNum");
          final int _cursorIndexOfFechaEmision = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaEmision");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfIva = CursorUtil.getColumnIndexOrThrow(_cursor, "iva");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstatus = CursorUtil.getColumnIndexOrThrow(_cursor, "estatus");
          final int _cursorIndexOfMotivoCancelacion = CursorUtil.getColumnIndexOrThrow(_cursor, "motivoCancelacion");
          final int _cursorIndexOfFolioReemplaza = CursorUtil.getColumnIndexOrThrow(_cursor, "folioReemplaza");
          final int _cursorIndexOfPdfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "pdfUri");
          final int _cursorIndexOfXmlUri = CursorUtil.getColumnIndexOrThrow(_cursor, "xmlUri");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final List<Factura> _result = new ArrayList<Factura>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Factura _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFolioId;
            _tmpFolioId = _cursor.getLong(_cursorIndexOfFolioId);
            final String _tmpFolioFactura;
            _tmpFolioFactura = _cursor.getString(_cursorIndexOfFolioFactura);
            final Integer _tmpFolioFacturaNum;
            if (_cursor.isNull(_cursorIndexOfFolioFacturaNum)) {
              _tmpFolioFacturaNum = null;
            } else {
              _tmpFolioFacturaNum = _cursor.getInt(_cursorIndexOfFolioFacturaNum);
            }
            final String _tmpFechaEmision;
            if (_cursor.isNull(_cursorIndexOfFechaEmision)) {
              _tmpFechaEmision = null;
            } else {
              _tmpFechaEmision = _cursor.getString(_cursorIndexOfFechaEmision);
            }
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpIva;
            _tmpIva = _cursor.getDouble(_cursorIndexOfIva);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstatus;
            _tmpEstatus = _cursor.getString(_cursorIndexOfEstatus);
            final String _tmpMotivoCancelacion;
            if (_cursor.isNull(_cursorIndexOfMotivoCancelacion)) {
              _tmpMotivoCancelacion = null;
            } else {
              _tmpMotivoCancelacion = _cursor.getString(_cursorIndexOfMotivoCancelacion);
            }
            final String _tmpFolioReemplaza;
            if (_cursor.isNull(_cursorIndexOfFolioReemplaza)) {
              _tmpFolioReemplaza = null;
            } else {
              _tmpFolioReemplaza = _cursor.getString(_cursorIndexOfFolioReemplaza);
            }
            final String _tmpPdfUri;
            if (_cursor.isNull(_cursorIndexOfPdfUri)) {
              _tmpPdfUri = null;
            } else {
              _tmpPdfUri = _cursor.getString(_cursorIndexOfPdfUri);
            }
            final String _tmpXmlUri;
            if (_cursor.isNull(_cursorIndexOfXmlUri)) {
              _tmpXmlUri = null;
            } else {
              _tmpXmlUri = _cursor.getString(_cursorIndexOfXmlUri);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            _item = new Factura(_tmpId,_tmpFolioId,_tmpFolioFactura,_tmpFolioFacturaNum,_tmpFechaEmision,_tmpSubtotal,_tmpIva,_tmpTotal,_tmpEstatus,_tmpMotivoCancelacion,_tmpFolioReemplaza,_tmpPdfUri,_tmpXmlUri,_tmpObservaciones);
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
  public Flow<List<Factura>> getPendientes() {
    final String _sql = "SELECT * FROM facturas WHERE estatus = 'Pendiente'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"facturas"}, new Callable<List<Factura>>() {
      @Override
      @NonNull
      public List<Factura> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFolioId = CursorUtil.getColumnIndexOrThrow(_cursor, "folioId");
          final int _cursorIndexOfFolioFactura = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFactura");
          final int _cursorIndexOfFolioFacturaNum = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFacturaNum");
          final int _cursorIndexOfFechaEmision = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaEmision");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfIva = CursorUtil.getColumnIndexOrThrow(_cursor, "iva");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstatus = CursorUtil.getColumnIndexOrThrow(_cursor, "estatus");
          final int _cursorIndexOfMotivoCancelacion = CursorUtil.getColumnIndexOrThrow(_cursor, "motivoCancelacion");
          final int _cursorIndexOfFolioReemplaza = CursorUtil.getColumnIndexOrThrow(_cursor, "folioReemplaza");
          final int _cursorIndexOfPdfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "pdfUri");
          final int _cursorIndexOfXmlUri = CursorUtil.getColumnIndexOrThrow(_cursor, "xmlUri");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final List<Factura> _result = new ArrayList<Factura>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Factura _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFolioId;
            _tmpFolioId = _cursor.getLong(_cursorIndexOfFolioId);
            final String _tmpFolioFactura;
            _tmpFolioFactura = _cursor.getString(_cursorIndexOfFolioFactura);
            final Integer _tmpFolioFacturaNum;
            if (_cursor.isNull(_cursorIndexOfFolioFacturaNum)) {
              _tmpFolioFacturaNum = null;
            } else {
              _tmpFolioFacturaNum = _cursor.getInt(_cursorIndexOfFolioFacturaNum);
            }
            final String _tmpFechaEmision;
            if (_cursor.isNull(_cursorIndexOfFechaEmision)) {
              _tmpFechaEmision = null;
            } else {
              _tmpFechaEmision = _cursor.getString(_cursorIndexOfFechaEmision);
            }
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpIva;
            _tmpIva = _cursor.getDouble(_cursorIndexOfIva);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstatus;
            _tmpEstatus = _cursor.getString(_cursorIndexOfEstatus);
            final String _tmpMotivoCancelacion;
            if (_cursor.isNull(_cursorIndexOfMotivoCancelacion)) {
              _tmpMotivoCancelacion = null;
            } else {
              _tmpMotivoCancelacion = _cursor.getString(_cursorIndexOfMotivoCancelacion);
            }
            final String _tmpFolioReemplaza;
            if (_cursor.isNull(_cursorIndexOfFolioReemplaza)) {
              _tmpFolioReemplaza = null;
            } else {
              _tmpFolioReemplaza = _cursor.getString(_cursorIndexOfFolioReemplaza);
            }
            final String _tmpPdfUri;
            if (_cursor.isNull(_cursorIndexOfPdfUri)) {
              _tmpPdfUri = null;
            } else {
              _tmpPdfUri = _cursor.getString(_cursorIndexOfPdfUri);
            }
            final String _tmpXmlUri;
            if (_cursor.isNull(_cursorIndexOfXmlUri)) {
              _tmpXmlUri = null;
            } else {
              _tmpXmlUri = _cursor.getString(_cursorIndexOfXmlUri);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            _item = new Factura(_tmpId,_tmpFolioId,_tmpFolioFactura,_tmpFolioFacturaNum,_tmpFechaEmision,_tmpSubtotal,_tmpIva,_tmpTotal,_tmpEstatus,_tmpMotivoCancelacion,_tmpFolioReemplaza,_tmpPdfUri,_tmpXmlUri,_tmpObservaciones);
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
  public Object getByFolioFactura(final String folioFactura,
      final Continuation<? super Factura> $completion) {
    final String _sql = "SELECT * FROM facturas WHERE folioFactura = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, folioFactura);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Factura>() {
      @Override
      @Nullable
      public Factura call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFolioId = CursorUtil.getColumnIndexOrThrow(_cursor, "folioId");
          final int _cursorIndexOfFolioFactura = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFactura");
          final int _cursorIndexOfFolioFacturaNum = CursorUtil.getColumnIndexOrThrow(_cursor, "folioFacturaNum");
          final int _cursorIndexOfFechaEmision = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaEmision");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfIva = CursorUtil.getColumnIndexOrThrow(_cursor, "iva");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstatus = CursorUtil.getColumnIndexOrThrow(_cursor, "estatus");
          final int _cursorIndexOfMotivoCancelacion = CursorUtil.getColumnIndexOrThrow(_cursor, "motivoCancelacion");
          final int _cursorIndexOfFolioReemplaza = CursorUtil.getColumnIndexOrThrow(_cursor, "folioReemplaza");
          final int _cursorIndexOfPdfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "pdfUri");
          final int _cursorIndexOfXmlUri = CursorUtil.getColumnIndexOrThrow(_cursor, "xmlUri");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final Factura _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFolioId;
            _tmpFolioId = _cursor.getLong(_cursorIndexOfFolioId);
            final String _tmpFolioFactura;
            _tmpFolioFactura = _cursor.getString(_cursorIndexOfFolioFactura);
            final Integer _tmpFolioFacturaNum;
            if (_cursor.isNull(_cursorIndexOfFolioFacturaNum)) {
              _tmpFolioFacturaNum = null;
            } else {
              _tmpFolioFacturaNum = _cursor.getInt(_cursorIndexOfFolioFacturaNum);
            }
            final String _tmpFechaEmision;
            if (_cursor.isNull(_cursorIndexOfFechaEmision)) {
              _tmpFechaEmision = null;
            } else {
              _tmpFechaEmision = _cursor.getString(_cursorIndexOfFechaEmision);
            }
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpIva;
            _tmpIva = _cursor.getDouble(_cursorIndexOfIva);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstatus;
            _tmpEstatus = _cursor.getString(_cursorIndexOfEstatus);
            final String _tmpMotivoCancelacion;
            if (_cursor.isNull(_cursorIndexOfMotivoCancelacion)) {
              _tmpMotivoCancelacion = null;
            } else {
              _tmpMotivoCancelacion = _cursor.getString(_cursorIndexOfMotivoCancelacion);
            }
            final String _tmpFolioReemplaza;
            if (_cursor.isNull(_cursorIndexOfFolioReemplaza)) {
              _tmpFolioReemplaza = null;
            } else {
              _tmpFolioReemplaza = _cursor.getString(_cursorIndexOfFolioReemplaza);
            }
            final String _tmpPdfUri;
            if (_cursor.isNull(_cursorIndexOfPdfUri)) {
              _tmpPdfUri = null;
            } else {
              _tmpPdfUri = _cursor.getString(_cursorIndexOfPdfUri);
            }
            final String _tmpXmlUri;
            if (_cursor.isNull(_cursorIndexOfXmlUri)) {
              _tmpXmlUri = null;
            } else {
              _tmpXmlUri = _cursor.getString(_cursorIndexOfXmlUri);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            _result = new Factura(_tmpId,_tmpFolioId,_tmpFolioFactura,_tmpFolioFacturaNum,_tmpFechaEmision,_tmpSubtotal,_tmpIva,_tmpTotal,_tmpEstatus,_tmpMotivoCancelacion,_tmpFolioReemplaza,_tmpPdfUri,_tmpXmlUri,_tmpObservaciones);
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
