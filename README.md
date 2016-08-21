<img src="https://github.com/amaneureka/iota/blob/master/screenshots/logo.png" align="middle" />

# iOTA
A Terrain + Accident Mapping Utility in Real Time for preventing accidents

## What it does?

1. Finds out condition of road ahead and informs nearby iOTA users through Android App, for eg. Speed Breakers, Bumpy roads etc
2. Combines Geolocation and Accelerometer readings to detect accidents, so that near by iOTA users can provide help, reducing first aid response time
3. Android App for real time notifications

## Why?

1. Over 1.24 million people were killed in road accidents in 2013 alone, that is more than the number of people killed in all our wars put together [1]
2. Ignoring speed breakers on highway roads is one of the major causes of road accidents [1]
3. Also, more people die due to delayed first aid response time
4. While 4726 lives were lost in crashes due to humps, 6672 people died due to potholes and speed breakers [1]

[1] [Real Time Detection of Speed Breakers and Warning System for on-road Drivers](http://ieeexplore.ieee.org/stamp/stamp.jsp?arnumber=7443976)

## How it does?

1. GPS Module and Accelerometer are connected to Arduino
2. Arduino sends data to server using ESP8266 Wifi Module
3. NodeJS Web Sockets maintain persistent connection with the module
4. Geolocation and Accelerometer data is added to the database
5. A Python Service uses this data and generates its FFT Spectrum which measures its dominant frequencies
6. Machine Learning classifies dominant frequencies to type of terrain
7. This information is sent to nearby iOTA users through Android App and authorities

## Frequency Spectrum

![image](https://github.com/amaneureka/iota/blob/master/screenshots/image.png)

* These Curve represents Fast Fourier Transform for the movement of vehicle, at a given LatLong, in Z direction.
* This curve can be used to predict whether road ahead is bumpy or whether there is a speedbreaker ahead.
* This curve have multiple spikes stating there are many bumps.

## Aesthetics

* We have made an Android App for the end User , which will bring driver(user) all the necessary informations after being thoroughly preprocessed by our decision making algorithms.
* It includes one time User Registration with afterwards a simple User-Login Page. 
* Currently ,Information fetched by android app is related to the road conditions experienced by vehicles moving ahead.

## Algorithms

We have employed mainly two algorithms:
1. Fast Fourier Transform(FFT): It is used in plotting frequency spectrum of movement along z direction which is then processed by our decision making algorithm.
2. Decision Making Algorithm: This algorithm evaluates road conditions by understanding the nature of spikes in the FFT curve. If we have no Dominant Spike then it would shoot bumpy road, else if there is single dominant spike then it will shoot for a speed breaker and accordingly.

## Hardware Used

Hardware used:

1. Arduino 
2. Accelerometer 
3. GPS Module
4. Node MCU
5. Android Phone

## Softwares/Languages Used

1. NodeJS + WebSockets for Real Time communications
2. Embedded C for Arduino and NodeMCU
3. Python for Service Worker and Analytics
4. MySQL for storing and retrieving data
5. Google Cloud Messaging for Notifications
6. Android Studio for Android Application
7. Amazon WebServices

## Networking

Network Topology adopted by Our team is as follow:

![Architecture](https://github.com/amaneureka/iota/blob/master/screenshots/Architecture.jpg)

## Future and Scalability

There is one casualty every 4 minutes , according to Road Safety Organisations of India. This stat is single most reason why road safety requires technological intervention.

With our IOT concept of connecting cars under a single umbrella of local networks, it help make drivers more aware of what other drivers have experienced while passing the same road , and take decision accordingly.

Currently our functionality is limited till road analysis (like whether it is bumpy, has potholes), however many other applications such as:
 
 1. Smart vehicle congestion control 
 2. Smart Collision control
 3. Admin panels for SOS calls. Can also be implemented.

Using Sockets it can be easily scaled to large number of cars (upto 1000) in real time. Also with a dedicated algorithmic power it can solve many other problems arising due to poor driving behaviours.

## Developers

* Manraj Singh ([@ManrajGrover](https://github.com/ManrajGrover))
* Harshul Jain ([@harshul1610](https://github.com/harshul1610))
* Dinesh Agarwal ([@da230896](https://github.com/da230896))
* Aman Priyadarshi ([@amaneureka](https://github.com/amaneureka))
