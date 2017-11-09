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
# Session:

sudo systemctl stop lowpan
sudo systemctl start lowpan_monitor

# Firefox plugin copper
# coap://[fd16:abcd:ef01:3:d1c1:$iotsensor:1336]/led