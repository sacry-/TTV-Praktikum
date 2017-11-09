#!/bin/bash

set -e

rasperry=ef07
if [[ -n "$1" ]]; then
  rasperry="$1"
fi

# Session to Raspberry PI
ssh pi@FD16:ABCD:$rasperry:2::1
# password: TTvSprak

# sudo radvd -d 5 -m stderr -n
# Address has gone away:
# strg + c
sudo radvd -d 5 -m stderr -n