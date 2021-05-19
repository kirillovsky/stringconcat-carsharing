#!/bin/bash
set -e

(exec "./gradlew" dependencyUpdates  --stacktrace --no-daemon)