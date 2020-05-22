start:
	npx react-native start

# On WSL, requires starting adb server in Windows first (via adb.exe) and using same adb version in WSL
# (I set up a symlink in ANDROID_SDK_HOME/platform-tools to the same exe)
launch:
	npx react-native run-android