
# Overview

#### This is an application in Kotlin which is created with a landing page contains 2 bottom tabs to allow the user to do the following

**Step 1 - Find Cities Weather**

Search by multiple city names (comma Separated) and get list of data weater for each city with limited number of cities per request
(minimum 3 cities and maximum 7 cities)

**Step 2 - Current City Forecast**

Get current location using google location services and view a list of data weather forecast for 5 days 3 hours.

- First time entering the screen, the app will ask the user for permission to access the location.

- Allow access to the location, the app will fetch longitude and latitude of the current location.

- Denied access to the location, the app will show an alert message that the permission is denied and the user has the option to open the mobile settings and allow access to the location.


 #### The following data should be displayed in both screens
 
- Temperature (Min and Max)

- Weather (description)

- Wind Speed  


#### Additional data

- Weather Icon

- Date for current weather to be displayed every 3 hours


# App Architecture and Libraries

1- Material Design

2- Android Architecture Components (MVVM, LiveData, Navigation)

3- RxJava Observables

4- Retrofit for Networking

5- Picasso for rendering images

6- Google Play Services for location detection

7- Google Truth for more readable test assertions and failure messages

8- Mockito for testing APIs tests cleanly & simply

9- KotlinTestRunner (JUnit4TestRunner) to remove final from classes and methods, especially needed in kotlin projects

# Screenshots

**Step 1 - Find Cities Weather**

![Step1](https://user-images.githubusercontent.com/6572487/76679500-85d85c80-65fa-11ea-9be2-ba81f6a18201.jpg)




**Step 2 - Current City Forecast**

![Step2](https://user-images.githubusercontent.com/6572487/76700651-14b8a800-66d3-11ea-907e-d29728ce74d8.jpg)



