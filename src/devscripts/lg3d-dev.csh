#!/bin/tcsh -f
#
# lg3d-dev.csh [<config-file>]
#
# Run the LG desktop in "development mode" as a normal
# application (in a window) under the control of another
# window system. This mode does not support native window
# applications; only 3D applications are supported.

set tmp = $0
set scriptdir = ${tmp:h}
if (${scriptdir} == ${tmp}) set scriptdir = .
source ${scriptdir}/setup.csh

set config = $1

if (-f ${scriptdir}/../etc/lg3d/$config) then
    echo "Using config file ${config}"
else
    set config = lgconfig_1p_nox.xml
endif

setenv LGCONFIG file:///${etcdir}/lg3d/${config}
setenv DISP_CONFIG -Dlg.displayconfigurl=file:///${etcdir}/lg3d/displayconfig/j3d1x1
setenv LG_SETTINGS "${LG_SETTINGS} -Dlg.fws.mode=dev ${TOOLKIT}"

# comment out the following until we start uring RMI again
# pkill -f "rmiregistry ${RMI_PORT}"
# sleep 1
# rmiregistry ${RMI_PORT} &

set javacmd = "${JAVA_HOME}/bin/java $jvmargs -Xbootclasspath/a:${BOOTCLASSPATH} ${PROFILE_FLAGS} ${DEBUG_FLAGS} ${CONFIGDIR} -Dj3d.sortShape3DBounds=true -Dlg.configurl=${LGCONFIG} ${DISP_CONFIG} ${LG_SETTINGS} org.jdesktop.lg3d.displayserver.Main"

echo "LG3D Dir         : ${lgdir}"            > ${logfile}
echo "LGCONFIG         : ${LGCONFIG}"        >> ${logfile}
echo "LG_SETTINGS      : ${LG_SETTINGS}"     >> ${logfile}
echo "LGX11HOME        : ${LGX11HOME}"       >> ${logfile}
echo "X Server Version : `cat ${LGX11HOME}/VERSION`" >> ${logfile}
echo "JAVA_HOME        : ${JAVA_HOME}"       >> ${logfile}
echo "CLASSPATH        : ${CLASSPATH}"       >> ${logfile}
echo "path             : ${path}"            >> ${logfile}
echo "LD_LIBRARY_PATH  : ${LD_LIBRARY_PATH}" >> ${logfile}
echo "javacmd          : ${javacmd}"         >> ${logfile}

${javacmd} |& tee -a ${logfile} | grep "SEVERE\|WARNING\|ClassVersionError"
