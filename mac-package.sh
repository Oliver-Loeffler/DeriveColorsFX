export VERSION="1.0.0"
export INSTALL_DIR="target/mac-installer"
export MAIN_CLASS="net.raumzeitfalle.fx.derivecolors.App"
export MOD_PATH="target/modulepath"

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
--description "Creates a color palette using JavaFX Color::deriveColor methd. Will require screen acces in order to pick colors." \
--vendor "Raumzeitfalle.de" \
--verbose \
--runtime-image target/runtime \
--dest $INSTALL_DIR \
--type dmg \
--mac-package-identifier net.raumzeitfalle.fx.derivecolors \
--icon packaging/osx/DeriveColors.icns


