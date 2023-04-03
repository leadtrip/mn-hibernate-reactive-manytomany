#!/bin/bash

function composeDown() {
  echo decomposing...
  docker-compose down
}

function composeUp() {
  echo composing...
  docker-compose up --build --force-recreate -d
}

function __run__() {
  composeDown
  composeUp $@
}

__run__ $@
