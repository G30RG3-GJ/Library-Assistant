# Run unit tests for Library Assistant in PowerShell

$LIB_DIR = "libs"
$TEST_LIB_DIR = "libs\test"
$STUBS_DIR = "build\test\stubs"
$CLASSES_DIR = "build\test\classes"

# Create directories
if (!(Test-Path $STUBS_DIR)) { New-Item -ItemType Directory -Force -Path $STUBS_DIR | Out-Null }
if (!(Test-Path $CLASSES_DIR)) { New-Item -ItemType Directory -Force -Path $CLASSES_DIR | Out-Null }

# Construct classpath
$JARS = @()
Get-ChildItem "$LIB_DIR\*.jar" | ForEach-Object { $JARS += $_.FullName }
Get-ChildItem "$TEST_LIB_DIR\*.jar" | ForEach-Object { $JARS += $_.FullName }
$JARS += (Get-Item $STUBS_DIR).FullName

$CLASSPATH = $JARS -join ";"

Write-Host "Compiling stubs..."
& javac -d $STUBS_DIR test\stubs\library\assistant\database\DatabaseHandler.java test\stubs\library\assistant\ui\listmember\MemberListController.java

if ($LASTEXITCODE -ne 0) {
    Write-Error "Stubs compilation failed"
    exit 1
}

Write-Host "Compiling tests and source files..."
& javac -cp "$CLASSPATH" -d $CLASSES_DIR src\library\assistant\data\model\Book.java src\library\assistant\data\model\MailServerInfo.java src\library\assistant\database\DataHelper.java test\library\assistant\database\DataHelperTest.java

if ($LASTEXITCODE -ne 0) {
    Write-Error "Tests compilation failed"
    exit 1
}

Write-Host "Running tests..."
$RUN_CLASSPATH = "$CLASSES_DIR;$CLASSPATH"
& java -cp "$RUN_CLASSPATH" org.junit.runner.JUnitCore library.assistant.database.DataHelperTest
