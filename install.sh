#!/bin/sh
apt-get update  # To get the latest package lists
apt-get install git default-jdk maven -y
wget https://download.oracle.com/java/19/latest/jdk-19_linux-x64_bin.deb
apt-get -qqy install ./jdk-19_linux-x64_bin.deb
update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-19/bin/java 1919
git clone https://github.com/BenCoepp/probatio.git
cd probatio
mvn clean
mvn package
echo "export PATH=$PATH:/home/dev/Dev/Projects/probatio/target" >> ~/.bashrc
echo "alias probatio='java -jar /home/dev/Dev/Projects/probatio/target/probatio-1.0-SNAPSHOT.jar'" >> ~/.bashrc
#etc.