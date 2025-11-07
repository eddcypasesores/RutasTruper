package com.trupercontrolEdwin.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/trupercontrolEdwin/app/utils/PdfReader;", "", "()V", "esAcuseCancelacion", "", "texto", "", "esAvisoPago", "extraerMontos", "", "", "leerPdf", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "app_debug"})
public final class PdfReader {
    @org.jetbrains.annotations.NotNull
    public static final com.trupercontrolEdwin.app.utils.PdfReader INSTANCE = null;
    
    private PdfReader() {
        super();
    }
    
    /**
     * Lee un PDF desde un Uri (PDF de Truper, acuse de cancelación, aviso de pago)
     * y devuelve TODO el texto.
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String leerPdf(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
        return null;
    }
    
    /**
     * Intenta detectar si es un acuse SAT de cancelación (como el que subiste).
     */
    public final boolean esAcuseCancelacion(@org.jetbrains.annotations.NotNull
    java.lang.String texto) {
        return false;
    }
    
    /**
     * Intenta detectar si es aviso de pago TRUPER (como NP0000700139.PDF)
     */
    public final boolean esAvisoPago(@org.jetbrains.annotations.NotNull
    java.lang.String texto) {
        return false;
    }
    
    /**
     * Extrae totales $xx,xxx.xx
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.Double> extraerMontos(@org.jetbrains.annotations.NotNull
    java.lang.String texto) {
        return null;
    }
}