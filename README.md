# CNIT355-MR-JK

Welcome to Calenduh

This is a group calendar designed to organize organizations and make event planning/coordinating a breeze!
Check it out!

All of our testing was done on the Pixel 2 XL running on Android Version 8.0.0. 

Some of the initial firebase setup and authentication comes from external sources.
The initial setup including creating the database and logging in/signing up comes from here
https://www.sitepoint.com/creating-a-cloud-backend-for-your-android-app-using-firebase/

We used these sites to learn about firebase and how to read/write from it
https://www.tutorialspoint.com/firebase/
https://firebase.google.com/docs/database/android/lists-of-data

There is an event class that holds our event objects that helps writing and reading from firebase. To write the data, instead of using a push where a unique id is created by firebase, we created simple counter that is used as the id and created a new child for each event. To read the data, we added a ValueEventListener which executes when there is new data in the database. We had to use Map to map the keys to the values that we wanted to extract. 
