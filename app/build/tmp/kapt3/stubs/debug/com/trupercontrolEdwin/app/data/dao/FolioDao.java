package com.trupercontrolEdwin.app.data.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u001b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001b\u0010\u0007\u001a\u0004\u0018\u00010\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\r0\f2\u0006\u0010\u000e\u001a\u00020\tH\'J\u0019\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0019\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0010\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J!\u0010\u0014\u001a\u00020\u00132\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0017"}, d2 = {"Lcom/trupercontrolEdwin/app/data/dao/FolioDao;", "", "findByFolioTruper", "Lcom/trupercontrolEdwin/app/data/entities/Folio;", "folioTruper", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "folioId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByRuta", "Lkotlinx/coroutines/flow/Flow;", "", "rutaId", "insert", "folio", "(Lcom/trupercontrolEdwin/app/data/entities/Folio;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "", "updateEstado", "estado", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao
public abstract interface FolioDao {
    
    @androidx.room.Query(value = "SELECT * FROM folios WHERE rutaId = :rutaId ORDER BY folioTruper ASC")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.trupercontrolEdwin.app.data.entities.Folio>> getByRuta(long rutaId);
    
    @androidx.room.Query(value = "SELECT * FROM folios WHERE folioTruper = :folioTruper LIMIT 1")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object findByFolioTruper(@org.jetbrains.annotations.NotNull
    java.lang.String folioTruper, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.trupercontrolEdwin.app.data.entities.Folio> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM folios WHERE id = :folioId LIMIT 1")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getById(long folioId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.trupercontrolEdwin.app.data.entities.Folio> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.trupercontrolEdwin.app.data.entities.Folio folio, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.trupercontrolEdwin.app.data.entities.Folio folio, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE folios SET estado = :estado WHERE id = :folioId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateEstado(long folioId, @org.jetbrains.annotations.NotNull
    java.lang.String estado, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}