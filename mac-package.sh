mvn clean install

export VERSION="1.1.0"
export INSTALL_DIR="target/mac-installer"
export MAIN_CLASS="net.raumzeitfalle.fx.derivecolors.App"
export MOD_PATH="target/modulepath"
export RESOURCE_DIR="packaging/macos"

jdeps --module-path $MOD_PATH --print-module-deps --ignore-missing-deps target/derive-colors-$VERSION.jar

JDEPS_MODULES=$(jdeps --module-path $MOD_PATH --print-module-deps --ignore-missing-deps target/derive-colors-$VERSION.jar)

jlink --module-path $MOD_PATH \
--add-modules $JDEPS_MODULES \
--output target/runtime \
--strip-debug \
--compress 2 \
--no-header-files \
--no-man-pages

cp target/derive-colors-$VERSION.jar target/modulepath

jpackage --app-version $VERSION \
--input target/modulepath \
--main-jar derive-colors-$VERSION.jar \
--main-class $MAIN_CLASS \
--name DeriveColorsFX \
--description "Creates a color palette using JavaFX Color::deriveColor method. Will require screen access in order to pick colors." \
--vendor "Raumzeitfalle.de" \
--about-url "https://github.com/Oliver-Loeffler/DeriveColorsFX#-derivecolorsfx" \
--verbose \
--runtime-image target/runtime \
--icon packaging/macos/DeriveColors.icns \
--dest $INSTALL_DIR \
--type pkg \
--mac-package-identifier net.raumzeitfalle.fx.derivecolors \
--resource-dir $RESOURCE_DIR
