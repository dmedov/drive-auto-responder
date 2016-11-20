#!/bin/bash
set -e -x

docker build -t dmedov/android .
docker run --interactive --volume=$(pwd):/opt/workspace --workdir=/opt/workspace --rm dmedov/android  /bin/sh -c "run-tests-emulator.sh"

