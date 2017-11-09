#!/bin/bash

set -e

# Sensor via RPI: fd16:abcd:ef16:3:D1C1:6D7F:AB6E:1336
# RPI: FD16:ABCD:EF16:2::1

# Host
sudo /sbin/ifconfig p4p1 up
sudo /sbin/ifconfig p4p1 inet6 add fd16:abcd:ef16:0002::0002/64
sudo ip route add fd16:abcd:ef16:3::/64 via fd16:abcd:ef16:2::1

ping6 fd16:abcd:ef16:3:D1C1:6D7F:AB6E:1336

# Wireshark
ssh pi@FD16:ABCD:EF16:2::1 'sudo dumpcap  -P  -i lowpan0  -w -'  | wireshark  -k  -i -


# 1. Session to Raspberry PI
ssh pi@FD16:ABCD:EF16:2::1
# password: TTvSprak

# Address has gone away:
# sudo systemctl restart lowpan
ping6 -I lowpan0 fd16:abcd:ef16:3:D1C1:6D7F:AB6E:1336



# 2. Session to Raspberry PI
ssh pi@FD16:ABCD:EF16:2::1
# password: TTvSprak

# sudo radvd -d 5 -m stderr -n
# Address has gone away:
# strg + c
sudo radvd -d 5 -m stderr -n