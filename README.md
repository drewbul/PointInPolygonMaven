# Point in Polygon

The project illustrates the [algorithm described by W. Randolph Franklin](https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html).

See [this article](https://a-t-engineering.com/en/how-a-heatpump-task-grew-into-math-game-in-java/) for more details.

## Getting Started

[PointInPolygonJAR.zip](https://github.com/drewbul/PointInPolygonMaven/raw/master/PointInPolygonJAR.zip) - download and unpack this archive to some folder on your computer and you can run the game by starting start.bat (for Windows) or start.sh (for Linux).

## Prerequisites

The project was developed using Java SE 8 and Java FX, so you will need the respective Java Runtime Engine. It is possible you already have it installed since Java can be used for many other programs. If not, you can install it from here:

[https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) (registration required)

You can run this program under Windows, Linux and MacOS. You can record your voice and replace the WAV files. You can also replace MP3 files and edit the input parameter in the start.bat file, but the appropriate codecs must be installed in your operating system (in Ubuntu 18, by default they were missing).

## Source code
You can change certain parameters like the color, size and position of elements. That kind of GUI changes can be done without recompiling the source code, by modifying the file Main.fxml inside the JAR archive.

If you want to change the source code beyond the GUI parameters, you need to install JDK 8, install Maven, change directory to project root where file pom.xml is located and rebuild the JAR file using the following Maven command:
```shell script
mvn install
```
Java FX was removed from JDK starting with version 11, so if you want to use JDK 11 or a later version then you will need to uncomment the lines in pom.xml.

## Authors

* Andrew Buldyzhov

I am still a newbie in Java, so all suggestions and comments are welcome.

## License

Open source.

## Acknowledgments

* Thanks to Vyacheslav Chegrinets and Alex Kovalchuk for criticism and recommendations.      
