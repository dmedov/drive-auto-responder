#!/bin/bash

set +e +x

echo "starting emulator..."
export SHELL=/bin/bash
echo "no" | emulator64-arm -avd test -no-audio -no-window &

echo "waiting boot of emulator..."
android-wait-for-emulator.sh
adb shell input keyevent 82
adb shell svc power stayon true
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
adb shell settings put global animator_duration_scale 0

adb install /opt/tools/test-butler-app-1.1.0.apk