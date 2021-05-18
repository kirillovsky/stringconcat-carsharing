#!/bin/bash
set -e

(exec "${BASH_SOURCE%/*}/mainApp/pushImage.sh" "$1" )