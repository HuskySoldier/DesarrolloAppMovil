package cl.gymtastic.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u0012\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u000e0\rJ\u0012\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u000e0\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcl/gymtastic/app/data/repository/ProductsRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "db", "Lcl/gymtastic/app/data/local/db/GymTasticDatabase;", "getById", "Lcl/gymtastic/app/data/local/entity/ProductEntity;", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeMerch", "Lkotlinx/coroutines/flow/Flow;", "", "observePlanes", "app_debug"})
public final class ProductsRepository {
    @org.jetbrains.annotations.NotNull()
    private final cl.gymtastic.app.data.local.db.GymTasticDatabase db = null;
    
    public ProductsRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<cl.gymtastic.app.data.local.entity.ProductEntity>> observePlanes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<cl.gymtastic.app.data.local.entity.ProductEntity>> observeMerch() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super cl.gymtastic.app.data.local.entity.ProductEntity> $completion) {
        return null;
    }
}