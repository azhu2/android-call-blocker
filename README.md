# android-call-blocker

I used to use an app from the Play Store to based on prefix but it stopped working with Android P (API level 28 now requires system permission MODIFY_PHONE_STATE to block calls), so I decided to build my own Android P-compliant call blocker app.

## Running

```
make start &
make launch
```

### WSL Quirks
Testing on a physical device requires starting adb server in Windows first (via adb.exe) and then using same adb version in WSL (I set up a symlink in ANDROID_SDK_HOME/platform-tools to the same exe).
Or just start an emulator first, but Android Studio's a pain.