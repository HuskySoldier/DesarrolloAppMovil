package cl.gymtastic.app.ui.planes;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000@\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a_\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000eH\u0003\u00a2\u0006\u0002\u0010\u0010\u001a\u0018\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0007\u00a8\u0006\u0016"}, d2 = {"PlanCardContent", "", "product", "Lcl/gymtastic/app/data/local/entity/ProductEntity;", "unitPrice", "", "canBuy", "", "remainingDays", "", "onAddToCart", "Lkotlin/Function0;", "onBuyNow", "onShowSnackbar", "Lkotlin/Function1;", "", "(Lcl/gymtastic/app/data/local/entity/ProductEntity;IZLjava/lang/Long;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "PlanesScreen", "nav", "Landroidx/navigation/NavController;", "windowSizeClass", "Landroidx/compose/material3/windowsizeclass/WindowSizeClass;", "app_debug"})
public final class PlanesScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void PlanesScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController nav, @org.jetbrains.annotations.NotNull()
    androidx.compose.material3.windowsizeclass.WindowSizeClass windowSizeClass) {
    }
    
    /**
     * Composable interno para el contenido de la tarjeta del plan (reutilizable)
     */
    @androidx.compose.runtime.Composable()
    private static final void PlanCardContent(cl.gymtastic.app.data.local.entity.ProductEntity product, int unitPrice, boolean canBuy, java.lang.Long remainingDays, kotlin.jvm.functions.Function0<kotlin.Unit> onAddToCart, kotlin.jvm.functions.Function0<kotlin.Unit> onBuyNow, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onShowSnackbar) {
    }
}