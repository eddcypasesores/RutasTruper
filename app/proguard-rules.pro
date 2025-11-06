# Mantener anotaciones de Room
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase

# Mantener modelos de Gson
-keep class com.trupercontrolEdwin.app.** { *; }

# ML Kit
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.** { *; }

# PDFBox
-keep class org.apache.pdfbox.** { *; }

# Apache POI
-keep class org.apache.poi.** { *; }

# Evitar warnings de Kotlin
-dontwarn org.jetbrains.annotations.**
