# openweathermap

# Overview:

The purpose of this application is to create a simple App to display the current weather forecast using the OpenWeatherMap free weather API.

OpenWeatherMap application is developed using Kotlin and followed by MVVM architecture and Observer Design Pattern ia also used.

# Third party libraries:

Dagger2: It is a compile-time framework for dependency injection.

Lifecycle: Build lifecycle-aware components that can adjust behavior based on the current lifecycle state of an activity.

LiveData: LiveData is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware.

RxJava, RxAndroid: It provides a set of tools to help you write clean and simpler code.

Retrofit: Retrofit is a REST Client for Java and Android. It handles the REST API request and response.

Room: For Persistence Storage.

Google Location: To get the current location.

Glide: Glide is a fast and efficient way to handle the remote images.

# Source code:

In the source code following packages are used for,

di: To handle dependency injection related tasks.

models: To handle the models.

views: To handle the views like MainActivity.

viewmodels: To handle the View models, basically ViewModel is a class that is responsible for preparing and managing the data for MainActivity.

repositories: It handles both remote and local data repositories.

util: Its used to do task related to Connectivity Manager, Application Constants and Permission Manager.

# Test:

I have tested the app its working fine.
I have written a Unit test case for WIFI enabled or not which is present in MainActivityTest class

# Note:

Due to some confusion and running out of time, I have not written the logic to schedule the API call for every 2 hours. 
The confusion is the whether request should be call only if application is in foreground or even if the application is not running.
We can achieve this by using WorkManager(Once confusion is resolved, If you want I can complete this part also).

Thanks.
