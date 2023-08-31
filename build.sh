#!/bin/bash

# Pre-requisite:
# Pre-fill lib with jars accordingly to README.md or from http://aigents.com/download/latest/

rm Aigents.jar
mkdir bin
cp -r resources/* bin
cp -r lib/* bin
cd src/main/java
# For Java 9+ (can not load jar in jar)
#javac -cp ".:./../../../lib/*" -d ./../../../bin -target 1.7 -source 1.7 $(find ./net/* | grep .java)
# For Java 6-8
# javac -cp ".:./../../../lib/*" --module-path ".:./../../../lib/javafx-sdk-20.0.2/lib" -d ./../../../bin -target 1.6 -source 1.6 -Xlint:deprecation $(find ./net/* | grep .java)
# javac -cp ".:./../../../lib/*" -d ./../../../bin -target 1.6 -source 1.6 -Xlint:deprecation $(find ./net/* | grep .java)
# javac -cp ".:./../../../lib/*" -sourcepath ".:./../../../javafx-sdk-11.0.2/src/javafx.base:./../../../javafx-sdk-11.0.2/src/javafx.controls:./../../../javafx-sdk-11.0.2/src/javafx.fxml:./../../../javafx-sdk-11.0.2/src/javafx.graphics:./../../../javafx-sdk-11.0.2/src/javafx.media:./../../../javafx-sdk-11.0.2/src/javafx.swing:./../../../javafx-sdk-11.0.2/src/javafx.web" -d ./../../../bin -Xlint:deprecation $(find ./net/* | grep .java)
# javac -cp ".:./../../../lib/*" -d ./../../../bin -target 1.6 -source 1.6 -Xlint:deprecation $(find ./net/* | grep .java)
javac -cp ".:./../../../lib/*" -d ./../../../bin -target 1.7 -source 7 -Xlint:deprecation $(find ./net/* | grep .java) $(find ./org/* | grep .java)
cd ./../../../bin
jar cvfm ../Aigents.jar manifest.mf *
cd ..
