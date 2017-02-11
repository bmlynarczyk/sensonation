# Sensonation

### Description

Sensonation it is an open hardware/software project for home blinds automation.
It's using raspberry pi as management unit, electronics stuff and dc engines for moving up and down blinds.
Main goal of this project is to make opening and closing home blind as automated as it's possible.

### Software Architecture

Sensonation is a Java process run in [Raspbian](https://www.raspbian.org/). 
Software is build with [Spring framework](https://spring.io/) using elements such as:

- Spring Boot
- Spring MVC
 
Java process is run as web application server in embedded servlet container [Tomcat](http://tomcat.apache.org/). Communication with User is over [HTTP](https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol) API.

Blinds maagement is possible thanks of library [Pi4j](https://github.com/Pi4J/pi4j/). Communiction between Java process and hardware is over [I2C bus](https://en.wikipedia.org/wiki/I%C2%B2C)

Blinds movement automation is based on calculation daylaigth changes in manner of localisation by [SunCalc-Java](https://github.com/florianmski/SunCalc-Java).

### Hardware Architecture

From hardware perpective Sensonation works on [raspberry pi model b](https://www.raspberrypi.org/products/model-b/). Of course run on higher versionion is also possible.

DC engines are controlled by [DRV8833 Dual Motor Driver Carrier](https://www.pololu.com/product/2130).

For leaving more free inputs/outputs on Raspberry Pi Sensonation uses [MCP23017 expander](http://ww1.microchip.com/downloads/en/DeviceDoc/21952b.pdf)

### Build tutorial

todo

### Manual

todo