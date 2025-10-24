package cl.gymtastic.app.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\r0\f2\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u001e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0012\u00a8\u0006\u0013"}, d2 = {"Lcl/gymtastic/app/data/local/dao/AttendanceDao;", "", "findLastOpenAttendanceId", "", "userEmail", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "reg", "Lcl/gymtastic/app/data/local/entity/AttendanceEntity;", "(Lcl/gymtastic/app/data/local/entity/AttendanceEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeByUser", "Lkotlinx/coroutines/flow/Flow;", "", "updateCheckOutById", "", "attendanceId", "checkOut", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface AttendanceDao {
    
    /**
     * Inserta un nuevo registro de asistencia.
     * @param reg La entidad AttendanceEntity a insertar.
     * @return El ID (rowId) del registro insertado.
     */
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    cl.gymtastic.app.data.local.entity.AttendanceEntity reg, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * Observa (Flow) todos los registros de asistencia para un usuario específico,
     * ordenados por fecha descendente (más recientes primero).
     * @param userEmail El email del usuario cuyos registros se quieren observar.
     * @return Un Flow que emite la lista de AttendanceEntity para ese usuario.
     */
    @androidx.room.Query(value = "SELECT * FROM attendance WHERE userEmail = :userEmail ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<cl.gymtastic.app.data.local.entity.AttendanceEntity>> observeByUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userEmail);
    
    /**
     * Busca el ID del último registro de check-in que AÚN NO tiene check-out (checkOutTimestamp IS NULL)
     * para un usuario específico.
     * @param userEmail El email del usuario.
     * @return El ID (Long) del último registro abierto, o null si no hay ninguno abierto.
     */
    @androidx.room.Query(value = "SELECT id FROM attendance WHERE userEmail = :userEmail AND checkOutTimestamp IS NULL ORDER BY timestamp DESC LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findLastOpenAttendanceId(@org.jetbrains.annotations.NotNull()
    java.lang.String userEmail, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * Actualiza el timestamp de check-out para un registro de asistencia específico, identificado por su ID.
     * @param attendanceId El ID del registro de asistencia a actualizar.
     * @param checkOut El timestamp (en milisegundos) del momento del check-out.
     */
    @androidx.room.Query(value = "UPDATE attendance SET checkOutTimestamp = :checkOut WHERE id = :attendanceId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateCheckOutById(long attendanceId, long checkOut, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}