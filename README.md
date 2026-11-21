# Closet Virtual

App Android (Kotlin + Jetpack Compose) para guardar tu ropa y armar outfits automáticamente, al estilo de Whering.

## Estado actual (MVP)
- [x] Agregar prendas desde la galería (Photo Picker)
- [x] Guardado local con Room
- [x] Color dominante automático (Palette)
- [x] Cuadrícula del closet con filtros por categoría
- [ ] Generador de outfits (superior + inferior + calzado)
- [ ] Remoción de fondo (ML Kit Subject Segmentation)
- [ ] Outfits favoritos y planner
- [ ] Sugerencias según el clima

## Stack
Kotlin · Jetpack Compose (Material 3) · Room · Navigation Compose · Coil · Palette

## Compilar
**Android Studio:** abre la carpeta del proyecto y ejecuta la app en un emulador o celular.

**GitHub Actions:** cada push a `main` compila el APK. Entra a la pestaña **Actions**, abre la última ejecución y descarga el artefacto `closet-virtual-debug`.

## Subir a GitHub
```bash
git init
git add .
git commit -m "Esqueleto del closet virtual"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/closet-virtual.git
git push -u origin main
```

## Nota sobre Gradle
El repositorio no incluye el *Gradle Wrapper* (`gradlew`). La compilación en GitHub Actions no lo necesita. Para generarlo en tu PC (con Gradle instalado):
```bash
gradle wrapper --gradle-version 8.10.2
```
