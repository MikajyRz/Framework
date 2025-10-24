@echo off
setlocal

set "JAVA_HOME=C:\Program Files\Java\jdk-17"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "CLASSPATH=lib\servlet-api.jar;lib\jakartaee-migration-1.0.8-shaded.jar"

echo Vérification des dépendances...
if not exist "lib\servlet-api.jar" (
    echo Erreur : servlet-api.jar manquant dans lib\
    exit /b 1
)
if not exist "lib\jakartaee-migration-1.0.8-shaded.jar" (
    echo Erreur : jakartaee-migration-1.0.8-shaded.jar manquant dans lib\
    exit /b 1
)
if not exist "src\main\java" (
    echo Erreur : dossier src\main\java introuvable
    exit /b 1
)

echo Création des dossiers build...
mkdir build 2>nul
mkdir build\classes 2>nul

echo Collecte des sources Java...
if exist build\_sources.txt del /f /q build\_sources.txt 2>nul
for /R "src\main\java" %%f in (*.java) do @echo %%f>> build\_sources.txt

rem Vérifier s'il y a des sources
findstr . build\_sources.txt >nul 2>&1
if errorlevel 1 (
    echo Erreur : aucun fichier .java trouvé dans src\main\java
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

echo Compilation de tous les fichiers .java...
javac -d build\classes -cp "%CLASSPATH%" @build\_sources.txt
if %ERRORLEVEL% neq 0 (
    echo Erreur : échec de la compilation
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

echo Création de build\framework.jar...
jar cvf "build\framework.jar" -C build\classes .
if %ERRORLEVEL% neq 0 (
    echo Erreur : échec de la création de build\framework.jar
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

echo Préparation du dossier test\lib et copie de framework.jar...
mkdir ..\test\lib 2>nul
copy /Y "build\framework.jar" "..\test\lib" >nul
if %ERRORLEVEL% neq 0 (
    echo Erreur : echec de la copie de framework.jar vers ..\test\lib
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

echo Assemblage du .war dans build\framework.war...
rmdir /s /q build\war 2>nul
mkdir build\war\WEB-INF\classes 2>nul
mkdir build\war\WEB-INF\lib 2>nul

echo Copie des classes dans WEB-INF\classes...
xcopy "build\classes\*" "build\war\WEB-INF\classes\" /E /I /Y >nul
if %ERRORLEVEL% neq 0 (
    echo Erreur : echec de la copie des classes vers build\war\WEB-INF\classes
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

echo Copie des bibliothèques dans WEB-INF\lib...
copy /Y "build\framework.jar" "build\war\WEB-INF\lib" >nul
copy /Y "lib\jakartaee-migration-1.0.8-shaded.jar" "build\war\WEB-INF\lib" >nul
copy /Y "lib\servlet-api.jar" "build\war\WEB-INF\lib" >nul

echo Création du fichier WAR...
jar cvf "build\framework.war" -C build\war .
if %ERRORLEVEL% neq 0 (
    echo Erreur : echec de la création du WAR
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

rem Nettoyage
del /f /q build\_sources.txt 2>nul

echo Compilation, création de framework.jar et build\framework.war terminées avec succès.
endlocal
```// filepath: d:\ITU\S5\Framework\compile.bat
@echo off
setlocal

set "JAVA_HOME=C:\Program Files\Java\jdk-17"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "CLASSPATH=lib\servlet-api.jar;lib\jakartaee-migration-1.0.8-shaded.jar"

echo Vérification des dépendances...
if not exist "lib\servlet-api.jar" (
    echo Erreur : servlet-api.jar manquant dans lib\
    exit /b 1
)
if not exist "lib\jakartaee-migration-1.0.8-shaded.jar" (
    echo Erreur : jakartaee-migration-1.0.8-shaded.jar manquant dans lib\
    exit /b 1
)
if not exist "src\main\java" (
    echo Erreur : dossier src\main\java introuvable
    exit /b 1
)

echo Création des dossiers build...
mkdir build 2>nul
mkdir build\classes 2>nul

echo Collecte des sources Java...
if exist build\_sources.txt del /f /q build\_sources.txt 2>nul
for /R "src\main\java" %%f in (*.java) do @echo %%f>> build\_sources.txt

rem Vérifier s'il y a des sources
findstr . build\_sources.txt >nul 2>&1
if errorlevel 1 (
    echo Erreur : aucun fichier .java trouvé dans src\main\java
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

echo Compilation de tous les fichiers .java...
javac -d build\classes -cp "%CLASSPATH%" @build\_sources.txt
if %ERRORLEVEL% neq 0 (
    echo Erreur : échec de la compilation
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

echo Création de build\framework.jar...
jar cvf "build\framework.jar" -C build\classes .
if %ERRORLEVEL% neq 0 (
    echo Erreur : échec de la création de build\framework.jar
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

echo Préparation du dossier test\lib et copie de framework.jar...
mkdir ..\test\lib 2>nul
copy /Y "build\framework.jar" "..\test\lib" >nul
if %ERRORLEVEL% neq 0 (
    echo Erreur : echec de la copie de framework.jar vers ..\test\lib
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

echo Assemblage du .war dans build\framework.war...
rmdir /s /q build\war 2>nul
mkdir build\war\WEB-INF\classes 2>nul
mkdir build\war\WEB-INF\lib 2>nul

echo Copie des classes dans WEB-INF\classes...
xcopy "build\classes\*" "build\war\WEB-INF\classes\" /E /I /Y >nul
if %ERRORLEVEL% neq 0 (
    echo Erreur : echec de la copie des classes vers build\war\WEB-INF\classes
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

echo Copie des bibliothèques dans WEB-INF\lib...
copy /Y "build\framework.jar" "build\war\WEB-INF\lib" >nul
copy /Y "lib\jakartaee-migration-1.0.8-shaded.jar" "build\war\WEB-INF\lib" >nul
copy /Y "lib\servlet-api.jar" "build\war\WEB-INF\lib" >nul

echo Création du fichier WAR...
jar cvf "build\framework.war" -C build\war .
if %ERRORLEVEL% neq 0 (
    echo Erreur : echec de la création du WAR
    del /f /q build\_sources.txt 2>nul
    exit /b 1
)

rem Nettoyage
del /f /q build\_sources.txt 2>nul

echo Compilation, création de framework.jar et build\framework.war terminées avec succès.
endlocal