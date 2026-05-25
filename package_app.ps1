
# Set paths
$PROJECT_ROOT = $PSScriptRoot
$SRC_DIR = "$PROJECT_ROOT\src"
$LIB_DIR = "$PROJECT_ROOT\libs"
$BUILD_DIR = "$PROJECT_ROOT\build\classes"
$DIST_DIR = "$PROJECT_ROOT\dist"
$VERSION = "1.2.0"

# Clean build and dist
if (Test-Path $BUILD_DIR) { Remove-Item -Recurse -Force $BUILD_DIR }
if (Test-Path $DIST_DIR) { Remove-Item -Recurse -Force $DIST_DIR }
New-Item -ItemType Directory -Force -Path $BUILD_DIR | Out-Null
New-Item -ItemType Directory -Force -Path $DIST_DIR | Out-Null

# Construct classpath for compilation
$JFXRT = "$LIB_DIR\jfxrt.jar"
$LIBS = Get-ChildItem "$LIB_DIR\*.jar" | Where-Object { $_.Name -ne "jfxrt.jar" } | ForEach-Object { $_.FullName }
$CLASSPATH = "$JFXRT;" + ($LIBS -join ";") + ";$BUILD_DIR"

# Copy resources
Write-Host "Copying resources..."
Copy-Item "$SRC_DIR\*" -Destination $BUILD_DIR -Recurse -Force -Exclude "*.java"

# Compile
Write-Host "Compiling..."
$JAVA_FILES = Get-ChildItem -Path $SRC_DIR -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
$JAVA_FILES | Out-File "$PROJECT_ROOT\sources.txt" -Encoding ASCII
& javac -d "$BUILD_DIR" -cp "$CLASSPATH" "@$PROJECT_ROOT\sources.txt"

if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed!"
    exit 1
}

# Create Manifest
$MANIFEST_PATH = "$PROJECT_ROOT\manifest.mf"
$LIB_NAMES = Get-ChildItem "$LIB_DIR\*.jar" | ForEach-Object { "libs/" + $_.Name }

$WRAPPED_CLASS_PATH = ""
$CURRENT_LINE = "Class-Path: "
foreach ($item in $LIB_NAMES) {
    if (($CURRENT_LINE.Length + $item.Length + 1) -gt 70) {
        $WRAPPED_CLASS_PATH += $CURRENT_LINE + " `r`n"
        $CURRENT_LINE = " " + $item
    } else {
        if ($CURRENT_LINE -eq "Class-Path: ") {
            $CURRENT_LINE += $item
        } else {
            $CURRENT_LINE += " " + $item
        }
    }
}
$WRAPPED_CLASS_PATH += $CURRENT_LINE + "`r`n"

$MANIFEST_CONTENT = "Manifest-Version: 1.0`r`nMain-Class: library.assistant.ui.main.MainLauncher`r`n" + $WRAPPED_CLASS_PATH
$MANIFEST_CONTENT | Out-File $MANIFEST_PATH -Encoding ASCII

# Create JAR
Write-Host "Creating JAR..."
$JAR_NAME = "LibraryAssistant.jar"
& jar cmf "$MANIFEST_PATH" "$DIST_DIR\$JAR_NAME" -C "$BUILD_DIR" .

# Package into ZIP
Write-Host "Packaging into ZIP..."
$ZIP_NAME = "LibraryAssistant-v$VERSION.zip"
$ZIP_PATH = "$DIST_DIR\$ZIP_NAME"

# Create a temporary folder for zipping
$TEMP_ZIP_DIR = "$DIST_DIR\LibraryAssistant"
New-Item -ItemType Directory -Force -Path $TEMP_ZIP_DIR | Out-Null

# Copy JAR
Copy-Item "$DIST_DIR\$JAR_NAME" -Destination $TEMP_ZIP_DIR
New-Item -ItemType Directory -Force -Path "$TEMP_ZIP_DIR\libs" | Out-Null

# Copy ONLY non-JavaFX dependency JARs (and no DLLs!)
Get-ChildItem "$LIB_DIR\*.jar" | Where-Object { $_.Name -notlike "javafx*" } | ForEach-Object {
    Copy-Item $_.FullName -Destination "$TEMP_ZIP_DIR\libs"
}

# Zip standard distribution (contains main JAR and non-JavaFX libs)
Compress-Archive -Path "$TEMP_ZIP_DIR\*" -DestinationPath $ZIP_PATH -Force

# Verify and download JMODs if needed
$jmods_zip = "openjfx-jmods.zip"
$jmods_dir = "$PROJECT_ROOT\openjfx-jmods"
if (!(Test-Path $jmods_dir)) {
    Write-Host "Downloading JavaFX JMODs..."
    Invoke-WebRequest -Uri "https://download2.gluonhq.com/openjfx/17.0.10/openjfx-17.0.10_windows-x64_bin-jmods.zip" -OutFile "$PROJECT_ROOT\$jmods_zip"
    Write-Host "Extracting JavaFX JMODs..."
    Expand-Archive -Path "$PROJECT_ROOT\$jmods_zip" -DestinationPath $jmods_dir
    Remove-Item -Force "$PROJECT_ROOT\$jmods_zip"
}

# Create Native EXE App
Write-Host "Creating Native Windows EXE App..."
$APP_DIR = "$DIST_DIR\LibraryAssistant-Windows"
if (Test-Path $APP_DIR) { Remove-Item -Recurse -Force $APP_DIR }

& jpackage --input $TEMP_ZIP_DIR --dest $DIST_DIR --name "LibraryAssistant-Windows" --main-jar $JAR_NAME --main-class library.assistant.ui.main.MainLauncher --java-options "-Dprism.order=sw" --module-path "$jmods_dir\javafx-jmods-17.0.10" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media,javafx.web,javafx.swing,java.se,jdk.unsupported,jdk.charsets --type app-image

# Package Native App into ZIP
Write-Host "Packaging Native App into ZIP..."
$EXE_ZIP_NAME = "LibraryAssistant-v$VERSION-Windows-EXE.zip"
Compress-Archive -Path "$APP_DIR\*" -DestinationPath "$DIST_DIR\$EXE_ZIP_NAME" -Force

# Cleanup temp folders
Remove-Item -Recurse -Force $TEMP_ZIP_DIR
Remove-Item -Recurse -Force $APP_DIR

Write-Host "Build complete: $ZIP_PATH"
