#!/bin/bash

set -e

rasperry=ef07
if [[ -n "$1" ]]; then
  rasperry="$1"
fi

ssh pi@FD16:ABCD:$rasperry:2::1
# password: TTvSprak

# Address has gone away:
# sudo systemctl restart lowpan
ping6 -I lowpan0 fd16:abcd:$rasperry:3:D1C1:6D7F:AB6E:1336