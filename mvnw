#!/usr/bin/env sh
# ----------------------------------------------------------------------------
# Maven Wrapper
# ----------------------------------------------------------------------------
# Resolve this script's location
PRG="$0"
# Get script dir
SCRIPT_DIR="$(cd "$(dirname "$PRG")" && pwd)"
exec java -jar "$SCRIPT_DIR/.mvn/wrapper/maven-wrapper.jar" "$@"
