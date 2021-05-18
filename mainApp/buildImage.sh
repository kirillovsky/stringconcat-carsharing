#!/bin/bash
rootDir=$(dirname ../)
source "$rootDir/bashScriptUtils/utils.sh"
set -e

imageTag=$(imageTagOrLatest "$1")
repositoryName=454535773208.dkr.ecr.us-east-2.amazonaws.com/mainapp
imageFullName=$repositoryName:$imageTag

echo [Main App STARTING] building "$imageFullName"...

echo [Main App] creating jar...
(exec "$rootDir/gradlew" check bootJar  --stacktrace --no-daemon)

echo [Main App] remove old image "$imageFullName"...
(docker rmi -f "$imageFullName")

echo [Main App] creating docker image "$imageFullName"...
(docker build -t "$imageFullName" "${BASH_SOURCE%/*}")

echo [Main App FINISHED] image "$imageFullName" has been built