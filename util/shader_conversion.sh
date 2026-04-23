#!/usr/bin/env bash

# ================================================================================= #
#                                   +++  NOTE +++                                   #
# The working directory needs to be the render module root for this script to work! #
# ================================================================================= #

BASE_DIR="src/main/resources"

SOURCE="${BASE_DIR}/shader_glsl"
DESTINATION="${BASE_DIR}/shader"

mkdir -p "$DESTINATION"

rm -f "$DESTINATION"/*

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
