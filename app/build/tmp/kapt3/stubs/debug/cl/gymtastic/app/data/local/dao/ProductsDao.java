package cl.gymtastic.app.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bg\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u001c\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\n0\r2\u0006\u0010\u000e\u001a\u00020\u000fH\'\u00a8\u0006\u0010"}, d2 = {"Lcl/gymtastic/app/data/local/dao/ProductsDao;", "", "getById", "Lcl/gymtastic/app/data/local/entity/ProductEntity;", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertAll", "", "products", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeByTipo", "Lkotlinx/coroutines/flow/Flow;", "tipo", "", "app_debug"})
@androidx.room.Dao()
public abstract interface ProductsDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<cl.gymtastic.app.data.local.entity.ProductEntity> products, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM products WHERE tipo = :tipo")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<cl.gymtastic.app.data.local.entity.ProductEntity>> observeByTipo(@org.jetbrains.annotations.NotNull()
    java.lang.String tipo);
    
    @androidx.room.Query(value = "SELECT * FROM products WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super cl.gymtastic.app.data.local.entity.ProductEntity> $completion);
}