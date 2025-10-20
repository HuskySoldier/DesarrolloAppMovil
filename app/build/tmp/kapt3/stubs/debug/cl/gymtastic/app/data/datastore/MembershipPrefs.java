package cl.gymtastic.app.data.datastore;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001!B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u00182\u0006\u0010\u0010\u001a\u00020\u0011J>\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\t2\u0006\u0010\u001c\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u001f\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u0010 R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcl/gymtastic/app/data/datastore/MembershipPrefs;", "", "()V", "KEY_ACTIVE", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "KEY_PLAN_END", "", "KEY_SEDE_ID", "", "KEY_SEDE_LAT", "", "KEY_SEDE_LNG", "KEY_SEDE_NAME", "", "canPurchaseNewPlan", "ctx", "Landroid/content/Context;", "thresholdDays", "(Landroid/content/Context;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clear", "", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observe", "Lkotlinx/coroutines/flow/Flow;", "Lcl/gymtastic/app/data/datastore/MembershipPrefs$State;", "setActiveWithSede", "id", "name", "lat", "lng", "planEndMillis", "(Landroid/content/Context;ILjava/lang/String;DDJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "State", "app_debug"})
public final class MembershipPrefs {
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> KEY_ACTIVE = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Integer> KEY_SEDE_ID = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_SEDE_NAME = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Double> KEY_SEDE_LAT = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Double> KEY_SEDE_LNG = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> KEY_PLAN_END = null;
    @org.jetbrains.annotations.NotNull()
    public static final cl.gymtastic.app.data.datastore.MembershipPrefs INSTANCE = null;
    
    private MembershipPrefs() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<cl.gymtastic.app.data.datastore.MembershipPrefs.State> observe(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setActiveWithSede(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx, int id, @org.jetbrains.annotations.NotNull()
    java.lang.String name, double lat, double lng, long planEndMillis, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object canPurchaseNewPlan(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx, long thresholdDays, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clear(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u001f\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0014J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u0010\u0010\u001f\u001a\u0004\u0018\u00010\tH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0017J\u0010\u0010 \u001a\u0004\u0018\u00010\tH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0017J\u0010\u0010!\u001a\u0004\u0018\u00010\fH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0011JT\u0010\"\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00c6\u0001\u00a2\u0006\u0002\u0010#J\u0013\u0010$\u001a\u00020\u00032\b\u0010%\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010&\u001a\u00020\u0005H\u00d6\u0001J\u0017\u0010\'\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010(\u001a\u00020\f\u00a2\u0006\u0002\u0010)J\t\u0010*\u001a\u00020\u0007H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0015\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u0015\u001a\u0004\b\u0013\u0010\u0014R\u0015\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\n\n\u0002\u0010\u0018\u001a\u0004\b\u0016\u0010\u0017R\u0015\u0010\n\u001a\u0004\u0018\u00010\t\u00a2\u0006\n\n\u0002\u0010\u0018\u001a\u0004\b\u0019\u0010\u0017R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006+"}, d2 = {"Lcl/gymtastic/app/data/datastore/MembershipPrefs$State;", "", "hasActivePlan", "", "sedeId", "", "sedeName", "", "sedeLat", "", "sedeLng", "planEndMillis", "", "(ZLjava/lang/Integer;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Long;)V", "getHasActivePlan", "()Z", "getPlanEndMillis", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getSedeId", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getSedeLat", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getSedeLng", "getSedeName", "()Ljava/lang/String;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(ZLjava/lang/Integer;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Long;)Lcl/gymtastic/app/data/datastore/MembershipPrefs$State;", "equals", "other", "hashCode", "remainingDays", "now", "(J)Ljava/lang/Long;", "toString", "app_debug"})
    public static final class State {
        private final boolean hasActivePlan = false;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer sedeId = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String sedeName = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Double sedeLat = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Double sedeLng = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Long planEndMillis = null;
        
        public State(boolean hasActivePlan, @org.jetbrains.annotations.Nullable()
        java.lang.Integer sedeId, @org.jetbrains.annotations.Nullable()
        java.lang.String sedeName, @org.jetbrains.annotations.Nullable()
        java.lang.Double sedeLat, @org.jetbrains.annotations.Nullable()
        java.lang.Double sedeLng, @org.jetbrains.annotations.Nullable()
        java.lang.Long planEndMillis) {
            super();
        }
        
        public final boolean getHasActivePlan() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getSedeId() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getSedeName() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Double getSedeLat() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Double getSedeLng() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long getPlanEndMillis() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long remainingDays(long now) {
            return null;
        }
        
        public State() {
            super();
        }
        
        public final boolean component1() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Double component4() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Double component5() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long component6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final cl.gymtastic.app.data.datastore.MembershipPrefs.State copy(boolean hasActivePlan, @org.jetbrains.annotations.Nullable()
        java.lang.Integer sedeId, @org.jetbrains.annotations.Nullable()
        java.lang.String sedeName, @org.jetbrains.annotations.Nullable()
        java.lang.Double sedeLat, @org.jetbrains.annotations.Nullable()
        java.lang.Double sedeLng, @org.jetbrains.annotations.Nullable()
        java.lang.Long planEndMillis) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}