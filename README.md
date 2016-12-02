#Android App to help review and mine reviews from the Google Play app store
The project has several complementary aims. These include:
1. An app that the development team can use to identify reviews they find valuable and enable them to process these reviews by categorising them and making notes.
2. To help gather data for academic research purposes on the approach used, the validity of the app and its use of mobile analytics
3. A learning experience
4. A tool to help software testers, and others, interested in exploring ways to use reviews to help improve their development and testing practices.
  
##Reviews in app stores provide clues
Reviews in app stores can help provide clues we can use to improve our work and the apps we work on. These clues can be bug reports, feature requests, information on how the app is being used, and what people expect the app to do. There may be lots of reviews, yet typically only a small percentage of these reviews are directly actionable. This project consists of software to help us to process reviews, identify the reviews we find valuable, and archive those we don't. 

Users will be able to score valuable reviews using various criteria and be able to describe potential tests based on the information gleaned from these reviews.

##The project 
The project currently comprises of two projects in active development:

1. An Android App to analyse and process reviews.
2. Python scripts to load historical reviews data into a SQLite database.

The Android App will be able to load and use historical data (downloaded from the Google Play Developer Console). Some examples of historical data are provided from the Android Kiwix app to enable people to practice working with the app using realistic data.

##Getting started
Here's the getting started for developers. For users, the app will soon be available from Google Play (once we've got enough functionality working).

###The Android app
The Android app is developed in Android Studio 2.2.2 and you should be able to import the project and build and run it as any Android app.

The app can also be built and deployed from the command-line using gradle e.g. `./gradlew installDebug` will build the app and deploy the debug build on an Android device.

###The python scripts are mainly for internal use. They're written to run in python3.

##Design of the Android app
The app will consist of several discrete layers:
1. Data: reviews
2. Application logic and navigation
3. The GUI

In addition the app will incorporate in-app mobile analytics to provide data on how the app is being used. The in-app mobile analytics is part of research led by Julian Harty as part of his Ph.D research.

##Approach to developing and testing the app
The app is itself part of the research project and will be developed based on analysing analytics, results, and feedback about the app. The journey and discoveries are - from a research and discovery perspective - as important as the app itself.

#Get involved
Feedback and contributions are welcome. Please raise requests and suggestions as issues on this project. If you'd like to contribute code and other related materials, please fork the project, add your contributions to your copy of the project, and then send pull requests.

Thank you


Julian Harty
=======

####Open questions
- What API level is expected?
- For the python code is there any requirements? I am guessing so. These should be in the environment setup or requirements.txt
