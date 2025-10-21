package cl.gymtastic.app.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00100\u000fH\'J\u001c\u0010\u0011\u001a\u00020\u00032\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\f0\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\b\u00a8\u0006\u0015"}, d2 = {"Lcl/gymtastic/app/data/local/dao/CartDao;", "", "clear", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "item", "Lcl/gymtastic/app/data/local/entity/CartItemEntity;", "(Lcl/gymtastic/app/data/local/entity/CartItemEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getQtyFor", "", "productId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeAll", "Lkotlinx/coroutines/flow/Flow;", "", "removeByProductIds", "productIds", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsert", "app_debug"})
@androidx.room.Dao()
public abstract interface CartDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    cl.gymtastic.app.data.local.entity.CartItemEntity item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM cart_items")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<cl.gymtastic.app.data.local.entity.CartItemEntity>> observeAll();
    
    @androidx.room.Query(value = "DELETE FROM cart_items")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clear(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    cl.gymtastic.app.data.local.entity.CartItemEntity item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COALESCE(SUM(qty), 0) FROM cart_items WHERE productId = :productId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getQtyFor(long productId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "DELETE FROM cart_items WHERE productId IN (:productIds)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object removeByProductIds(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> productIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}