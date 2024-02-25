This dir is a local Maven repository with all required local JARs installed. In case of any changes made to the JAR
files' contents, or addition of any new libraries, they need to be installed via running:

    mvn install:install-file -Dfile=JAR/symbols.jar
    -DgroupId=com.minicasino.graphics
    -DartifactId=symbols
    -Dpackaging=jar
    -Dversion=0.0.1
    -DlocalRepositoryPath=lib

The above is just a template. The groupId and artifactId should match the nature and the contents of a JAR to be
installed in the local repository.

The usage of the local repository is indicated in the pom.xml as:

    <repositories>
        <repository>
            <id>Local repository</id>
            <url>file://${basedir}/lib</url>
        </repository>
    </repositories>

Any new dependencies used in the project and coming from this repo must be also added to the pom.xml, e.g.:

        <dependency>
            <groupId>com.minicasino.graphics</groupId>
            <artifactId>symbols</artifactId>
            <version>0.0.1</version>
        </dependency>

Any JARs to be installed may be kept in ${basedir}/JAR.
