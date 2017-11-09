#!/bin/bash

set -e

# Sensor via RPI: fd16:abcd:$rasperry:3:D1C1:6D7F:AB6E:1336
# RPI: FD16:ABCD:$rasperry:2::1

sudo /sbin/ifconfig eth0 inet6 add fd16:abcd:ef01:0001::16/64
ping6 fd16:abcd:ef01:1::1

# Reset Sensor (right click 4 - red blinks)
sudo ip route add fd16:abcd:ef01:3::/64 via fd16:abcd:ef01:1::1
ping6 fd16:abcd:ef01:3:D1C1:6D7F:AB6E:1336