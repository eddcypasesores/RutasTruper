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
import com.trupercontrolEdwin.app.data.entities.Ruta;
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
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RutaDao_Impl implements RutaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Ruta> __insertionAdapterOfRuta;

  private final EntityDeletionOrUpdateAdapter<Ruta> __deletionAdapterOfRuta;

  public RutaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRuta = new EntityInsertionAdapter<Ruta>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rutas` (`id`,`nombre`,`fecha`,`fotoListadoUri`,`foliosEsperados`,`notas`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Ruta entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombre());
        }
        if (entity.getFecha() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFecha());
        }
        if (entity.getFotoListadoUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getFotoListadoUri());
        }
        if (entity.getFoliosEsperados() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFoliosEsperados());
        }
        if (entity.getNotas() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNotas());
        }
      }
    };
    this.__deletionAdapterOfRuta = new EntityDeletionOrUpdateAdapter<Ruta>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rutas` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Ruta entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Ruta ruta, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRuta.insertAndReturnId(ruta);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Ruta ruta, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRuta.handle(ruta);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Ruta>> getAll() {
    final String _sql = "SELECT * FROM rutas ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rutas"}, new Callable<List<Ruta>>() {
      @Override
      @NonNull
      public List<Ruta> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfFotoListadoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoListadoUri");
          final int _cursorIndexOfFoliosEsperados = CursorUtil.getColumnIndexOrThrow(_cursor, "foliosEsperados");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final List<Ruta> _result = new ArrayList<Ruta>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Ruta _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpFecha;
            if (_cursor.isNull(_cursorIndexOfFecha)) {
              _tmpFecha = null;
            } else {
              _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            }
            final String _tmpFotoListadoUri;
            if (_cursor.isNull(_cursorIndexOfFotoListadoUri)) {
              _tmpFotoListadoUri = null;
            } else {
              _tmpFotoListadoUri = _cursor.getString(_cursorIndexOfFotoListadoUri);
            }
            final String _tmpFoliosEsperados;
            if (_cursor.isNull(_cursorIndexOfFoliosEsperados)) {
              _tmpFoliosEsperados = null;
            } else {
              _tmpFoliosEsperados = _cursor.getString(_cursorIndexOfFoliosEsperados);
            }
            final String _tmpNotas;
            if (_cursor.isNull(_cursorIndexOfNotas)) {
              _tmpNotas = null;
            } else {
              _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
            }
            _item = new Ruta(_tmpId,_tmpNombre,_tmpFecha,_tmpFotoListadoUri,_tmpFoliosEsperados,_tmpNotas);
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
  public Object getById(final long rutaId, final Continuation<? super Ruta> $completion) {
    final String _sql = "SELECT * FROM rutas WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rutaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Ruta>() {
      @Override
      @Nullable
      public Ruta call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfFotoListadoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoListadoUri");
          final int _cursorIndexOfFoliosEsperados = CursorUtil.getColumnIndexOrThrow(_cursor, "foliosEsperados");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final Ruta _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpFecha;
            if (_cursor.isNull(_cursorIndexOfFecha)) {
              _tmpFecha = null;
            } else {
              _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            }
            final String _tmpFotoListadoUri;
            if (_cursor.isNull(_cursorIndexOfFotoListadoUri)) {
              _tmpFotoListadoUri = null;
            } else {
              _tmpFotoListadoUri = _cursor.getString(_cursorIndexOfFotoListadoUri);
            }
            final String _tmpFoliosEsperados;
            if (_cursor.isNull(_cursorIndexOfFoliosEsperados)) {
              _tmpFoliosEsperados = null;
            } else {
              _tmpFoliosEsperados = _cursor.getString(_cursorIndexOfFoliosEsperados);
            }
            final String _tmpNotas;
            if (_cursor.isNull(_cursorIndexOfNotas)) {
              _tmpNotas = null;
            } else {
              _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
            }
            _result = new Ruta(_tmpId,_tmpNombre,_tmpFecha,_tmpFotoListadoUri,_tmpFoliosEsperados,_tmpNotas);
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
