#!/bin/bash

set +e +x

run-emulator.sh

# run junit tests and integration tests in verbose mode
./gradlew test cAT -i


