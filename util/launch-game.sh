#!/usr/bin/env bash

# Useful function to check if a command exists.
command_exists() {
  command -v "$1" >/dev/null 2>&1
}

# Set variables for later use.
GAME_VERSION="0.1.0"
GAME_FILE="build/libs/Dreath-Game-$GAME_VERSION.jar"
WORKING_DIRECTORY="run"

if command_exists kotlin; then
  ( cd "$WORKING_DIRECTORY" && kotlin "../$GAME_FILE" "$@" )
elif command_exists java; then
  ( cd "$WORKING_DIRECTORY" && java -jar "../$GAME_FILE" "$@" )
else
  echo "Either Kotlin or Java must be installed to run the game."
  exit 1
fi
