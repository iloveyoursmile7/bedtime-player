# Bedtime Player

Android 13-compatible standalone local audio player. This app does not control Samsung Music.

## Build
1. Install Android Studio with Android SDK Platform 35 and JDK 17.
2. Open the `BedtimePlayer` directory as a project.
3. Allow Gradle sync and dependency downloads.
4. Select **Build > Build APK(s)**.
5. Install `app/build/outputs/apk/debug/app-debug.apk` on the tablet.

## Use
Tap **ADD AUDIO FILES**, choose local audio files, select a track, and tap PLAY. STOP stops playback, and REPEAT ONE loops the selected track. A foreground notification keeps playback active when the screen is off. Android may require battery settings to allow unrestricted background operation.

## Notes
- No APK is included: the build environment did not have the Android SDK/Gradle.
- The project has not been compiled or device-tested.
- Android file picker grants persistent access only to selected files.
- No internet permission, ads, or analytics.
