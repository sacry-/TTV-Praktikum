#!/bin/bash

set -e

# Hop-Metrik
# RPL Root (rank-0)
# 2 nodes (rank-1)
# RPL Root (rank-0) - DIO Messages (prefix, number, instanz-id, rank-0) -> 2 nodes
# 2 nodes - DIO Messages (.., rank-1) -> other node - (.. rank-n + 1) -> ..


# Sensor via RPI: fd16:abcd:ef16:3:D1C1:6D7F:AB6E:1336
# RPI: FD16:ABCD:EF16:2::1

## Host
sudo /sbin/ifconfig eth0 inet6 add fd16:abcd:ef01:0001::16/64
ping6 -c 4 fd16:abcd:ef01:1::1

# Reset Sensor (right click 4 - red blinks)
sudo ip route add fd16:abcd:ef01:3::/64 via fd16:abcd:ef01:1::1
ping6 -c 4 fd16:abcd:ef01:3:D1C1:6D7F:AB6E:1336

ssh pi@FD16:ABCD:EF16:2::1 'sudo dumpcap  -P  -i monitor0  -w -'  | wireshark  -k  -i -


## Session RPI:
ssh pi@FD16:ABCD:EF16:2::1
# Session:

sudo systemctl stop lowpan
sudo systemctl start lowpan monitor


# Firefox plugin copper
coap://[fd16:abcd:ef01:3:d1c1:6d7f:ab6e:1336]/led
