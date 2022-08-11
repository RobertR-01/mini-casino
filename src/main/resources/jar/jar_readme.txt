These JARs need to be installed  into a local Maven repository and added to pom.xml as dependencies.

Example:
1. installing JAR in local maven repo:
mvn install:install-file -Dfile=C:\...\mini-casino\src\main\resources\jar\graphics.jar -DgroupId=com.minicasino -DartifactId=local-graphics -Dversion=1.0 -Dpackaging=jar

2. dependency for pom.xml:
<dependency>
    <groupId>com.minicasino</groupId>
    <artifactId>local-graphics</artifactId>
    <version>1.0</version>
</dependency>
