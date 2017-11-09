#!/bin/bash

set -e

rasperry=ef07
if [[ -n "$1" ]]; then
  rasperry="$1"
fi


ssh pi@FD16:ABCD:$rasperry:2::1
# Session:

sudo systemctl stop lowpan
sudo systemctl start lowpan monitor

# Firefox plugin copper
coap://[fd16:abcd:ef01:3:d1c1:6d7f:ab6e:1336]/led