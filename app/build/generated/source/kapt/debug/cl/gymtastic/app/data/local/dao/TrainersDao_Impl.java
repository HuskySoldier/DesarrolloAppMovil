package cl.gymtastic.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import cl.gymtastic.app.data.local.entity.TrainerEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class TrainersDao_Impl implements TrainersDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TrainerEntity> __insertionAdapterOfTrainerEntity;

  private final EntityDeletionOrUpdateAdapter<TrainerEntity> __deletionAdapterOfTrainerEntity;

  private final EntityUpsertionAdapter<TrainerEntity> __upsertionAdapterOfTrainerEntity;

  public TrainersDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrainerEntity = new EntityInsertionAdapter<TrainerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `trainers` (`id`,`nombre`,`fono`,`email`,`especialidad`,`img`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrainerEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombre());
        }
        if (entity.getFono() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFono());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmail());
        }
        if (entity.getEspecialidad() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEspecialidad());
        }
        if (entity.getImg() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getImg());
        }
      }
    };
    this.__deletionAdapterOfTrainerEntity = new EntityDeletionOrUpdateAdapter<TrainerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `trainers` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrainerEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__upsertionAdapterOfTrainerEntity = new EntityUpsertionAdapter<TrainerEntity>(new EntityInsertionAdapter<TrainerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `trainers` (`id`,`nombre`,`fono`,`email`,`especialidad`,`img`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrainerEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombre());
        }
        if (entity.getFono() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFono());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmail());
        }
        if (entity.getEspecialidad() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEspecialidad());
        }
        if (entity.getImg() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getImg());
        }
      }
    }, new EntityDeletionOrUpdateAdapter<TrainerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `trainers` SET `id` = ?,`nombre` = ?,`fono` = ?,`email` = ?,`especialidad` = ?,`img` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrainerEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombre());
        }
        if (entity.getFono() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFono());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmail());
        }
        if (entity.getEspecialidad() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEspecialidad());
        }
        if (entity.getImg() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getImg());
        }
        statement.bindLong(7, entity.getId());
      }
    });
  }

  @Override
  public Object insertAll(final List<TrainerEntity> list, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTrainerEntity.insert(list);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object delete(final TrainerEntity product, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTrainerEntity.handle(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object save(final TrainerEntity product, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfTrainerEntity.upsert(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Flow<List<TrainerEntity>> observeAll() {
    final String _sql = "SELECT * FROM trainers";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trainers"}, new Callable<List<TrainerEntity>>() {
      @Override
      @NonNull
      public List<TrainerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfFono = CursorUtil.getColumnIndexOrThrow(_cursor, "fono");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfEspecialidad = CursorUtil.getColumnIndexOrThrow(_cursor, "especialidad");
          final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
          final List<TrainerEntity> _result = new ArrayList<TrainerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrainerEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpFono;
            if (_cursor.isNull(_cursorIndexOfFono)) {
              _tmpFono = null;
            } else {
              _tmpFono = _cursor.getString(_cursorIndexOfFono);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpEspecialidad;
            if (_cursor.isNull(_cursorIndexOfEspecialidad)) {
              _tmpEspecialidad = null;
            } else {
              _tmpEspecialidad = _cursor.getString(_cursorIndexOfEspecialidad);
            }
            final String _tmpImg;
            if (_cursor.isNull(_cursorIndexOfImg)) {
              _tmpImg = null;
            } else {
              _tmpImg = _cursor.getString(_cursorIndexOfImg);
            }
            _item = new TrainerEntity(_tmpId,_tmpNombre,_tmpFono,_tmpEmail,_tmpEspecialidad,_tmpImg);
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
  public Object count(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM trainers";
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
    }, arg0);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
