package com.trupercontrolEdwin.app.data.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH\'J\u001b\u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJ\u0019\u0010\u000e\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000f"}, d2 = {"Lcom/trupercontrolEdwin/app/data/dao/RutaDao;", "", "delete", "", "ruta", "Lcom/trupercontrolEdwin/app/data/entities/Ruta;", "(Lcom/trupercontrolEdwin/app/data/entities/Ruta;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "Lkotlinx/coroutines/flow/Flow;", "", "getById", "rutaId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "app_debug"})
@androidx.room.Dao
public abstract interface RutaDao {
    
    @androidx.room.Query(value = "SELECT * FROM rutas ORDER BY fecha DESC")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.trupercontrolEdwin.app.data.entities.Ruta>> getAll();
    
    @androidx.room.Query(value = "SELECT * FROM rutas WHERE id = :rutaId LIMIT 1")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getById(long rutaId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.trupercontrolEdwin.app.data.entities.Ruta> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.trupercontrolEdwin.app.data.entities.Ruta ruta, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Delete
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull
    com.trupercontrolEdwin.app.data.entities.Ruta ruta, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}