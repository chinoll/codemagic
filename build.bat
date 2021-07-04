call gradlew.bat build
rm /s/q build_jar_tmp
mkdir build_jar_tmp
copy luaj-jse-3.0.2.jar build_jar_tmp
copy build/libs/codemagic-0.0.1.jar build_jar_tmp
cd build_jar_tmp
jar xvf codemagic-0.0.1.jar
jar xvf luaj-jse-3.0.2.jar
rm /q *.jar
jar cvfM codemagic.jar .