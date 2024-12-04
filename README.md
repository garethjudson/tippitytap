# Tippity Tap

This is an example java app that simulates a service that transforms a list of taps into trips
Tried to keep it simple. 
Avoided eternal libs where possible. E.g. using something like lombok would reduce the effort of setters/getters.

## Assumptions
1. PAN is unique enough for tap on/tap off matching. The bus/company are not needed to match a tap on / tap off
   - this means that if a PAN were shared, it would break this system
   - obviously this was just to make the algorithm a little simpler
2. Limited data validation, assuming that the data is valid, no nulls where don't expect etc
   - should be clear that this is not how you'd treat a prod system
   - just reducing the amount of code to write in a short time

## Setup
You will need JDK 23. You should then be able to use the included gradlew wrapper to build and test the project

## Usage
build:
`./gradlew build`

test only:
`./gradlew test`

format project with spotless:
`./gradlew spotlessApply`

TODO: need to add a way to run the project against a csv file 

## Currently Missing
Need to add a csv handling