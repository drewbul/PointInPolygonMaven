#!/bin/sh

sudo apt install openjfx

# Sources may not be installed in the above method, so you can also install OpenJFX manually by uncomment the following lines:

#wget https://download2.gluonhq.com/openjfx/20.0.2/openjfx-20.0.2_linux-x64_bin-sdk.zip -O ~/Downloads/openjfx-sdk.zip
#sudo unzip ~/Downloads/openjfx-sdk.zip -d /usr/share/
#rm ~/Downloads/openjfx-sdk.zip
#sudo mv /usr/share/javafx-sdk-20.0.2/ /usr/share/openjfx/


echo '
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/bin/java
PATH_TO_FX=/usr/share/openjfx/lib
' | sudo tee -a /etc/environment

source /etc/environment
