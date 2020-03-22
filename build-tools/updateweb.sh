#!/bin/sh

EDSCRIPT="/tmp/ed$$"
trap "rm -f $EDSCRIPT" SIGINT SIGTERM SIGKILL

cat << EOF > $EDSCRIPT
/NEXT DAILY/
a
<li>$1  <a href="http://javadesktop.org/lg3d/builds/daily/$2">$2</a></li>
.
w
EOF

ed - binary-builds.html < $EDSCRIPT

cvs commit -m 'Daily build' binary-builds.html
