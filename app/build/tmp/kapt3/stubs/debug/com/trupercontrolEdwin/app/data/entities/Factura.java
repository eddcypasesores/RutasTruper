package com.trupercontrolEdwin.app.data.entities;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b-\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B\u009b\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0014J\t\u0010(\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010,\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0006H\u00c6\u0003J\u0010\u00100\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001aJ\u000b\u00101\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\t\u00102\u001a\u00020\u000bH\u00c6\u0003J\t\u00103\u001a\u00020\u000bH\u00c6\u0003J\t\u00104\u001a\u00020\u000bH\u00c6\u0003J\t\u00105\u001a\u00020\u0006H\u00c6\u0003J\u00a8\u0001\u00106\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000b2\b\b\u0002\u0010\u000e\u001a\u00020\u00062\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0006H\u00c6\u0001\u00a2\u0006\u0002\u00107J\u0013\u00108\u001a\u0002092\b\u0010:\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010;\u001a\u00020\bH\u00d6\u0001J\t\u0010<\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\u000e\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0013\u0010\t\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u0015\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u001b\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0016R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001dR\u0011\u0010\f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0016R\u0013\u0010\u0013\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0016R\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0016R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010!R\u0011\u0010\r\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010!R\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u0016\u00a8\u0006="}, d2 = {"Lcom/trupercontrolEdwin/app/data/entities/Factura;", "", "id", "", "folioId", "folioFactura", "", "folioFacturaNum", "", "fechaEmision", "subtotal", "", "iva", "total", "estatus", "motivoCancelacion", "folioReemplaza", "pdfUri", "xmlUri", "observaciones", "(JJLjava/lang/String;Ljava/lang/Integer;Ljava/lang/String;DDDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getEstatus", "()Ljava/lang/String;", "getFechaEmision", "getFolioFactura", "getFolioFacturaNum", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getFolioId", "()J", "getFolioReemplaza", "getId", "getIva", "()D", "getMotivoCancelacion", "getObservaciones", "getPdfUri", "getSubtotal", "getTotal", "getXmlUri", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JJLjava/lang/String;Ljava/lang/Integer;Ljava/lang/String;DDDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/trupercontrolEdwin/app/data/entities/Factura;", "equals", "", "other", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "facturas")
public final class Factura {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    private final long folioId = 0L;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String folioFactura = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer folioFacturaNum = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String fechaEmision = null;
    private final double subtotal = 0.0;
    private final double iva = 0.0;
    private final double total = 0.0;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String estatus = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String motivoCancelacion = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String folioReemplaza = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String pdfUri = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String xmlUri = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String observaciones = null;
    
    public Factura(long id, long folioId, @org.jetbrains.annotations.NotNull
    java.lang.String folioFactura, @org.jetbrains.annotations.Nullable
    java.lang.Integer folioFacturaNum, @org.jetbrains.annotations.Nullable
    java.lang.String fechaEmision, double subtotal, double iva, double total, @org.jetbrains.annotations.NotNull
    java.lang.String estatus, @org.jetbrains.annotations.Nullable
    java.lang.String motivoCancelacion, @org.jetbrains.annotations.Nullable
    java.lang.String folioReemplaza, @org.jetbrains.annotations.Nullable
    java.lang.String pdfUri, @org.jetbrains.annotations.Nullable
    java.lang.String xmlUri, @org.jetbrains.annotations.Nullable
    java.lang.String observaciones) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final long getFolioId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getFolioFactura() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getFolioFacturaNum() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getFechaEmision() {
        return null;
    }
    
    public final double getSubtotal() {
        return 0.0;
    }
    
    public final double getIva() {
        return 0.0;
    }
    
    public final double getTotal() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getEstatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getMotivoCancelacion() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getFolioReemplaza() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getPdfUri() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getXmlUri() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getObservaciones() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component14() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component5() {
        return null;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    public final double component8() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.trupercontrolEdwin.app.data.entities.Factura copy(long id, long folioId, @org.jetbrains.annotations.NotNull
    java.lang.String folioFactura, @org.jetbrains.annotations.Nullable
    java.lang.Integer folioFacturaNum, @org.jetbrains.annotations.Nullable
    java.lang.String fechaEmision, double subtotal, double iva, double total, @org.jetbrains.annotations.NotNull
    java.lang.String estatus, @org.jetbrains.annotations.Nullable
    java.lang.String motivoCancelacion, @org.jetbrains.annotations.Nullable
    java.lang.String folioReemplaza, @org.jetbrains.annotations.Nullable
    java.lang.String pdfUri, @org.jetbrains.annotations.Nullable
    java.lang.String xmlUri, @org.jetbrains.annotations.Nullable
    java.lang.String observaciones) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}