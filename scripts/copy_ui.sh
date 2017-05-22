#!/bin/sh
# should be called from project root via mvn install
# but really executes in ui folder, because bounded to ui module build time
FROM_DIR="$PWD/ui/resources/public"
TO_DIR="$PWD/core-service/src/main/resources/public"

cp -rvf $FROM_DIR $TO_DIR
