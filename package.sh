#!/bin/bash
set -e -x

git clone https://github.com/dmedov/android-docker-ci.git
cd android-docker-ci
docker build -t dmedov/android .
cd ..
docker run --tty --interactive --volume=$(pwd):/opt/workspace --workdir=/opt/workspace --rm dmedov/android  /bin/sh -c "run-tests-emulator.sh"

