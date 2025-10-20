package cl.gymtastic.app.data.local.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&\u00a8\u0006\u0010"}, d2 = {"Lcl/gymtastic/app/data/local/db/GymTasticDatabase;", "Landroidx/room/RoomDatabase;", "()V", "attendance", "Lcl/gymtastic/app/data/local/dao/AttendanceDao;", "bookings", "Lcl/gymtastic/app/data/local/dao/BookingsDao;", "cart", "Lcl/gymtastic/app/data/local/dao/CartDao;", "products", "Lcl/gymtastic/app/data/local/dao/ProductsDao;", "trainers", "Lcl/gymtastic/app/data/local/dao/TrainersDao;", "users", "Lcl/gymtastic/app/data/local/dao/UsersDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {cl.gymtastic.app.data.local.entity.UserEntity.class, cl.gymtastic.app.data.local.entity.ProductEntity.class, cl.gymtastic.app.data.local.entity.CartItemEntity.class, cl.gymtastic.app.data.local.entity.AttendanceEntity.class, cl.gymtastic.app.data.local.entity.TrainerEntity.class, cl.gymtastic.app.data.local.entity.BookingEntity.class}, version = 3, exportSchema = false)
public abstract class GymTasticDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile cl.gymtastic.app.data.local.db.GymTasticDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_1_2 = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_2_3 = null;
    @org.jetbrains.annotations.NotNull()
    public static final cl.gymtastic.app.data.local.db.GymTasticDatabase.Companion Companion = null;
    
    public GymTasticDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.gymtastic.app.data.local.dao.UsersDao users();
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.gymtastic.app.data.local.dao.ProductsDao products();
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.gymtastic.app.data.local.dao.CartDao cart();
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.gymtastic.app.data.local.dao.AttendanceDao attendance();
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.gymtastic.app.data.local.dao.TrainersDao trainers();
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.gymtastic.app.data.local.dao.BookingsDao bookings();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004H\u0082@\u00a2\u0006\u0002\u0010\u000eR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcl/gymtastic/app/data/local/db/GymTasticDatabase$Companion;", "", "()V", "INSTANCE", "Lcl/gymtastic/app/data/local/db/GymTasticDatabase;", "MIGRATION_1_2", "Landroidx/room/migration/Migration;", "MIGRATION_2_3", "get", "context", "Landroid/content/Context;", "seedIfNeeded", "", "db", "(Lcl/gymtastic/app/data/local/db/GymTasticDatabase;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final cl.gymtastic.app.data.local.db.GymTasticDatabase get(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        private final java.lang.Object seedIfNeeded(cl.gymtastic.app.data.local.db.GymTasticDatabase db, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
    }
}