package cl.gymtastic.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J&\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u000f\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0012J\u0012\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00150\u0014J\u0016\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\u00020\b2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\n0\u0015H\u0086@\u00a2\u0006\u0002\u0010\u001cR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcl/gymtastic/app/data/repository/CartRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "db", "Lcl/gymtastic/app/data/local/db/GymTasticDatabase;", "add", "", "productId", "", "qty", "", "unitPrice", "(JIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clear", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getQtyFor", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeCart", "Lkotlinx/coroutines/flow/Flow;", "", "Lcl/gymtastic/app/data/local/entity/CartItemEntity;", "remove", "item", "(Lcl/gymtastic/app/data/local/entity/CartItemEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "removeByProductIds", "ids", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class CartRepository {
    @org.jetbrains.annotations.NotNull()
    private final cl.gymtastic.app.data.local.db.GymTasticDatabase db = null;
    
    public CartRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<cl.gymtastic.app.data.local.entity.CartItemEntity>> observeCart() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object add(long productId, int qty, int unitPrice, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clear(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object remove(@org.jetbrains.annotations.NotNull()
    cl.gymtastic.app.data.local.entity.CartItemEntity item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getQtyFor(long productId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object removeByProductIds(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> ids, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}