#!/bin/bash
set -e

function stopAll() {
    echo Stop project applications...
    (docker-compose down)
}

trap stopAll INT TERM

echo Build project applications...
(exec "${BASH_SOURCE%/*}/buildAll.sh")
echo Successfully built all modules

echo Run project applications...
(docker-compose up)