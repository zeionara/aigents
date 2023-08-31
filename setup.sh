#!/bin/bash

sudo apt install openjdk-8-jdk-headless

mkdir lib

pushd lib
 
pull () {
    name="$(basename $1)"
    curl -O "$1"
    unzip "$name"
    rm "$name"
}

pull http://www.java2s.com/Code/JarDownload/javax.mail/javax.mail-api-1.4.7.jar.zip
pull http://www.java2s.com/Code/JarDownload/servlet/servlet-2.5.jar.zip
pull http://www.java2s.com/Code/JarDownload/javax.json/javax.json-1.0.2.jar.zip

# Ancient version of java is required - even 11th version of open javafx doesn't work

# pull https://download2.gluonhq.com/openjfx/20.0.2/openjfx-20.0.2_linux-x64_bin-sdk.zip
# mv javafx-sdk-20.0.2/lib/*.jar ./
# rm -rf javafx-sdk-20.0.2

curl -O https://dlcdn.apache.org/pdfbox/2.0.29/pdfbox-app-2.0.29.jar

pushd /tmp

git clone https://github.com/raisercostin/eclipse-jarinjarloader.git

eval "mv eclipse-jarinjarloader/eclipse-jarinjarloader/src/main/java/org $(dirs | cut -d ' ' -f 2)"

rm -rf eclipse-jarinjarloader

popd # move from /tmp to lib

popd # move from lib to the root repo folder
