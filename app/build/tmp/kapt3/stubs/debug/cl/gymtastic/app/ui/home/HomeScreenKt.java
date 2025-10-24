package cl.gymtastic.app.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000`\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0004\u001a8\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a(\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\b\u001a\u00020\t2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a3\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00052\u0011\u0010\u0006\u001a\r\u0012\u0004\u0012\u00020\u00010\u000b\u00a2\u0006\u0002\b\u0013H\u0003\u001a8\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\u0018\u0010\u0017\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00190\u00182\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a<\u0010\u001b\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u000eH\u0003\u001a\u0018\u0010\"\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u001fH\u0007\u001aB\u0010#\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u000e2\u000e\u0010$\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u000bH\u0003\u001a-\u0010%\u001a\u00020\u00012\u0006\u0010&\u001a\u00020\t2\b\u0010\'\u001a\u0004\u0018\u00010(2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u00a2\u0006\u0002\u0010*\u001a\u0010\u0010+\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\u0003\u00a8\u0006,"}, d2 = {"ActionCard", "", "modifier", "Landroidx/compose/ui/Modifier;", "title", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "enabled", "", "onClick", "Lkotlin/Function0;", "CheckStatsCard", "counts", "Lcl/gymtastic/app/data/datastore/CheckCounts;", "onOpenHistory", "CompactStatBox", "value", "subtitle", "Landroidx/compose/runtime/Composable;", "DrawerContent", "nav", "Landroidx/navigation/NavController;", "drawerItems", "", "Lkotlin/Pair;", "onClose", "HomeContent", "userEntity", "Lcl/gymtastic/app/data/local/entity/UserEntity;", "windowSizeClass", "Landroidx/compose/material3/windowsizeclass/WindowSizeClass;", "authEmail", "checkCounts", "HomeScreen", "HomeScreenScaffold", "onOpenDrawer", "MembershipCard", "hasPlan", "planEndMillis", "", "onManagePlan", "(ZLjava/lang/Long;Lkotlin/jvm/functions/Function0;)V", "SectionTitle", "app_debug"})
public final class HomeScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController nav, @org.jetbrains.annotations.NotNull()
    androidx.compose.material3.windowsizeclass.WindowSizeClass windowSizeClass) {
    }
    
    /**
     * Contenido del Drawer
     */
    @androidx.compose.runtime.Composable()
    private static final void DrawerContent(androidx.navigation.NavController nav, java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> drawerItems, kotlin.jvm.functions.Function0<kotlin.Unit> onClose) {
    }
    
    /**
     * Scaffold principal
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void HomeScreenScaffold(androidx.navigation.NavController nav, cl.gymtastic.app.data.local.entity.UserEntity userEntity, androidx.compose.material3.windowsizeclass.WindowSizeClass windowSizeClass, java.lang.String authEmail, cl.gymtastic.app.data.datastore.CheckCounts checkCounts, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenDrawer) {
    }
    
    /**
     * Contenido principal
     */
    @androidx.compose.runtime.Composable()
    private static final void HomeContent(androidx.compose.ui.Modifier modifier, androidx.navigation.NavController nav, cl.gymtastic.app.data.local.entity.UserEntity userEntity, androidx.compose.material3.windowsizeclass.WindowSizeClass windowSizeClass, java.lang.String authEmail, cl.gymtastic.app.data.datastore.CheckCounts checkCounts) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionTitle(java.lang.String title) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ActionCard(androidx.compose.ui.Modifier modifier, java.lang.String title, androidx.compose.ui.graphics.vector.ImageVector icon, boolean enabled, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MembershipCard(boolean hasPlan, java.lang.Long planEndMillis, kotlin.jvm.functions.Function0<kotlin.Unit> onManagePlan) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CheckStatsCard(cl.gymtastic.app.data.datastore.CheckCounts counts, boolean enabled, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenHistory) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CompactStatBox(java.lang.String title, java.lang.String value, java.lang.String subtitle, kotlin.jvm.functions.Function0<kotlin.Unit> icon) {
    }
}