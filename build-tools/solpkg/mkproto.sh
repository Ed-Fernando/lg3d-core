#!/usr/bin/bash -f

PROTOTYPE="prototype"
PKGINFO="pkginfo"

# delete configuration files for pkgs

if [ -f $PROTOTYPE ]; then
  echo delete $PROTOTYPE
  rm $PROTOTYPE
fi

# create a prototype file

echo "i $PKGINFO" > $PROTOTYPE
pkgproto . | sed -e "s/$USER $GROUP/root other/" | grep -v " $PROTOTYPE " | grep -v "$PKGINFO" >> $PROTOTYPE

