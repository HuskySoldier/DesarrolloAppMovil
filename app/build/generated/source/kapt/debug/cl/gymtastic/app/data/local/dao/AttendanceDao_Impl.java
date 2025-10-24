package cl.gymtastic.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import cl.gymtastic.app.data.local.entity.AttendanceEntity;
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
public final class AttendanceDao_Impl implements AttendanceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AttendanceEntity> __insertionAdapterOfAttendanceEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCheckOutById;

  public AttendanceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAttendanceEntity = new EntityInsertionAdapter<AttendanceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `attendance` (`id`,`userEmail`,`timestamp`,`checkOutTimestamp`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttendanceEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUserEmail() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUserEmail());
        }
        statement.bindLong(3, entity.getTimestamp());
        if (entity.getCheckOutTimestamp() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCheckOutTimestamp());
        }
      }
    };
    this.__preparedStmtOfUpdateCheckOutById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE attendance SET checkOutTimestamp = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final AttendanceEntity reg, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAttendanceEntity.insertAndReturnId(reg);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCheckOutById(final long attendanceId, final long checkOut,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCheckOutById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, checkOut);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, attendanceId);
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
          __preparedStmtOfUpdateCheckOutById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AttendanceEntity>> observeByUser(final String userEmail) {
    final String _sql = "SELECT * FROM attendance WHERE userEmail = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userEmail == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userEmail);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance"}, new Callable<List<AttendanceEntity>>() {
      @Override
      @NonNull
      public List<AttendanceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "userEmail");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfCheckOutTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutTimestamp");
          final List<AttendanceEntity> _result = new ArrayList<AttendanceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUserEmail;
            if (_cursor.isNull(_cursorIndexOfUserEmail)) {
              _tmpUserEmail = null;
            } else {
              _tmpUserEmail = _cursor.getString(_cursorIndexOfUserEmail);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Long _tmpCheckOutTimestamp;
            if (_cursor.isNull(_cursorIndexOfCheckOutTimestamp)) {
              _tmpCheckOutTimestamp = null;
            } else {
              _tmpCheckOutTimestamp = _cursor.getLong(_cursorIndexOfCheckOutTimestamp);
            }
            _item = new AttendanceEntity(_tmpId,_tmpUserEmail,_tmpTimestamp,_tmpCheckOutTimestamp);
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
  public Object findLastOpenAttendanceId(final String userEmail,
      final Continuation<? super Long> $completion) {
    final String _sql = "SELECT id FROM attendance WHERE userEmail = ? AND checkOutTimestamp IS NULL ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userEmail == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userEmail);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getLong(0);
            }
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
