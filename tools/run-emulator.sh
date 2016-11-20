#!/bin/bash

set +e +x

echo "starting emulator..."
export SHELL=/bin/bash

export TERM=dumb
export ADB_INSTALL_TIMEOUT=10

echo 'vm.heapSize=512' >> ~/.android/avd/test.ini
echo 'hw.ramSize=1024' >> ~/.android/avd/test.ini
cat ~/.android/avd/test.ini

echo "no" | emulator -avd test -no-audio -no-window &

echo "waiting boot of emulator..."
android-wait-for-emulator.sh
sleep 30
adb shell input keyevent 82
adb shell svc power stayon true
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
adb shell settings put global animator_duration_scale 0

adb install /opt/tools/test-butler-app-1.1.0.apk