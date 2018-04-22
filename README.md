# PhysicsAcademy (WIP)
An all in one physics program where once can gain an intuitive understanding of physical concepts through various simulations.

## Dependencies
* [Fisica Physics Library](http://www.ricardmarxer.com/fisica/) Physics Library (Simplified Wrapper for Box2D)
* All fonts/files used are already installed in the data folder

## Flowchart
![Diagram](https://github.com/QiLinXue/PhysicsAcademy/blob/master/flowchart/PhysicsAcademyFlowchart.png)
[Edit](https://www.draw.io/#HQiLinXue%2FPhysicsAcademy%2Fmaster%2Fflowchart%2FPhysicsAcademyFlowchart.png "Click me to edit the flowchart")

## Files
This is a relatively large processing project in terms of components. The global variable screenMode determines the screen the user currently sees. The main file "PhysicsAcademy.pde" acts as a hub where it combines all the major commands. At a glance, it should be easy telling the purpose.

### Screen Mode 0: HomeScreen_Mode.pde
This is the landing screen the user will first see, characterized by the color black in the flowchart. In this file, there is code for buttons that switch the screen mode (self explanatory). The major component of this screen is the code to register as a new user and log in as an existing user.

#### Data Folder
In the data folder, there's two key csv files: "userData.csv" and "empty.csv". "userData.csv" holds the important user information such as username and password (this is not secure at all but hey it's Processing). "empty.csv" is a blank template which is duplicated to create a new user csv file.

I chose to create a new csv file which holds user data for every single user. There are positives and negatives that come with this. First, by creating a new file for each single user, the data folder will get clustered up really really fast. I do not know if it will affect the efficiency, but for a small number of users there should be no problem. The positive side is that it is much easier to work with in the code in comparison to having everyone's data on one file. I can store an entire row in an array easily, and manipulate rows and columns without worrying about modifying anyone else's data.

#### Registering and Logging In
Registering and logging in is a fairly straightforward process. The only major concern was creating a user friendly interface to input the specified information without getting an error message. This is where the variable homeScreenTypeMode comes in, which basically tells the code which stage of the sign up/log in process the user is on.

After logging in or registering, the appropriate csv file is copied to a giant array. This will speed up run time drastically.

### Screen Mode 1: Simulation__Master.pde
Currently, this mode can only be accessed from the quiz mode screen. Depending on how this mode was initialized
