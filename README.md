# group-04 HealthRover
## Travic CI
[![Build Status](https://travis-ci.com/DIT112-V20/group-04.svg?branch=master)](https://travis-ci.com/DIT112-V20/group-04)
## What
The HealthRover is aimed towards a hospital environment and will deliver medicine/medical equipment to a desired location using manual control, without the need for human contact. The HealthRover will primarily be using voice control and will be functional inside a hospital where there is Wifi connectivity.

The solution consists of an android application for interfacing with the user and a smart-car for accomplishing the physical task of delivering goods.

## Why
It will minimize the amount of human contact amidst a pandemic and reduce the risk of spreading contagious diseases. It will help medical professionals in and out of hospitals by improving the efficiency of the medical workflow by automating medicine and equipment distribution. 

## How
Healthcare professionals will have the opportunity to provide patients with the needed medication or medical equipment by the use of a Smartcar along with a smartphone application. A simple graphical user interface will guide the end-user through the process of the delivery, which is going to be primarily led by voice controlling the car through the app. It will provide features such as voice recognition, access through Wi-Fi as well as capabilities that support the manual navigation of the car, such as obstacle avoidance.

For simplicity and security, the solution is developed to have the SmartCar and the application connect to the same wi-fi network. Optionally, the customer can change the networking configurations in order to adapt the connectivity between the SmartCar and the application to their needs.

The android application is developed using an object oriented approach by applying proven design and architecture patterns as well as early and continuous testing to ensure the robustness and high quality of the application. 

## Technical Overview

### Hardware used:
- Arduino
- [Smartcar platform](https://www.hackster.io/platisd/getting-started-with-the-smartcar-platform-1648ad) based on the ESP32 microcontroller
- Wi-Fi connectivity (integrated in the ESP32 SoC microcontroller)
- 3D-printer

 
### Software:
- Android Studio
- [TeamHub plugin](https://teamhub.dev/)
- Java
- Gradle
- Travis CI
- [SmartCar API](https://platisd.github.io/smartcar_shield/)
- Arduino IDE
- GitHub
- [Virtual Joystick Android library](https://github.com/controlwear/virtual-joystick-android)
- [DialogFlow API](https://dialogflow.com/)
- [Dexmaker Mockito](https://mvnrepository.com/artifact/com.google.dexmaker/dexmaker-mockito/1.2)

 
### Team Overview
- Ella Alklid - guslarcaan@student.gu.se - Developer/Coordinator
- Sandra Smoler Eisenberg - gussmosa@student.gu.se - Developer/Administrator
- Samuel Gunnarsson - gusgunsah@student.gu.se - Developer/HR-manager/Sponsor 
- Chrysostomos Tsagkidis - gustsach@student.gu.se - Developer
- Eemil Jeskanen - gusjesee@student.gu.se - Developer
- Krasen Parvanov - gusparkr@student.gu.se - Developer/Tester
