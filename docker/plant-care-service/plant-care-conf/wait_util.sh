#!/bin/bash
# contains functions that implements awaiting for startup of dependency services:
# - parse 'host' and port part from service URL
# - wait for port (on given host) to become busy

parseHostPort() {
  declare HOST_PORT=$1
  declare ARR={}

  IFS=: read -ra ARR <<<"$HOST_PORT"

  declare PORT_PART=${ARR[-1]}
  declare HOST_PART=${ARR[-2]}

  IFS=/ read -ra P_ARR <<<"$PORT_PART"
  IFS=/ read -ra H_ARR <<<"$HOST_PART"

  WAIT_PORT=${P_ARR[0]}
  WAIT_HOST=${H_ARR[-1]}

  HOST_PORT_ARR=($WAIT_HOST $WAIT_PORT)

  echo Parsing results: ${HOST_PORT_ARR[@]}
}

waitForHostPortByURL() {
  declare HOST_PORT=$1
  declare WAIT_TIMEOUT_SEC=$2
  parseHostPort "$HOST_PORT"
  waitForHostPort "$WAIT_HOST" "$WAIT_PORT" "$WAIT_TIMEOUT_SEC"
}

waitForHostPort() {
    WAIT_HOST=$1
    WAIT_PORT=$2
    WAIT_TIMEOUT_SEC=$3

    echo "Waiting for service \"$WAIT_HOST:$WAIT_PORT\", timeout = $WAIT_TIMEOUT_SEC (-1 means no timeout) ..."

    local COUNTDOWN=$WAIT_TIMEOUT_SEC
  local SHIFT=1; [ $COUNTDOWN -lt 0 ] && SHIFT=0

    until [ $COUNTDOWN -eq 0 ]; do
        isHostPortReachable && echo "Service is reachable, proceeding..." && return 0;

        echo "Left ${COUNTDOWN} seconds of timeout ..."
        sleep 1s;
        let COUNTDOWN=$COUNTDOWN-$SHIFT;
    done;

    echo "Timeout is over. Service is unreachable. Aborting!";
    exit 1s;
}

isHostPortReachable() {
  bash -c "cat < /dev/null > /dev/tcp/${WAIT_HOST}/${WAIT_PORT}" >/dev/null 2>/dev/null \
        && return 0 \
        || return 1;
}
