package com.trupercontrolEdwin.app.utils;

/**
 * Procesa una imagen (foto del listado o del texto de cambio)
 * y devuelve TODO el texto reconocido como String.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0005J\u0015\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\tJ\u0019\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\r\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000e"}, d2 = {"Lcom/trupercontrolEdwin/app/utils/OcrProcessor;", "", "()V", "extraerFolios", "", "", "texto", "extraerMetros", "", "(Ljava/lang/String;)Ljava/lang/Double;", "procesarImagen", "bitmap", "Landroid/graphics/Bitmap;", "(Landroid/graphics/Bitmap;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class OcrProcessor {
    @org.jetbrains.annotations.NotNull
    public static final com.trupercontrolEdwin.app.utils.OcrProcessor INSTANCE = null;
    
    private OcrProcessor() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object procesarImagen(@org.jetbrains.annotations.NotNull
    android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    /**
     * Intenta detectar patrones de folios en un texto OCRizado.
     * Por ejemplo: "Folio :54376" o "Folio 54376"
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> extraerFolios(@org.jetbrains.annotations.NotNull
    java.lang.String texto) {
        return null;
    }
    
    /**
     * Intenta detectar metros:
     * "36.00 m²" o "36 m2" o "Total 44.87 m²"
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Double extraerMetros(@org.jetbrains.annotations.NotNull
    java.lang.String texto) {
        return null;
    }
}