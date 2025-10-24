package cl.gymtastic.app.util;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\bJ\u000e\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\bJ\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0007\u001a\u00020\bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcl/gymtastic/app/util/ServiceLocator;", "", "()V", "productsRepo", "Lcl/gymtastic/app/data/local/ProductsRepository;", "attendance", "Lcl/gymtastic/app/data/repository/AttendanceRepository;", "context", "Landroid/content/Context;", "auth", "Lcl/gymtastic/app/data/repository/AuthRepository;", "bookings", "Lcl/gymtastic/app/data/repository/BookingsRepository;", "cart", "Lcl/gymtastic/app/data/repository/CartRepository;", "db", "Lcl/gymtastic/app/data/local/db/GymTasticDatabase;", "ctx", "products", "trainers", "Lcl/gymtastic/app/data/repository/TrainersRepository;", "app_debug"})
public final class ServiceLocator {
    @org.jetbrains.annotations.Nullable()
    private static cl.gymtastic.app.data.local.ProductsRepository productsRepo;
    @org.jetbrains.annotations.NotNull()
    public static final cl.gymtastic.app.util.ServiceLocator INSTANCE = null;
    
    private ServiceLocator() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final cl.gymtastic.app.data.local.ProductsRepository products(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final cl.gymtastic.app.data.repository.AuthRepository auth(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final cl.gymtastic.app.data.repository.CartRepository cart(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final cl.gymtastic.app.data.repository.AttendanceRepository attendance(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final cl.gymtastic.app.data.repository.TrainersRepository trainers(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final cl.gymtastic.app.data.repository.BookingsRepository bookings(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final cl.gymtastic.app.data.local.db.GymTasticDatabase db(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
        return null;
    }
}