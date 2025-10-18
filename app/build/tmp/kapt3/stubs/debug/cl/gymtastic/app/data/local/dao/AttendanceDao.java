package cl.gymtastic.app.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u0016\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000b2\u0006\u0010\u0004\u001a\u00020\u0003H\'J\u001e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0011\u00a8\u0006\u0012"}, d2 = {"Lcl/gymtastic/app/data/local/dao/AttendanceDao;", "", "findLastOpenAttendanceId", "", "userId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "reg", "Lcl/gymtastic/app/data/local/entity/AttendanceEntity;", "(Lcl/gymtastic/app/data/local/entity/AttendanceEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeByUser", "Lkotlinx/coroutines/flow/Flow;", "", "updateCheckOutById", "", "attendanceId", "checkOut", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface AttendanceDao {
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    cl.gymtastic.app.data.local.entity.AttendanceEntity reg, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM attendance WHERE userId = :userId ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<cl.gymtastic.app.data.local.entity.AttendanceEntity>> observeByUser(long userId);
    
    @androidx.room.Query(value = "\n        SELECT id FROM attendance\n        WHERE userId = :userId AND checkOutTimestamp IS NULL\n        ORDER BY timestamp DESC\n        LIMIT 1\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findLastOpenAttendanceId(long userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "\n        UPDATE attendance\n        SET checkOutTimestamp = :checkOut\n        WHERE id = :attendanceId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateCheckOutById(long attendanceId, long checkOut, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}