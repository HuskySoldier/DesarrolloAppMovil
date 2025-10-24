package cl.gymtastic.app.ui.auth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a.\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0007\u0010\b\u001a\u0018\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007\u001a\u0010\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0002\u001a\u001c\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00100\u00122\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0013"}, d2 = {"PasswordStrengthBar", "", "level", "", "barHeightDp", "activeColor", "Landroidx/compose/ui/graphics/Color;", "PasswordStrengthBar-mxwnekA", "(IIJ)V", "RegisterScreen", "nav", "Landroidx/navigation/NavController;", "windowSizeClass", "Landroidx/compose/material3/windowsizeclass/WindowSizeClass;", "calcPasswordStrength", "pw", "", "strengthColorAndLabel", "Lkotlin/Pair;", "app_debug"})
public final class RegisterScreenKt {
    
    @android.annotation.SuppressLint(value = {"UnrememberedMutableState"})
    @androidx.compose.runtime.Composable()
    public static final void RegisterScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController nav, @org.jetbrains.annotations.NotNull()
    androidx.compose.material3.windowsizeclass.WindowSizeClass windowSizeClass) {
    }
    
    /**
     * Devuelve (color, etiqueta) para el nivel de fuerza 0..4.
     */
    @androidx.compose.runtime.Composable()
    private static final kotlin.Pair<androidx.compose.ui.graphics.Color, java.lang.String> strengthColorAndLabel(int level) {
        return null;
    }
    
    /**
     * Calcula la fuerza de la contraseña en una escala 0..4.
     * +1 por cada criterio: largo >= 8, mayúsculas, dígitos, símbolos.
     * Si el largo >= 12, agrega un punto extra (máximo 4).
     */
    private static final int calcPasswordStrength(java.lang.String pw) {
        return 0;
    }
}