#!/bin/bash
rootDir=$(dirname ../)
source "$rootDir/bashScriptUtils/utils.sh"
set -e

imageTag=$(imageTagOrLatest "$1")
repositoryName=454535773208.dkr.ecr.us-east-2.amazonaws.com/mainapp
imageFullName=$repositoryName:$imageTag

echo [Main app STARTING] pushing image "$imageFullName"...

echo [Main app] pushing image...
docker push "$imageFullName"

echo [Main app FINISHED] image "$imageFullName" pushed