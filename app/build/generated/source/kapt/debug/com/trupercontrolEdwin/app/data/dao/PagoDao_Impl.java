package com.trupercontrolEdwin.app.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.trupercontrolEdwin.app.data.entities.Pago;
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
        return "INSERT OR REPLACE INTO `pagos` (`id`,`facturaId`,`fechaPago`,`monto`,`documentoPagoUri`,`referenciaBanco`,`tipo`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pago entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFacturaId());
        if (entity.getFechaPago() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFechaPago());
        }
        statement.bindDouble(4, entity.getMonto());
        if (entity.getDocumentoPagoUri() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDocumentoPagoUri());
        }
        if (entity.getReferenciaBanco() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getReferenciaBanco());
        }
        if (entity.getTipo() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getTipo());
        }
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
  public Flow<List<Pago>> getByFactura(final long facturaId) {
    final String _sql = "SELECT * FROM pagos WHERE facturaId = ?";
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
          final int _cursorIndexOfFechaPago = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaPago");
          final int _cursorIndexOfMonto = CursorUtil.getColumnIndexOrThrow(_cursor, "monto");
          final int _cursorIndexOfDocumentoPagoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "documentoPagoUri");
          final int _cursorIndexOfReferenciaBanco = CursorUtil.getColumnIndexOrThrow(_cursor, "referenciaBanco");
          final int _cursorIndexOfTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo");
          final List<Pago> _result = new ArrayList<Pago>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pago _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFacturaId;
            _tmpFacturaId = _cursor.getLong(_cursorIndexOfFacturaId);
            final String _tmpFechaPago;
            if (_cursor.isNull(_cursorIndexOfFechaPago)) {
              _tmpFechaPago = null;
            } else {
              _tmpFechaPago = _cursor.getString(_cursorIndexOfFechaPago);
            }
            final double _tmpMonto;
            _tmpMonto = _cursor.getDouble(_cursorIndexOfMonto);
            final String _tmpDocumentoPagoUri;
            if (_cursor.isNull(_cursorIndexOfDocumentoPagoUri)) {
              _tmpDocumentoPagoUri = null;
            } else {
              _tmpDocumentoPagoUri = _cursor.getString(_cursorIndexOfDocumentoPagoUri);
            }
            final String _tmpReferenciaBanco;
            if (_cursor.isNull(_cursorIndexOfReferenciaBanco)) {
              _tmpReferenciaBanco = null;
            } else {
              _tmpReferenciaBanco = _cursor.getString(_cursorIndexOfReferenciaBanco);
            }
            final String _tmpTipo;
            if (_cursor.isNull(_cursorIndexOfTipo)) {
              _tmpTipo = null;
            } else {
              _tmpTipo = _cursor.getString(_cursorIndexOfTipo);
            }
            _item = new Pago(_tmpId,_tmpFacturaId,_tmpFechaPago,_tmpMonto,_tmpDocumentoPagoUri,_tmpReferenciaBanco,_tmpTipo);
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
