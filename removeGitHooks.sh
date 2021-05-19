#!/bin/bash
set -e

echo [Remove git hooks] remove git hooks from project repo...

(exec "./gradlew" removeGitHooks  --stacktrace --no-daemon)

echo [Remove git hooks] git hooks sucessfully removed