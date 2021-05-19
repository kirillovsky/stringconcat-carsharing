#!/bin/bash
set -e

echo [Install git hooks] installing git hooks to project repo...

(exec "./gradlew" installGitHooks  --stacktrace --no-daemon)

echo [Install git hooks] git hooks sucessfully installed