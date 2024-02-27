# Mini Casino

A small, 3-reel classic slots game for PC developed with JavaFX.

## :ledger: Index

- [About](#beginner-about)
- [Technologies used](bulb-technologies-used)
- [Features](#star2-features)
- [Usage](#zap-usage)
  - [Requirements](#electric_plug-requirements)
- [Development](#wrench-development)
  - [Planned features](#bulb-planned-features)
  - [Pre-Requisites](#pencil-pre-requisites)
  - [Installing JARs](#nut_and_bolt-installing-jars)
- [Resources](#page_facing_up-resources)
- [Gallery](#camera-gallery)
- [Credit/Acknowledgment](#pray-creditacknowledgment)
- [License](#lock-license)

## :beginner: About
The base game features a standard 3-reel slots with 10 symbols in play. Currently there's also a debugging option available for toggling between 5 and 10 symbols for the ease of testing. The game has a functional GUI and a profile management system implemented, which allows the user to create different in-game profiles for keeping separate balance records. All profile data is stored externally in a dedicated directory as an XML file.

## :bulb: Technologies used
- JavaFX (GUI)
- Java SE 19 (game logic)
- JDOM (exporting/importing profile data)

## :star2: Features
- base 3-reel mode with a single payline in the middle
- 10 or 5 symbols with different payout multipliers
- nudge - if one or two reels are a single move away from producing a winning combination they will get shifted up or down (the game always picks the best possible outcome)
- in-game profile management system for a separate balance and keeping track of the highest payouts for each profile

<p align="center">
  <img src="/docs/media/preview_1.png" width="75%" height="75%" align="center">
</p>

## :zap: Usage
To play the game just download the latest [release](https://github.com/RobertR-01/mini-casino/releases), unzip it and run the `.jar` file.

#### :electric_plug: Requirements
- Windows OS
- Java 19+

## :wrench: Development
The game is still under development. The project's source code the can be freely cloned, inspected and modified using an IDE of choice. 

#### :bulb: Planned features
- turbo mode (already partially implemented) - a mode in which all 3 reels start and stop spiining simultaneously to speed up the gameplay
- wild symbols - counts as every other symbol when determining a viable payline
- free spins - getting a payline consisting of 3 free-spin symbols allows the player to perform a number of cost-free spins
- reworking the RNG aspect of the game's core logic to better fit the digital slots standards

#### :pencil: Pre-Requisites
- JDK 19 or higher
- IntelliJ IDEA or any other IDE with Maven support

#### :nut_and_bolt: Installing JARs
The game uses some graphics stored as an external JAR archive. Due to the nature of the Maven Shade plugin for creating fat JARs and how JavaFX accesses graphic files, JARs containing these resources are installed as a local Maven repository and added to the pom.xml as a dependency. This process should be done in case of any modifications to the current resources or any new JARs being added.
Currently the JARs which need to be installed are kept in the `mini-casino/JAR` directory, and the local Maven repositories are installed in `mini-casino/lib`.

To install a JAR as a local Maven repository use this Maven goal template:

    mvn install:install-file
    -Dfile=JAR/symbols.jar
    -DgroupId=com.minicasino.graphics
    -DartifactId=slots_graphics
    -Dpackaging=jar -Dversion=1.0.0
    -DlocalRepositoryPath=lib

The usage of the local repository is indicated in the pom.xml as follows:

    <repositories>
      <repository>
        <id>Local repository</id>
        <url>file://${basedir}/lib</url>
      </repository>
    </repositories>


Any new dependency used in the project and coming from this repository must be also added to the pom.xml, e.g.:

    <dependency>
      <groupId>com.minicasino.graphics</groupId>
      <artifactId>slots_graphics</artifactId>
      <version>1.0.0</version>
    </dependency>

## :page_facing_up: Resources
Add important resources here

## :camera: Gallery

<div align="center">
  <img src="/docs/media/preview_3.png" width="75%" height="75%" align="center">
  <p align="center">
    <i>Main menu</i>
  </p>
</div>

<div align="center">
  <img src="/docs/media/preview_2.png" width="75%" height="75%" align="center">
  <p align="center">
    <i>Slots game window (half of the symbols disabled)</i>
  </p>
</div>

<div align="center">
  <img src="/docs/media/profile_preview_1.png" width="75%" height="75%" align="center">
  <p align="center">
    <i>In-game profile selection window</i>
  </p>
</div>

<div align="center">
  <img src="/docs/media/profile_preview_2.png" width="35%" height="35%" align="center">
  <p align="center">
    <i>Editing the selected profile</i>
  </p>
</div>

<div align="center">
  <img src="/docs/media/nudge_preview.png" width="60%" height="60%" align="center">
  <p align="center">
    <i>Nudge feature info window</i>
  </p>
</div>

## :pray: Credit/Acknowledgment
Graphics used for this application:

| Graphics pack                      |          Author          | Link                                         |
|------------------------------------|:------------------------:|----------------------------------------------|
| food icon set                      | Reda                     | https://freeicons.io/icon-list/food-icon-set |
| Icon Pack: Fruit \| Flat           | amonrat rungreangfangsai | https://www.flaticon.com/packs/fruit-80      |
| Icon Pack: Casino \| Outline Color | Backwoods                | https://www.flaticon.com/packs/casino-144    |
| Icon Pack: Casino \| Flat          | Freepik                  | https://www.flaticon.com/packs/casino-243    |

## :lock: License
Add a license here (or a link to it).
