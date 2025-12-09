package com.trupercontrolEdwin.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.trupercontrolEdwin.app.data.entities.Pago;
import com.trupercontrolEdwin.app.data.model.PagoConFactura;
import java.lang.Class;
import java.lang.Exception;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PagoDao_Impl implements PagoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Pago> __insertionAdapterOfPago;

  public PagoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPago = new EntityInsertionAdapter<Pago>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `pagos` (`id`,`facturaId`,`monto`,`fecha`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pago entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFacturaId());
        statement.bindDouble(3, entity.getMonto());
        statement.bindLong(4, entity.getFecha());
      }
    };
  }

  @Override
  public Object insert(final Pago pago, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPago.insertAndReturnId(pago);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Pago>> getPagosByFactura(final long facturaId) {
    final String _sql = "SELECT * FROM pagos WHERE facturaId = ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, facturaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pagos"}, new Callable<List<Pago>>() {
      @Override
      @NonNull
      public List<Pago> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFacturaId = CursorUtil.getColumnIndexOrThrow(_cursor, "facturaId");
          final int _cursorIndexOfMonto = CursorUtil.getColumnIndexOrThrow(_cursor, "monto");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final List<Pago> _result = new ArrayList<Pago>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pago _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFacturaId;
            _tmpFacturaId = _cursor.getLong(_cursorIndexOfFacturaId);
            final double _tmpMonto;
            _tmpMonto = _cursor.getDouble(_cursorIndexOfMonto);
            final long _tmpFecha;
            _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
            _item = new Pago(_tmpId,_tmpFacturaId,_tmpMonto,_tmpFecha);
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
  public Object getAllSimple(final Continuation<? super List<Pago>> $completion) {
    final String _sql = "SELECT * FROM pagos";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Pago>>() {
      @Override
      @NonNull
      public List<Pago> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFacturaId = CursorUtil.getColumnIndexOrThrow(_cursor, "facturaId");
          final int _cursorIndexOfMonto = CursorUtil.getColumnIndexOrThrow(_cursor, "monto");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final List<Pago> _result = new ArrayList<Pago>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pago _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFacturaId;
            _tmpFacturaId = _cursor.getLong(_cursorIndexOfFacturaId);
            final double _tmpMonto;
            _tmpMonto = _cursor.getDouble(_cursorIndexOfMonto);
            final long _tmpFecha;
            _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
            _item = new Pago(_tmpId,_tmpFacturaId,_tmpMonto,_tmpFecha);
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
  public Flow<List<PagoConFactura>> getPagosConFactura() {
    final String _sql = "\n"
            + "        SELECT f.folioFactura, p.monto, p.fecha \n"
            + "        FROM pagos p \n"
            + "        INNER JOIN facturas f ON p.facturaId = f.id \n"
            + "        ORDER BY p.fecha DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pagos",
        "facturas"}, new Callable<List<PagoConFactura>>() {
      @Override
      @NonNull
      public List<PagoConFactura> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFolioFactura = 0;
          final int _cursorIndexOfMontoPago = 1;
          final int _cursorIndexOfFechaPago = 2;
          final List<PagoConFactura> _result = new ArrayList<PagoConFactura>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PagoConFactura _item;
            final String _tmpFolioFactura;
            _tmpFolioFactura = _cursor.getString(_cursorIndexOfFolioFactura);
            final double _tmpMontoPago;
            _tmpMontoPago = _cursor.getDouble(_cursorIndexOfMontoPago);
            final long _tmpFechaPago;
            _tmpFechaPago = _cursor.getLong(_cursorIndexOfFechaPago);
            _item = new PagoConFactura(_tmpFolioFactura,_tmpMontoPago,_tmpFechaPago);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
