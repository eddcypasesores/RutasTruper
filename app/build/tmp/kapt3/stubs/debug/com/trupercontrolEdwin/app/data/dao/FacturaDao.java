package com.trupercontrolEdwin.app.data.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u0014\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u0019\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ!\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0011H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0013"}, d2 = {"Lcom/trupercontrolEdwin/app/data/dao/FacturaDao;", "", "getByFolio", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/trupercontrolEdwin/app/data/entities/Factura;", "folioId", "", "getPendientes", "insert", "factura", "(Lcom/trupercontrolEdwin/app/data/entities/Factura;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "", "updateEstatus", "facturaId", "estatus", "", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao
public abstract interface FacturaDao {
    
    @androidx.room.Query(value = "SELECT * FROM facturas WHERE folioId = :folioId")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.trupercontrolEdwin.app.data.entities.Factura>> getByFolio(long folioId);
    
    @androidx.room.Query(value = "SELECT * FROM facturas WHERE estatus = \'Pendiente\'")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.trupercontrolEdwin.app.data.entities.Factura>> getPendientes();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.trupercontrolEdwin.app.data.entities.Factura factura, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.trupercontrolEdwin.app.data.entities.Factura factura, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE facturas SET estatus = :estatus WHERE id = :facturaId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateEstatus(long facturaId, @org.jetbrains.annotations.NotNull
    java.lang.String estatus, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}