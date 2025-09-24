@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%
set CLASSPATH=lib\servlet-api.jar;lib\jakartaee-migration-1.0.8-shaded.jar

echo Vérification des dépendances...
if not exist "lib\servlet-api.jar" (
    echo Erreur : servlet-api.jar manquant dans lib\
    exit /b 1
)
if not exist "lib\jakartaee-migration-1.0.8-shaded.jar" (
    echo Erreur : jakartaee-migration-1.0.8-shaded.jar manquant dans lib\
    exit /b 1
)
if not exist "src\main\java\framework\FrontServlet.java" (
    echo Erreur : FrontServlet.java manquant dans src\main\java\framework\
    exit /b 1
)

echo Création du dossier build\classes...
mkdir build\classes 2>nul

echo Compilation de FrontServlet.java...
javac -d build\classes src\main\java\framework\FrontServlet.java
if %ERRORLEVEL% neq 0 (
    echo Erreur : Échec de la compilation de FrontServlet.java
    exit /b 1
)

echo Vérification des fichiers .class...
if not exist "build\classes\main\java\framework\FrontServlet.class" (
    echo Erreur : FrontServlet.class n'a pas été généré
    exit /b 1
)

echo Création de FrontServlet.jar...
cd build\classes
jar cvf FrontServlet.jar main\java\framework\*.class
if %ERRORLEVEL% neq 0 (
    echo Erreur : Échec de la création de FrontServlet.jar
    cd ..\..
    exit /b 1
)
cd ..\..

echo Copie de FrontServlet.jar et des bibliothèques vers test\lib...
mkdir ..\test\lib 2>nul
copy build\classes\FrontServlet.jar ..\test\lib
if %ERRORLEVEL% neq 0 (
    echo Erreur : Échec de la copie de FrontServlet.jar vers test\lib
    exit /b 1
)
copy lib\jakartaee-migration-1.0.8-shaded.jar ..\test\lib
copy lib\servlet-api.jar ..\test\lib

echo Compilation et copie terminées avec succès.