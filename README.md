# Time Blocks

### Version 0.2.0
* Each time block is 60 minutes
* Show all 24 time blocks in a day
* Use fixed style for time block row header
* Use fixed label as time block row header label (1-12 AM/PM format)

## What is it?
* A single-day view of 24 (60 min) time blocks with events 

## What is it not?
* A full-fledged calendar view with management of overlapping events

## How to use it
* Library builds an AAR file using Gradle (e.g. "timeblocks-release-0.2.0.aar")
* Add the AAR file to your project (e.g. under "libs/")
* Using Gradle import it in `build.gradle`
```gradle
repositories {
    ...
    flatDir {
        dirs 'libs'
    }
    ...
}

dependencies {
  ...    
  compile (name:'timeblocks-release-0.2.0', ext:'aar')
}
```

