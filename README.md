# Tippity Tap

This is an example java app that simulates a service that transforms a list of public transport taps into trips.
Tried to keep it simple. Avoided eternal libs where possible.
Use an external lib for the csv parsing and testing.

## Assumptions
1. PAN is unique enough for tap on/tap off matching. The bus/company are not needed to match a tap on / tap off
   - this means that if a PAN were shared, it would break this system
   - obviously this was just to make the algorithm a little simpler
2. Limited data validation, assuming that the data is valid, no nulls where don't expect etc
   - should be clear that this is not how you'd treat a prod system
   - just reducing the amount of code to write in a short time
3. Input csv has a header row of: ID,DateTimeUTC,TapType,StopId,CompanyId,BusId,PAN
   - The casing is important. In the supplied requirements the BusId was BusID, I corrected this to be consistent with the other headers

## Setup
You will need JDK 23. You should then be able to use the included gradlew wrapper to build and test the project

## Usage
build:
`./gradlew build`

test only:
`./gradlew test`

format project with spotless:
`./gradlew spotlessApply`

To run the application:
`./gradlew run --args="<input csv> <output csv>"`
Where `<input csv>` should be replaced with a path to an input csv file where the taps will be read from 
And `<output csv>` should be replaced with a path to an output csv where trips output will be written to

## Testing with CSV
The easiest way to test the code is to use the `ApplicationTest`.
It uses a convention of reading csv files from the `src/test/resources/csvInput` directory.
There is expected to be a matching '-expected.csv' suffixed file which contains the trips associated with the recorded taps.
The input file can then be added to the parameterised test.

## Currently Missing
- adequate error handling
- adequate logging
- failures generally throw exceptions which totally abort the process 
- all built with the idea that data going in is valid
