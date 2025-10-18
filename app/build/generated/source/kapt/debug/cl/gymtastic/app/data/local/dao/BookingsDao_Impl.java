package cl.gymtastic.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import cl.gymtastic.app.data.local.entity.BookingEntity;
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
public final class BookingsDao_Impl implements BookingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BookingEntity> __insertionAdapterOfBookingEntity;

  private final EntityDeletionOrUpdateAdapter<BookingEntity> __deletionAdapterOfBookingEntity;

  private final EntityDeletionOrUpdateAdapter<BookingEntity> __updateAdapterOfBookingEntity;

  public BookingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBookingEntity = new EntityInsertionAdapter<BookingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `bookings` (`id`,`userId`,`trainerId`,`fechaHora`,`estado`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getTrainerId());
        statement.bindLong(4, entity.getFechaHora());
        if (entity.getEstado() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEstado());
        }
      }
    };
    this.__deletionAdapterOfBookingEntity = new EntityDeletionOrUpdateAdapter<BookingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `bookings` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookingEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfBookingEntity = new EntityDeletionOrUpdateAdapter<BookingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `bookings` SET `id` = ?,`userId` = ?,`trainerId` = ?,`fechaHora` = ?,`estado` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getTrainerId());
        statement.bindLong(4, entity.getFechaHora());
        if (entity.getEstado() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEstado());
        }
        statement.bindLong(6, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final BookingEntity b, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBookingEntity.insertAndReturnId(b);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final BookingEntity b, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBookingEntity.handle(b);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BookingEntity b, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBookingEntity.handle(b);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BookingEntity>> observeByUser(final long uid) {
    final String _sql = "SELECT * FROM bookings WHERE userId = ? ORDER BY fechaHora DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, uid);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTrainerId = CursorUtil.getColumnIndexOrThrow(_cursor, "trainerId");
          final int _cursorIndexOfFechaHora = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaHora");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpTrainerId;
            _tmpTrainerId = _cursor.getLong(_cursorIndexOfTrainerId);
            final long _tmpFechaHora;
            _tmpFechaHora = _cursor.getLong(_cursorIndexOfFechaHora);
            final String _tmpEstado;
            if (_cursor.isNull(_cursorIndexOfEstado)) {
              _tmpEstado = null;
            } else {
              _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            }
            _item = new BookingEntity(_tmpId,_tmpUserId,_tmpTrainerId,_tmpFechaHora,_tmpEstado);
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
