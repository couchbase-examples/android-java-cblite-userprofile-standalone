# Quickstart in Couchbase Lite with Android and Java 
#### Build an Android App in Java with Couchbase Lite 

> This repo is designed to show you an app that allows users to log in and make changes to their user profile information.  User profile information is persisted as a Document in the local Couchbase Lite Database. When the user logs out and logs back in again, the profile information is loaded from the Database. 

Full documentation can be found on the [Couchbase Developer Portal](https://developer.couchbase.com/tutorial-quickstart-android-java-basic/).

[![Build Standalone](https://github.com/couchbase-examples/android-java-cblite-userprofile-standalone/actions/workflows/standalone-build-workflow.yml/badge.svg)](https://github.com/couchbase-examples/android-java-cblite-userprofile-standalone/actions/workflows/standalone-build-workflow.yml)

## Prerequisites
To run this prebuilt project, you will need:

- [Android Studio Arctic Fox or above](https://developer.android.com/studio)
- Android device or emulator running API level 22 or above 
- Android SDK installed and setup (> v.29.0.0)
- Android Build Tools (> v.29.0.0)
- JDK 8 (now embedded into Android Studio 4+)

### Installing Couchbase Lite Framework

- src/build.gradle already contains the appropriate additions for downloading and utilizing the Android Couchbase Lite dependency module. However, in the future, to include Couchbase Lite support within an Android app add the following within the Module gradle file (src/app/build.gradle)

```
dependencies {
    ...

    implementation 'com.couchbase.lite:couchbase-lite-android-ee:3.0.0-beta02'
}
```

## App Architecture

The sample app follows the [MVP pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter), separating the internal data model, from a passive view through a presenter that handles the logic of our application and acts as the conduit between the model and the view

## Try it out

* Open src/build.gradle using Android Studio.
* Build and run the project.
* Verify that you see the login screen.

## Conclusion

Setting up a basic Android app in Java with Couchbase Lite is fairly simple, this project when run will allow a user to login, update a user profile, and save the information into the embedded database.
