#!/bin/sh
apt-get update  # To get the latest package lists
apt-get install git default-jdk maven -y
git clone https://github.com/BenCoepp/probatio.git
cd probatio
mvn clean
mvn package
#etc.