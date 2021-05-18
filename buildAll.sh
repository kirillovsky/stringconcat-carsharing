#!/bin/bash
set -e

(exec "${BASH_SOURCE%/*}/mainApp/buildImage.sh" "$1" )