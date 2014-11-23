
# Rapid start development and test
with Android Studio, Gradle, Espresso, Robolectric, AndroidAnnotations, JaCoCo

[![Build Status](https://travis-ci.org/nenick/AndroidAppDevelopment.svg)](https://travis-ci.org/nenick/AndroidAppDevelopment) **& UnitTest** [![Coverage Status](https://coveralls.io/repos/nenick/AndroidAppDevelopment/badge.png?branch=master)](https://coveralls.io/r/nenick/AndroidAppDevelopment?branch=master)

***Wishes, improvements and discussions about the stuff here are welcome***

[see also wiki for more help](https://github.com/nenick/AndroidAppDevelopment/wiki)

More simple project templates can be found at https://github.com/nenick/android-gradle-template

## Getting Started

### Check if the project and tests works on your machine

***clone, attach phone (or start emulator), run `Scripts/run-tests.sh` from project root***

### Check if you get this project to work with andorid studio

***import to android studio, start development, press play***

## Test support in IDE

install the plugin https://github.com/evant/android-studio-unit-test-plugin you can install the plugin through the plugin installer in Android Studio

This project template support AndroidStudio.

Run tests in Android Studio need some configuration:  [see wiki for description](https://github.com/nenick/AndroidAppDevelopment/wiki/Tests-in-Android-Studio---IntellJ)

## Tests on command line

**unit tests:** `./gradlew testNormLDebug`

* append `jacocoReport` for coverage report ***can't be combined with the integration tests***

**integration tests:** `./gradlew testIntegrationDebug`

* append `jacocoReport for coverage report ***can't be combined with the unit tests***

**acceptance tests:** `./gradlew connectedAndroidIntegrationTest`

* replace by `:App:connectedCheck` for coverage report

### rest example
Currently RestActivity example fails when no server is reachable.

* start `Script/start-wiremock.sh`
* kill  `Script/start-wiremock.sh kill`

## Test results

* build/index.html (**robolectric & unit tests**) *(should collect all reports, current is miss the some reports)*
* App/build/test-coverage-report/index.html (**unit test coverage**)
* App/build/test-coverage-report/index.html (**integration test coverage**)
* App/build/reports/androidTests/connected/index.html (**acceptance tests**)
* App/build/reports/coverage/debug/index.html (**acceptance tests coverage**)


## Last tests done with

* Android Studio 0.8.14
* Gradle Build Tools 0.12.+
* Gradle 1.12

## Features done

* Gradle + AndroidStudio as development Enironment
* Robolectric for unit tests
    * Mockito
    * Code coverage with JaCoCo
    * Code coverage with Teamcity
* Robolectric for component tests
    * Code coverage with JaCoCo
    * Code coverage with Teamcity
* FEST Android assertions
* Espresso for acceptance tests
* [AndroidAnnotations](http://androidannotations.org/) generate dependency injection
* [RoboCoP](https://github.com/mediarain/RoboCoP) generate database management
* Shortcut: jump between test and implementation with default short cut
* [Travis](https://travis-ci.org/) CI runs all test variants
* [Coveralls](https://coveralls.io/) shows unit test code coverage [coveralls-gradle-plugin](https://github.com/kt3k/coveralls-gradle-plugin)
* Clean Architecture based on this create example [https://github.com/android10/Android-CleanArchitecture]


## Scripts

* run-tests.sh execute all test variants
* run-tests-fast.sh execute just the robolectric tests
* run-tests-travis.sh do all steps necessary for full automatic builds, like install custom dependencies

# Some notes

The base example is copied from https://github.com/android10/Android-CleanArchitecture I have only combined this with my favourite development tools and test setup.