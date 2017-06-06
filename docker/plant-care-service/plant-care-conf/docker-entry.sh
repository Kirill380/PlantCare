#!/bin/bash

IFS=':' read -r -a hostPort <<< $CASSANDRA_HOST_PORT
CASSANDRA_HOST=${hostPort[0]}
CASSANDRA_PORT=${hostPort[1]}

SCRIPT=$(readlink -f "$0")
SCRIPTPATH=$(dirname "$SCRIPT")
source $SCRIPTPATH/wait_util.sh

echo "Get supporting services information ..."
echo "Database service URL: ${MYSQL_HOST_PORT}"
echo "Waiting timeout: ${DEPENDENCY_SERVICES_WAIT_TIMEOUT_SEC}"

# Wait for needed services before startup
waitForHostPortByURL "$MYSQL_HOST_PORT" "$WAIT_TIMEOUT_SEC"
waitForHostPortByURL "$CASSANDRA_HOST_PORT" "$DEPENDENCY_SERVICES_WAIT_TIMEOUT_SEC"

# Launch plant care service

java -XX:+PrintFlagsFinal -XX:+PrintGCDetails $JAVA_OPTIONS -jar /plant-care-service.jar --mysql.url=${MYSQL_HOST_PORT}  --cassandra.host=${CASSANDRA_HOST} --cassandra.port=${CASSANDRA_PORT}
