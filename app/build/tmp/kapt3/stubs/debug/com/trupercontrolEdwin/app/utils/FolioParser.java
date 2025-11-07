package com.trupercontrolEdwin.app.utils;

/**
 * Utilidad para interpretar los documentos (PDF o texto OCR) relacionados con los folios.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0002\u0014\u0015B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0018\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bJ\u0010\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000e\u001a\u00020\u0004J\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u000e\u001a\u00020\u0004J\u000e\u0010\u0011\u001a\u0004\u0018\u00010\u0004*\u00020\u0004H\u0002J\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0006*\u00020\u0004H\u0002\u00a2\u0006\u0002\u0010\u0013\u00a8\u0006\u0016"}, d2 = {"Lcom/trupercontrolEdwin/app/utils/FolioParser;", "", "()V", "formatMetros", "", "valor", "", "generarMensajeValidacion", "folio", "Lcom/trupercontrolEdwin/app/data/entities/Folio;", "ruta", "Lcom/trupercontrolEdwin/app/data/entities/Ruta;", "parseCambio", "Lcom/trupercontrolEdwin/app/utils/FolioParser$CambioData;", "texto", "parseSolicitud", "Lcom/trupercontrolEdwin/app/utils/FolioParser$SolicitudData;", "lineOrNull", "normalizeDouble", "(Ljava/lang/String;)Ljava/lang/Double;", "CambioData", "SolicitudData", "app_debug"})
public final class FolioParser {
    @org.jetbrains.annotations.NotNull
    public static final com.trupercontrolEdwin.app.utils.FolioParser INSTANCE = null;
    
    private FolioParser() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.trupercontrolEdwin.app.utils.FolioParser.SolicitudData parseSolicitud(@org.jetbrains.annotations.NotNull
    java.lang.String texto) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.trupercontrolEdwin.app.utils.FolioParser.CambioData parseCambio(@org.jetbrains.annotations.NotNull
    java.lang.String texto) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String generarMensajeValidacion(@org.jetbrains.annotations.NotNull
    com.trupercontrolEdwin.app.data.entities.Folio folio, @org.jetbrains.annotations.Nullable
    com.trupercontrolEdwin.app.data.entities.Ruta ruta) {
        return null;
    }
    
    private final java.lang.String lineOrNull(java.lang.String $this$lineOrNull) {
        return null;
    }
    
    private final java.lang.Double normalizeDouble(java.lang.String $this$normalizeDouble) {
        return null;
    }
    
    private final java.lang.String formatMetros(double valor) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B;\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u000eJ\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u000eJ\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003JF\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\b\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001J\t\u0010\u001e\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000eR\u0015\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\u0010\u0010\u000eR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\u0011\u0010\u000e\u00a8\u0006\u001f"}, d2 = {"Lcom/trupercontrolEdwin/app/utils/FolioParser$CambioData;", "", "folio", "", "metrosSolicitud", "", "metrosDiferencia", "metrosFinales", "comentarios", "(Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/String;)V", "getComentarios", "()Ljava/lang/String;", "getFolio", "getMetrosDiferencia", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getMetrosFinales", "getMetrosSolicitud", "component1", "component2", "component3", "component4", "component5", "copy", "(Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/String;)Lcom/trupercontrolEdwin/app/utils/FolioParser$CambioData;", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    public static final class CambioData {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String folio = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.Double metrosSolicitud = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.Double metrosDiferencia = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.Double metrosFinales = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String comentarios = null;
        
        public CambioData(@org.jetbrains.annotations.NotNull
        java.lang.String folio, @org.jetbrains.annotations.Nullable
        java.lang.Double metrosSolicitud, @org.jetbrains.annotations.Nullable
        java.lang.Double metrosDiferencia, @org.jetbrains.annotations.Nullable
        java.lang.Double metrosFinales, @org.jetbrains.annotations.NotNull
        java.lang.String comentarios) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getFolio() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Double getMetrosSolicitud() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Double getMetrosDiferencia() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Double getMetrosFinales() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getComentarios() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Double component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Double component3() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Double component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component5() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.trupercontrolEdwin.app.utils.FolioParser.CambioData copy(@org.jetbrains.annotations.NotNull
        java.lang.String folio, @org.jetbrains.annotations.Nullable
        java.lang.Double metrosSolicitud, @org.jetbrains.annotations.Nullable
        java.lang.Double metrosDiferencia, @org.jetbrains.annotations.Nullable
        java.lang.Double metrosFinales, @org.jetbrains.annotations.NotNull
        java.lang.String comentarios) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0013\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0014\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010\u0016\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u000eJH\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\bH\u00c6\u0001\u00a2\u0006\u0002\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001J\t\u0010\u001e\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0015\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000eR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000bR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000b\u00a8\u0006\u001f"}, d2 = {"Lcom/trupercontrolEdwin/app/utils/FolioParser$SolicitudData;", "", "folio", "", "nombreNegocio", "tipoFachada", "direccion", "metrosReportados", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;)V", "getDireccion", "()Ljava/lang/String;", "getFolio", "getMetrosReportados", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getNombreNegocio", "getTipoFachada", "component1", "component2", "component3", "component4", "component5", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;)Lcom/trupercontrolEdwin/app/utils/FolioParser$SolicitudData;", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    public static final class SolicitudData {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String folio = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.String nombreNegocio = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.String tipoFachada = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.String direccion = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.Double metrosReportados = null;
        
        public SolicitudData(@org.jetbrains.annotations.NotNull
        java.lang.String folio, @org.jetbrains.annotations.Nullable
        java.lang.String nombreNegocio, @org.jetbrains.annotations.Nullable
        java.lang.String tipoFachada, @org.jetbrains.annotations.Nullable
        java.lang.String direccion, @org.jetbrains.annotations.Nullable
        java.lang.Double metrosReportados) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getFolio() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getNombreNegocio() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getTipoFachada() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getDireccion() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Double getMetrosReportados() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component4() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Double component5() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.trupercontrolEdwin.app.utils.FolioParser.SolicitudData copy(@org.jetbrains.annotations.NotNull
        java.lang.String folio, @org.jetbrains.annotations.Nullable
        java.lang.String nombreNegocio, @org.jetbrains.annotations.Nullable
        java.lang.String tipoFachada, @org.jetbrains.annotations.Nullable
        java.lang.String direccion, @org.jetbrains.annotations.Nullable
        java.lang.Double metrosReportados) {
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
}