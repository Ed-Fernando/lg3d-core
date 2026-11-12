#!/bin/bash
#
# lg3d-x11-is-compatible <extdir> <x11dir>
#
# Returns an exit status of 1 if the interface required by this version of lg3d-core
# is incompatible with the interface version of the accompanying lg3d-x11 build.
# Versions are compatible if and only if: 1. The major numbers are equal and
# 2. The required minor number is less than or equal to the server minor number.

EXTDIR="$1"
X11DIR="$2"

INT_REQ_LINE=`tail -1 $EXTDIR/lg3d-x11-interface-required.xml | sed 's.<interfaceRequired="..' | sed 's.">..'`
INT_PROV_LINE=`tail -1 $X11DIR/version.xml | sed 's.<interfaceProvided="..' | sed 's.">..'`

MAJOR_REQ=`echo $INT_REQ_LINE | awk -F. '{print $1}'`
MINOR_REQ=`echo $INT_REQ_LINE | awk -F. '{print $2}'`
MAJOR_PROV=`echo $INT_PROV_LINE | awk -F. '{print $1}'`
MINOR_PROV=`echo $INT_PROV_LINE | awk -F. '{print $2}'`

if [ x$MAJOR_REQ != x$MAJOR_PROV ]; then
    echo "lg3d-x11-is-not-compatible=true"
    echo "lg3d-x11-error=Major numbers are incompatible. Required: $MAJOR_REQ. Actual: $MAJOR_PROV"
    exit 0
fi

if [ $MINOR_REQ -lt $MINOR_PROV ]; then
    echo "lg3d-x11-is-not-compatible=true"
    echo "lg3d-x11-error=Minor numbers are incompatible. Required: $MINOR_REQ. Actual: $MINOR_PROV"
    exit 0
fi

exit 0

