#!/bin/bash

set -e

# Password Rasperry: TTvSprak

rasperry=ef07
if [[ -n "$1" ]]; then
  rasperry="$1"
fi

ssh pi@FD16:ABCD:$rasperry:2::1 'sudo dumpcap  -P  -i lowpan0  -w -'  | wireshark  -k  -i -