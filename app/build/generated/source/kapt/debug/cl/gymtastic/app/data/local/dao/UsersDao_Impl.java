package cl.gymtastic.app.data.local.dao;

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
import cl.gymtastic.app.data.local.entity.UserEntity;
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
public final class UsersDao_Impl implements UsersDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserEntity> __insertionAdapterOfUserEntity;

  private final EntityDeletionOrUpdateAdapter<UserEntity> __deletionAdapterOfUserEntity;

  private final EntityDeletionOrUpdateAdapter<UserEntity> __updateAdapterOfUserEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSubscription;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePasswordHash;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByEmail;

  public UsersDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserEntity = new EntityInsertionAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `users` (`email`,`passHash`,`nombre`,`rol`,`planEndMillis`,`sedeId`,`sedeName`,`sedeLat`,`sedeLng`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        if (entity.getEmail() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getEmail());
        }
        if (entity.getPassHash() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPassHash());
        }
        if (entity.getNombre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNombre());
        }
        if (entity.getRol() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getRol());
        }
        if (entity.getPlanEndMillis() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getPlanEndMillis());
        }
        if (entity.getSedeId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getSedeId());
        }
        if (entity.getSedeName() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getSedeName());
        }
        if (entity.getSedeLat() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getSedeLat());
        }
        if (entity.getSedeLng() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getSedeLng());
        }
      }
    };
    this.__deletionAdapterOfUserEntity = new EntityDeletionOrUpdateAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `users` WHERE `email` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        if (entity.getEmail() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getEmail());
        }
      }
    };
    this.__updateAdapterOfUserEntity = new EntityDeletionOrUpdateAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `users` SET `email` = ?,`passHash` = ?,`nombre` = ?,`rol` = ?,`planEndMillis` = ?,`sedeId` = ?,`sedeName` = ?,`sedeLat` = ?,`sedeLng` = ? WHERE `email` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        if (entity.getEmail() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getEmail());
        }
        if (entity.getPassHash() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPassHash());
        }
        if (entity.getNombre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNombre());
        }
        if (entity.getRol() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getRol());
        }
        if (entity.getPlanEndMillis() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getPlanEndMillis());
        }
        if (entity.getSedeId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getSedeId());
        }
        if (entity.getSedeName() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getSedeName());
        }
        if (entity.getSedeLat() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getSedeLat());
        }
        if (entity.getSedeLng() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getSedeLng());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getEmail());
        }
      }
    };
    this.__preparedStmtOfUpdateSubscription = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET planEndMillis=?, sedeId=?, sedeName=?, sedeLat=?, sedeLng=? WHERE email = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePasswordHash = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET passHash = ? WHERE email = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByEmail = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM users WHERE email = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final UserEntity user, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfUserEntity.insertAndReturnId(user);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final UserEntity user, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfUserEntity.handle(user);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final UserEntity user, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __updateAdapterOfUserEntity.handle(user);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSubscription(final String email, final Long planEndMillis,
      final Integer sedeId, final String sedeName, final Double sedeLat, final Double sedeLng,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSubscription.acquire();
        int _argIndex = 1;
        if (planEndMillis == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, planEndMillis);
        }
        _argIndex = 2;
        if (sedeId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, sedeId);
        }
        _argIndex = 3;
        if (sedeName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, sedeName);
        }
        _argIndex = 4;
        if (sedeLat == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindDouble(_argIndex, sedeLat);
        }
        _argIndex = 5;
        if (sedeLng == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindDouble(_argIndex, sedeLng);
        }
        _argIndex = 6;
        if (email == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, email);
        }
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
          __preparedStmtOfUpdateSubscription.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePasswordHash(final String email, final String newPassHash,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePasswordHash.acquire();
        int _argIndex = 1;
        if (newPassHash == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, newPassHash);
        }
        _argIndex = 2;
        if (email == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, email);
        }
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdatePasswordHash.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByEmail(final String email, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByEmail.acquire();
        int _argIndex = 1;
        if (email == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, email);
        }
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByEmail.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM users";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
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
  public Object findByEmail(final String email,
      final Continuation<? super UserEntity> $completion) {
    final String _sql = "SELECT * FROM users WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passHash");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfRol = CursorUtil.getColumnIndexOrThrow(_cursor, "rol");
          final int _cursorIndexOfPlanEndMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planEndMillis");
          final int _cursorIndexOfSedeId = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeId");
          final int _cursorIndexOfSedeName = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeName");
          final int _cursorIndexOfSedeLat = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeLat");
          final int _cursorIndexOfSedeLng = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeLng");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassHash;
            if (_cursor.isNull(_cursorIndexOfPassHash)) {
              _tmpPassHash = null;
            } else {
              _tmpPassHash = _cursor.getString(_cursorIndexOfPassHash);
            }
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpRol;
            if (_cursor.isNull(_cursorIndexOfRol)) {
              _tmpRol = null;
            } else {
              _tmpRol = _cursor.getString(_cursorIndexOfRol);
            }
            final Long _tmpPlanEndMillis;
            if (_cursor.isNull(_cursorIndexOfPlanEndMillis)) {
              _tmpPlanEndMillis = null;
            } else {
              _tmpPlanEndMillis = _cursor.getLong(_cursorIndexOfPlanEndMillis);
            }
            final Integer _tmpSedeId;
            if (_cursor.isNull(_cursorIndexOfSedeId)) {
              _tmpSedeId = null;
            } else {
              _tmpSedeId = _cursor.getInt(_cursorIndexOfSedeId);
            }
            final String _tmpSedeName;
            if (_cursor.isNull(_cursorIndexOfSedeName)) {
              _tmpSedeName = null;
            } else {
              _tmpSedeName = _cursor.getString(_cursorIndexOfSedeName);
            }
            final Double _tmpSedeLat;
            if (_cursor.isNull(_cursorIndexOfSedeLat)) {
              _tmpSedeLat = null;
            } else {
              _tmpSedeLat = _cursor.getDouble(_cursorIndexOfSedeLat);
            }
            final Double _tmpSedeLng;
            if (_cursor.isNull(_cursorIndexOfSedeLng)) {
              _tmpSedeLng = null;
            } else {
              _tmpSedeLng = _cursor.getDouble(_cursorIndexOfSedeLng);
            }
            _result = new UserEntity(_tmpEmail,_tmpPassHash,_tmpNombre,_tmpRol,_tmpPlanEndMillis,_tmpSedeId,_tmpSedeName,_tmpSedeLat,_tmpSedeLng);
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
  public Flow<UserEntity> observeByEmail(final String email) {
    final String _sql = "SELECT * FROM users WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users"}, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passHash");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfRol = CursorUtil.getColumnIndexOrThrow(_cursor, "rol");
          final int _cursorIndexOfPlanEndMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planEndMillis");
          final int _cursorIndexOfSedeId = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeId");
          final int _cursorIndexOfSedeName = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeName");
          final int _cursorIndexOfSedeLat = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeLat");
          final int _cursorIndexOfSedeLng = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeLng");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassHash;
            if (_cursor.isNull(_cursorIndexOfPassHash)) {
              _tmpPassHash = null;
            } else {
              _tmpPassHash = _cursor.getString(_cursorIndexOfPassHash);
            }
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpRol;
            if (_cursor.isNull(_cursorIndexOfRol)) {
              _tmpRol = null;
            } else {
              _tmpRol = _cursor.getString(_cursorIndexOfRol);
            }
            final Long _tmpPlanEndMillis;
            if (_cursor.isNull(_cursorIndexOfPlanEndMillis)) {
              _tmpPlanEndMillis = null;
            } else {
              _tmpPlanEndMillis = _cursor.getLong(_cursorIndexOfPlanEndMillis);
            }
            final Integer _tmpSedeId;
            if (_cursor.isNull(_cursorIndexOfSedeId)) {
              _tmpSedeId = null;
            } else {
              _tmpSedeId = _cursor.getInt(_cursorIndexOfSedeId);
            }
            final String _tmpSedeName;
            if (_cursor.isNull(_cursorIndexOfSedeName)) {
              _tmpSedeName = null;
            } else {
              _tmpSedeName = _cursor.getString(_cursorIndexOfSedeName);
            }
            final Double _tmpSedeLat;
            if (_cursor.isNull(_cursorIndexOfSedeLat)) {
              _tmpSedeLat = null;
            } else {
              _tmpSedeLat = _cursor.getDouble(_cursorIndexOfSedeLat);
            }
            final Double _tmpSedeLng;
            if (_cursor.isNull(_cursorIndexOfSedeLng)) {
              _tmpSedeLng = null;
            } else {
              _tmpSedeLng = _cursor.getDouble(_cursorIndexOfSedeLng);
            }
            _result = new UserEntity(_tmpEmail,_tmpPassHash,_tmpNombre,_tmpRol,_tmpPlanEndMillis,_tmpSedeId,_tmpSedeName,_tmpSedeLat,_tmpSedeLng);
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
  public Flow<List<UserEntity>> observeAllExcept(final String excludeEmail) {
    final String _sql = "SELECT * FROM users WHERE email != ? ORDER BY email ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (excludeEmail == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, excludeEmail);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users"}, new Callable<List<UserEntity>>() {
      @Override
      @NonNull
      public List<UserEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passHash");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfRol = CursorUtil.getColumnIndexOrThrow(_cursor, "rol");
          final int _cursorIndexOfPlanEndMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planEndMillis");
          final int _cursorIndexOfSedeId = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeId");
          final int _cursorIndexOfSedeName = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeName");
          final int _cursorIndexOfSedeLat = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeLat");
          final int _cursorIndexOfSedeLng = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeLng");
          final List<UserEntity> _result = new ArrayList<UserEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserEntity _item;
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassHash;
            if (_cursor.isNull(_cursorIndexOfPassHash)) {
              _tmpPassHash = null;
            } else {
              _tmpPassHash = _cursor.getString(_cursorIndexOfPassHash);
            }
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpRol;
            if (_cursor.isNull(_cursorIndexOfRol)) {
              _tmpRol = null;
            } else {
              _tmpRol = _cursor.getString(_cursorIndexOfRol);
            }
            final Long _tmpPlanEndMillis;
            if (_cursor.isNull(_cursorIndexOfPlanEndMillis)) {
              _tmpPlanEndMillis = null;
            } else {
              _tmpPlanEndMillis = _cursor.getLong(_cursorIndexOfPlanEndMillis);
            }
            final Integer _tmpSedeId;
            if (_cursor.isNull(_cursorIndexOfSedeId)) {
              _tmpSedeId = null;
            } else {
              _tmpSedeId = _cursor.getInt(_cursorIndexOfSedeId);
            }
            final String _tmpSedeName;
            if (_cursor.isNull(_cursorIndexOfSedeName)) {
              _tmpSedeName = null;
            } else {
              _tmpSedeName = _cursor.getString(_cursorIndexOfSedeName);
            }
            final Double _tmpSedeLat;
            if (_cursor.isNull(_cursorIndexOfSedeLat)) {
              _tmpSedeLat = null;
            } else {
              _tmpSedeLat = _cursor.getDouble(_cursorIndexOfSedeLat);
            }
            final Double _tmpSedeLng;
            if (_cursor.isNull(_cursorIndexOfSedeLng)) {
              _tmpSedeLng = null;
            } else {
              _tmpSedeLng = _cursor.getDouble(_cursorIndexOfSedeLng);
            }
            _item = new UserEntity(_tmpEmail,_tmpPassHash,_tmpNombre,_tmpRol,_tmpPlanEndMillis,_tmpSedeId,_tmpSedeName,_tmpSedeLat,_tmpSedeLng);
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
