#!/bin/bash

set -e

# Password Rasperry: TTvSprak

# Sensor via RPI: fd16:abcd:$rasperry:3:D1C1:6D7F:AB6E:1336
# RPI: FD16:ABCD:$rasperry:2::1

rasperry=ef07
if [[ -n "$1" ]]; then
  rasperry="$1"
fi

iotsensor="6d48:ab50"
if [[ -n "$2" ]]; then
  iotsensor="$2"
fi

# Host
sudo /sbin/ifconfig p4p1 up
sudo /sbin/ifconfig p4p1 inet6 add fd16:abcd:$rasperry:0002::0002/64
sudo ip route add fd16:abcd:$rasperry:3::/64 via fd16:abcd:$rasperry:2::1

ping6 -c 4 fd16:abcd:$rasperry:3:D1C1:$iotsensor:1336