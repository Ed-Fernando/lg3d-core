#!/bin/tcsh -f
#
# lg3d-x11-interface-required.csh
#
# Prints to stdout the X server interface required for this lg3d-core,
# as specified in the file lg3d-core/ext/lg3d-x11-interface-required.xml.

set intReqLine = `tail -1 $LGCOREEXTDIR/lg3d-x11-interface-required.xml | sed 's.<interfaceRequired="..' | sed 's.">..' `
echo $intReqLine
