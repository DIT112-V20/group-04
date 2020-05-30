# Group-04 HealthRover  [![Build Status](https://travis-ci.com/DIT112-V20/group-04.svg?branch=master)](https://travis-ci.com/DIT112-V20/group-04)      [![codecov](https://codecov.io/gh/DIT112-V20/group-04/branch/master/graph/badge.svg)](https://codecov.io/gh/DIT112-V20/group-04) <img align="left" src="https://github.com/DIT112-V20/group-04/blob/master/app/src/main/ic_launcher-playstore.png" width="50" height="50"/>

## What
The HealthRover is aimed towards a hospital environment and will deliver medicine/medical equipment to a desired location using manual and voice control, without the need for human contact. The HealthRover uses natural language correlation in order to simplify the use of voice control for staff and is functional where there is Wi-Fi connectivity.

The solution consists of an android application for interfacing with the user and a [SmartCar](https://www.hackster.io/platisd/getting-started-with-the-smartcar-platform-1648ad) for accomplishing the physical task of delivering goods.

## Why
It will minimize the amount of human contact amidst a pandemic and reduce the risk of spreading contagious diseases. It will help medical professionals in and out of hospitals by improving the efficiency of the medical workflow, by assisting with safe distribution of medicine and equipment.

## How
Healthcare professionals will have the opportunity to provide patients with the needed medication or medical equipment by the use of a SmartCar along with a smartphone application. A simple graphical user interface will guide the end-user through the different features of the HealthRover application. It provides features such as voice recognition using a natural language processing engine, access through Wi-Fi as well as capabilities that support the navigation of the car, such as obstacle avoidance.

For simplicity and security, the solution is developed to have the SmartCar and the application connect to the same Wi-Fi network. Optionally, the customer can change the networking configurations in order to adapt the connectivity between the SmartCar and the application to their needs.

The android application is developed using an object oriented approach by applying proven design and architecture patterns, early and continuous testing, as well as continuous integration with automated tests to ensure the robustness and high quality of the application.

## Feature overview 

#### [Basic Obstacle Avoidance](https://github.com/DIT112-V20/group-04/wiki/Basic-Obstacle-Avoidance)
The Basic Obstacle Avoidance is implemented with the purpose of helping the user avoid obstacles in the front, while driving the SmartCar. The SmartCar will stop when an obstacle is present at 30cm and the user is then able to steer away from the obstacle or find an alternative route.

#### [Wifi Connectivity](https://github.com/DIT112-V20/group-04/wiki/Wifi-Connectivity)
The purpose of the Wifi Connectivity feature is to let the user connect to a SmartCar over Wi-Fi. When a connection is established the application can send commands to the SmartCar and receive output.

#### [Manual Maneuvering](https://github.com/DIT112-V20/group-04/wiki/Manual-Maneuvering)
This feature assures that the SmartCar can be manually controlled by the user. The car is able to move in different directions with variations in angle and velocity.

#### [Voice Control](https://github.com/DIT112-V20/group-04/wiki/Voice-Control) using [Speech Recognition](https://github.com/DIT112-V20/group-04/wiki/Speech-Recognition)
The Voice Control feature is enhanced by a Natural Language Processing Engine in order to allow the user to specify any turning maneuver for touch-free interaction with the SmartCar. The intent is to make it possible to perform the same type of steering done in [Manual Maneuvering](https://github.com/DIT112-V20/group-04/wiki/Manual-Maneuvering), but using voice. 

#### [Android Application and User Interface](https://github.com/DIT112-V20/group-04/wiki/Android-Application-and-User-Interface)
The Android Application and Graphical user interface feature is implemented in order to provide a user-friendly way for the user to interact with the system. The application also allows for the user to customize some parts of the system in order to further adapt the system to each customer's use case.

#### [Feedback](https://github.com/DIT112-V20/group-04/wiki/Feedback)
This feature provides feedback from the application to the user when connecting to the SmartCar or when an obstacle is detected to warn the user. The intent is to make events clear to the end-user, without having to view the screen.

#### [Continuous Integration & Quality Assurance](https://github.com/DIT112-V20/group-04/wiki/Continuous-Integration-and-Quality-Assurance)
The project implements Continuous Integration by utilizing the Travis CI framework.This assures the high quality of the product.

## Technical Overview

### Hardware:

The HealthRover SmartCar is designed by using the following components:

- [Smartcar platform](https://www.hackster.io/platisd/smartcar-gets-an-esp32-upgrade-bcbeb1) based on the ESP32 microcontroller
- Wi-Fi connectivity (integrated in the ESP32 SoC microcontroller)
- 1 x Ultrasonic Sensor - HC-SR04 (Generic)
- 3D-printed shell (find models in group-4/model/, g-code sliced for Prusa i3 MK3S)

Assembling the car is done by attaching the ultrasonic sensor to the front of the car and adding the 3D shell on top. Adding the shell still allows the user to connect to the car with USB as well as turning the vehicle on and off. The car runs with a set of 8 AA batteries(found below the ESP-32). 

### Software:

The following software libraries, API and external service was used during for the development of the HealthRover android application, the SmartCar sketch and surrounding features:

- Java
- Gradle
- [Travis CI](https://travis-ci.com/github/DIT112-V20/group-04)
- [SmartCar API](https://platisd.github.io/smartcar_shield/)
- [Virtual Joystick Android library](https://github.com/controlwear/virtual-joystick-android)
- [DialogFlow API v2](https://dialogflow.com/)
- [Codecov](https://codecov.io/gh/DIT112-V20/group-04/)
- [OkHttp 4.5.0](https://square.github.io/okhttp/)
- [JUnit 4.13](https://github.com/junit-team/junit4/blob/master/doc/ReleaseNotes4.13.md)
- [Espresso 3.2.0](https://developer.android.com/training/testing/espresso)
- [Dexmaker Mockito](https://mvnrepository.com/artifact/com.google.dexmaker/dexmaker-mockito/1.2)
- [Jacoco Gradle plugin](https://github.com/arturdm/jacoco-android-gradle-plugin)
- [SQLite](https://www.sqlite.org/index.html)
- [Autodesk Fusion 360 (Education License)](https://www.autodesk.com/products/fusion-360/overview) 
- [PrusSlicer 2.2.0](https://www.prusa3d.com/prusaslicer/)



#### Architecture and design

The android application was designed by using object oriented principles and design patterns such as simple factory pattern and the Model-view-controller. During the development process the team worked to stay consistent with the SOLID principles. The Graphical User Interface (View) was designed by using XML files and they are updated by the corresponding controllers which are responsible for the communication between the view layer and the main business logic. In addition to that the business logic interacts with the model part of the system which is represented with plain objects. The persistent storage is implemented by using a local SQLite database. The communication between the SmartCar and the Android application is performed by HTTP over TCP protocol on a single Wi-Fi connection. The SmartCar runs a web server which receives the commands from the application and responds accordingly upon the received requests. The logic that is implemented in a single file for simplicity and all the logic is encapsulated into methods. 

### Testing Summary 

In order to assure the high quality of the system, it was tested continuously both manually and with the help of automated tests. The automated tests were then integrated into an Continuous Integration framework based on Travis-CI and with automated reports from Codecov. Based on those reports we can verify the functionality of the system features described above. 

### Known issues:
- Ultrasonic sensor produce invalid readings sometimes that affect the performance of the car.
- Wi-Fi connectivity quality highly depends on the stability of the network that it is connected to, which affects the overall functionality of the application
- Sometimes some unpredicted results occur while using Speech Recognition, after building an apk and using the application on a smartphone. Possible reasons that affect this behaviour might be the smartphone’s speech recognition settings, smartphone’s microphone performance and surround audio capturing, as well as the dependency on the training of DialogFlow's agent being used.


## Set-up and User Manual 

### Set-up

In order to set-up and run the application and the SmartCar you will need to clone the repository and use:
Android Studio
Update the Gradle build and synchronize the project
Add your own DialogFlow access key credentials in the following file: app/src/main/res/raw/dialogflow_access_key.json
Import the DialogFlow settings provided with the following .zip file (which includes the used entities and intents): app/src/dialogFlow/DialogFlow.zip

Build the APK, install it on an Android smartphone (version >= 5.0) and connect to the same Wi-Fi network as the one specified in the Arduino sketch and then run the application.

Arduino
Open the sketch from: arduino/healthRover in Arduino and add your network SSID and Password
Import Smartcar shield, VL53L0X, ServoESP32 and ESP32 AnalogWrite libraries.
Compile on DEV KIT ESP 32 board 
Connect your SmartCar to ESP32 by USB and upload the sketch
Turn on the car 

### User Manual


<img align="left" src="https://github.com/DIT112-V20/group-04/blob/master/misc/healthrover_showcase.gif" width="360" height="720"/>

After completing the set-up steps from above the user can use the application features

#### Landing screen
The user can choose to edit the name of the car by clicking on the edit button and entering the new name and pressing the save button.    
The user can select the SmartCar to use from the list by clicking on it.
Once selected the user can connect to the car by pressing the “Connect” button,

#### Manual Control screen
On the manual control screen the user can control the SmartCar by using the joystick button. The user can steer in different directions by dragging the joystick button to the left, right, up, or down. The user may increase the speed by dragging the joystick button further towards the outer circle and vice versa.
The phone back button on the manual screen takes the user to the landing screen
The voice control button takes the user to the voice control screen.

#### Voice Control screen
On the voice control screen the user can press the microphone button in the middle  to start speaking commands in English to the application in order to control. 
The user can click on the help button(marked with “?”) which will show a pop-up describing how the user can use the voice control to control the car.
The phone back button will return the user to the manual control screen.
The manual control button takes the user to the manual control screen.

<br />
<br />

## Team Overview
- [Ella Alklid](https://github.com/Enilo) - guslarcaan@student.gu.se
- [Sandra Smoler Eisenberg](https://github.com/SandraSmolerEisenberg) - gussmosa@student.gu.se
- [Samuel Gunnarsson](https://github.com/samgun-6) - gusgunsah@student.gu.se
- [Chrysostomos Tsagkidis](https://github.com/chrytsa) - gustsach@student.gu.se 
- [Eemil Jeskanen](https://github.com/eemilj) - gusjesee@student.gu.se
- [Krasen Parvanov](https://github.com/krasen86) - gusparkr@student.gu.se 

