#!/bin/bash

# Extracting file parts
NAME1=$(echo "$1" | sed 's/\.[^\.]*$//')
EAD="${NAME1}EAD.txt"
MHC="${NAME1}MHC.txt"

INFTY=1000000

grep -P '(?<=Earliest Arrival Date: ).*' $1 |  sed 's/Unreachable/'${INFTY}'/g' | cut -d' ' -f4-  > $EAD
grep -P '(?<=Minimum hopcount: ).*' $1 |  sed 's/Unreachable/'${INFTY}'/g' | cut -d' ' -f3-  > $MHC
#grep ^Earliest $1 | egrep "[0-9]{1,}" -o > $EAD
#grep ^Minimum $1 | egrep "[0-9]{1,}" -o > $MHC
