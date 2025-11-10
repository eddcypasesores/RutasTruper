# Icono de la aplicación

Para que Android utilice una imagen personalizada (`icono.png`) como icono de la aplicación debes copiarla en las carpetas `mipmap` dentro de `app/src/main/res`.

```
app/
  src/
    main/
      res/
        mipmap-mdpi/
        mipmap-hdpi/
        mipmap-xhdpi/
        mipmap-xxhdpi/
        mipmap-xxxhdpi/
```

1. Crea cada carpeta `mipmap-*` si aún no existe.
2. Genera versiones redimensionadas de la imagen para cada densidad y guárdalas con el mismo nombre `icono.png`.
3. Sustituye la referencia del icono en el `AndroidManifest.xml` o en el tema si es necesario.

Android buscará automáticamente el archivo correspondiente a la densidad del dispositivo, por lo que es importante mantener el mismo nombre en todas las carpetas.
