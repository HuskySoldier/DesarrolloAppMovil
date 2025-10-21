package cl.gymtastic.app.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\"\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00a7@\u00a2\u0006\u0002\u0010\tJ\"\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00a7@\u00a2\u0006\u0002\u0010\tJ\"\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u00032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u0012H\'J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u0012H\'J\u001e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0018\u00a8\u0006\u0019"}, d2 = {"Lcl/gymtastic/app/data/local/dao/ProductsDao;", "", "getAll", "", "Lcl/gymtastic/app/data/local/entity/ProductEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByIds", "ids", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getNamesByIds", "Lcl/gymtastic/app/data/local/dao/ProductNameProjection;", "getStockByIds", "Lcl/gymtastic/app/data/local/dao/ProductStockProjection;", "insertAll", "", "products", "observeMerch", "Lkotlinx/coroutines/flow/Flow;", "observePlanes", "tryDecrementStock", "", "id", "qty", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface ProductsDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<cl.gymtastic.app.data.local.entity.ProductEntity> products, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT id, stock FROM products WHERE id IN (:ids)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getStockByIds(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> ids, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<cl.gymtastic.app.data.local.dao.ProductStockProjection>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM products")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<cl.gymtastic.app.data.local.entity.ProductEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM products WHERE id IN (:ids)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByIds(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> ids, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<cl.gymtastic.app.data.local.entity.ProductEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT id, nombre FROM products WHERE id IN (:ids)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getNamesByIds(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> ids, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<cl.gymtastic.app.data.local.dao.ProductNameProjection>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM products WHERE tipo = \'plan\'")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<cl.gymtastic.app.data.local.entity.ProductEntity>> observePlanes();
    
    @androidx.room.Query(value = "SELECT * FROM products WHERE tipo = \'merch\'")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<cl.gymtastic.app.data.local.entity.ProductEntity>> observeMerch();
    
    @androidx.room.Query(value = "\n        UPDATE products\n        SET stock = stock - :qty\n        WHERE id = :id\n          AND tipo = \'merch\'\n          AND stock IS NOT NULL\n          AND stock >= :qty\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object tryDecrementStock(long id, int qty, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}