# IOA Playground
Hey everyone! this is a project that I was asked to made for a course in my software engineering degree.
Hope you will find it useful, enjoy!

## Appendix Technologies
- Eclipse
- Java
- JavaFX
- Spring AOP
- Spring Core container
- Spring Contex
- Spring DAO
- Spring Web
- MongoDB
- Postman
- Jackson
- AspectJ
- Apache Common Logger

## Setup Documentation
1)	Download and install Eclipse (version 4.9.0 or above).
2)	Download and install Java JDK 8 and Java JRE 8.
3)	Download the zip file that contains the application's source code.
4)	Open new project in Eclipse and drag the source files you just download. 
Right click on the *new project* --> *Build Path* --> *Configure Build Path* --> *Source* --> Change the source folders to:

•	Src/main/java

•	Src/main/resources

•	Src/test/java

5)	Download Spring support files for Web servers, HTTP servers and IoC.
6)	Download Spring support files for MongoDB.
7)	Download Spring support files for Mail integration.
8)	Right click on the *new project* --> *Build Path* --> *Configure Build Path* --> *Libraries* --> Add all the external jar files you just download
9)	Enter to MongoDB web site and download the MSI installation file and run it.
10)	 Right click on the java file: *Application.java* --> *Run As* --> *Spring Boot App*
11)	 Right click on the java file: *ClientApplication.java* --> *Run As* --> *Java Application*

## Project Requirements Document
### Introduction
The system is a system that represents a playground that will include 2 types elements --> **Message Board** and **Carrousel**.  Also, each element will have actions that can only be performed on him.

### Purpose of System
The system is designed for children so they can enjoy the facilities of the garden and play whatever element they want. The main goal is for all the children to enjoy the playground.

### Vision of the system
The system vision is a kindergarten with 2 types of elements --> **Carrousel** and a **Message Board** (and in the future to add more elements), in which each child plays with the element he chooses.
While playing, the kids who use the system are getting points to their profile for every use of element. It doesn’t really mean something special, just another entertainment dimension to the user experience.   
The management of the kindergarten is responsible for adding elements, and update existing elements if needed.

## Actors and goals
![ioa1](https://i.imgur.com/gXzbVT1.png)

## Use Case Diagram
![ioa2](https://i.imgur.com/kDynfOT.png)

## Use Case Details
**Use Case:** Register

**Participating Actors:** Guest - Major, System Database, Email confirmation system

**Goal:** Create a profile in the platform.

**Preconditions:** User must connect into the internet.

**Postconditions:** The confirmation system informs the user that the account has been created.

![ioa3](https://i.imgur.com/8iv9iGq.png)

**Use Case:** Add Element

**Participating Actors:** Admin - Major, System Database 

**Goal:** Create new element.

**Preconditions:** User must be an Admin.

**Postconditions:** The system informs the Admin that the element has been added.

![ioa4](https://i.imgur.com/7QPbts9.png)

**Use Case:** Update Element

**Participating Actors:** Admin - Major, System Database 

**Goal:** Update existing element.

**Preconditions:** User must be an Admin.

**Postconditions:** The system informs the Admin that the element has been updated.

![ioa5](https://i.imgur.com/s6WF614.png)

**Use Case:** View Element

**Participating Actors:** Admin – Major, Member - Major, System Database 

**Goal:** View elements.

**Preconditions:** User must be an Admin or a Member.

**Postconditions:** The system displays the element information.

![ioa6](https://i.imgur.com/FylB1Qb.png)

**Use Case:** Active Element

**Participating Actors:** Member - Major, System Database 

**Goal:** Active elements.

**Preconditions:** User must be a Member.

**Postconditions:** The system updates the action log and the Member points.

![ioa7](https://i.imgur.com/vp3b0T4.png)

**Use Case:** Confirm registration

**Participating Actors:** Confirmation System – major, Guest

**Goal:** Confirm Guest registration.

**Preconditions:** User must be a Guest.

**Postconditions:** The Guest becomes a Member/Admin.

![ioa8](https://i.imgur.com/J1fL0PX.png)


## Client Application Screenshots
![client1](https://i.imgur.com/a5ED9bG.png)
![client2](https://i.imgur.com/GgG145t.png)
![client3](https://i.imgur.com/x8PSYdl.png)
![client4](https://i.imgur.com/dAKjQ7F.png)
![client5](https://i.imgur.com/kLvvw1Z.png)
![client6](https://i.imgur.com/y1AFh66.png)
![client7](https://i.imgur.com/Zd8qoKv.png)
![client8](https://i.imgur.com/Yv5H9H0.png)
