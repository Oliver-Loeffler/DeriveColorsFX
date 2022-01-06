@ECHO OFF
set VERSION=1.1.0
set INSTALL_DIR=target/win-installer
set MAIN_CLASS=net.raumzeitfalle.fx.derivecolors.App
set MOD_PATH=target\modulepath
for /F %%i in ('"%JAVA_HOME%\bin\jdeps" --module-path %MOD_PATH% --print-module-deps --ignore-missing-deps target/derive-colors-%VERSION%.jar') do SET JDEPS_MODULES=%%i

"%JAVA_HOME%\bin\jlink" --module-path %MOD_PATH% ^
--add-modules %JDEPS_MODULES% ^
--output target/runtime ^
--strip-debug ^
--compress 2 ^
--no-header-files ^
--no-man-pages

copy target\derive-colors-%VERSION%.jar target\modulepath

"%JAVA_HOME%\bin\jpackage" ^
--app-version %VERSION% ^
--input target\modulepath ^
--main-jar derive-colors-%VERSION%.jar ^
--main-class %MAIN_CLASS% ^
--name DeriveColorsFX ^
--description "Creates a color palette using JavaFX Color::deriveColor function." ^
--vendor "Raumzeitfalle.de" ^
--verbose ^
--runtime-image target/runtime ^
--dest %INSTALL_DIR% ^
--type msi ^
--java-options "--add-opens=javafx.fxml/javafx.fxml=ALL-UNNAMED" ^
--java-options "-Djava.library.path=runtime\bin;runtime\lib" ^
--win-dir-chooser ^
--win-menu ^
--win-per-user-install ^
--win-menu-group "DeriveColorsFX" ^
--win-shortcut ^
--icon packaging/windows/DeriveColors.ico
