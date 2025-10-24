package cl.gymtastic.app.ui.checkin;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000^\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\u0018\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007\u001a6\u0010\t\u001a\u00020\u00012\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u000fH\u0003\u001a\u0010\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\rH\u0003\u001a!\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0016H\u0002\u00a2\u0006\u0002\u0010\u0018\u001a\u0017\u0010\u0019\u001a\u00020\u00142\b\u0010\u001a\u001a\u0004\u0018\u00010\u0016H\u0002\u00a2\u0006\u0002\u0010\u001b\u001a*\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"H\u0002\u001a\u001a\u0010$\u001a\u00020\u0001*\u00020%2\u0006\u0010&\u001a\u00020\u0014H\u0082@\u00a2\u0006\u0002\u0010\'\u00a8\u0006("}, d2 = {"AttendanceCard", "", "e", "Lcl/gymtastic/app/data/local/entity/AttendanceEntity;", "CheckInScreen", "nav", "Landroidx/navigation/NavController;", "windowSizeClass", "Landroidx/compose/material3/windowsizeclass/WindowSizeClass;", "PillButtonsRow", "userEntity", "Lcl/gymtastic/app/data/local/entity/UserEntity;", "hasOpenSession", "", "onCheckIn", "Lkotlin/Function0;", "onCheckOut", "StatusPill", "inCourse", "durationText", "", "start", "", "end", "(Ljava/lang/Long;Ljava/lang/Long;)Ljava/lang/String;", "fmt", "ts", "(Ljava/lang/Long;)Ljava/lang/String;", "generateBarcodeBitmap", "Landroidx/compose/ui/graphics/ImageBitmap;", "text", "format", "Lcom/google/zxing/BarcodeFormat;", "width", "", "height", "showMessage", "Landroidx/compose/material3/SnackbarHostState;", "msg", "(Landroidx/compose/material3/SnackbarHostState;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class CheckInScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CheckInScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController nav, @org.jetbrains.annotations.NotNull()
    androidx.compose.material3.windowsizeclass.WindowSizeClass windowSizeClass) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PillButtonsRow(cl.gymtastic.app.data.local.entity.UserEntity userEntity, boolean hasOpenSession, kotlin.jvm.functions.Function0<kotlin.Unit> onCheckIn, kotlin.jvm.functions.Function0<kotlin.Unit> onCheckOut) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AttendanceCard(cl.gymtastic.app.data.local.entity.AttendanceEntity e) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatusPill(boolean inCourse) {
    }
    
    private static final java.lang.String fmt(java.lang.Long ts) {
        return null;
    }
    
    private static final java.lang.String durationText(java.lang.Long start, java.lang.Long end) {
        return null;
    }
    
    private static final java.lang.Object showMessage(androidx.compose.material3.SnackbarHostState $this$showMessage, java.lang.String msg, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private static final androidx.compose.ui.graphics.ImageBitmap generateBarcodeBitmap(java.lang.String text, com.google.zxing.BarcodeFormat format, int width, int height) {
        return null;
    }
}