set JAVA_HOME=C:\Program Files\Java\jdk-19
set PATH="C:\MavenBuild\apache-maven-3.9.6\bin";%PATH%
mvn install:install-file -Dfile=spread-4.4.0.jar  -DgroupId=org.spread -DartifactId=spread -Dversion=4.4.0 -Dpackaging=jar