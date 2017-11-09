#!/bin/bash

set -e

# Password Rasperry: TTvSprak

rasperry=ef07
if [[ -n "$1" ]]; then
  rasperry="$1"
fi

iotsensor="6d48:ab50"
if [[ -n "$2" ]]; then
  iotsensor="$2"
fi

ssh pi@FD16:ABCD:$rasperry:2::1
# password: TTvSprak

# Address has gone away:
# sudo systemctl restart lowpan
ping6 -c 4 -I lowpan0 fd16:abcd:$rasperry:3:D1C1:$iotsensor:1336