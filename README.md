# Quick Repair

### Problem description

Suppose you are a dorm student and something is broken ( water flows from a pipe, bed is broken ), maybe you have to go to administrator and write down your problem on a notebook. Now, you have to wait for the handyman to come when he has time.

What if you have a mobile phone application and apply directly to the handyman? 
 * This is a faster solution and will be more convenient for everyone.
 * The student can make a request through server and all of the handymen will be notified
 * The handyman can see all of the request and schedule them at different hours

### System Topology

The application is build over a client-server architecture

#### Server

This is used to expose data to the users depending on their characteristics (role, building number)

#### Client

In this application there are 2 types of customers:

  * `Mobile app`
    * This is used by students and handymen to communicate
    * To use this app, the user have to log in with username and password
    * There are 2 types of users:
      * Student - will send requests through server and will be notified when its request has been approved or rejected
      * Handyman - will see the list of requests and will changed it's status (accepted, rejected). If the request is accepted, then he has to set a date for the visit.
      
  * `Web app`
    * This is used by system administrators
    * Functions:
      * Manage buildings
      * Manage dorm student accounts
  
### Technologies used

System | Technology
:---:|:---:
Server | .NET Core
Mobile APP | Kotlin
Web Server | React

**The Mobile App only supports Android**