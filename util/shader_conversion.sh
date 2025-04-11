#!/usr/bin/env bash

# =========================================================================== #
#                               +++  NOTE: +++                                #
# The working directory needs to be the project root for this script to work! #
# =========================================================================== #

echo "$(dirname "$0")"

BASE_DIR="game/src/main/resources"

SOURCE="${BASE_DIR}/shader"
DESTINATION="${BASE_DIR}/shader_compiled"

mkdir -p "$DESTINATION"

# Shader file extensions
EXTENSIONS=(".glsl" ".vert" ".frag" ".geom" ".tese" ".tesc" ".comp" )

find "$SOURCE" -type f | while read -r SHADER_FILE; do
    for EXT in "${EXTENSIONS[@]}"; do
        if [[ "$SHADER_FILE" == *$EXT ]]; then
            OUT_FILE="$DESTINATION/$(basename "$SHADER_FILE").spv"
            glslangValidator -V "$SHADER_FILE" -o "$OUT_FILE"
            break
        fi
    done
done