Jar-based Application Deployment within LG3D
--------------------------------------------

Since the 0.7.0 release a simpler method for deploying applications has been integrated
into LG3D. Previously, only those classes built as part of the incubator project, or
configured into the sources of the cvs release could be easily added to lg3d. With this
change all that is required is to jar the file correctly and put it in the ext/app
directory. When a correctly configured jar file is found in the ${LG3DHOME}/ext/app
directory it is added to the taskbar.

The process is as follows:

1. Upon startup LG3D looks in the ${LG3DHOME}/ext/app directory for any jar files and
   adds these to the classpath. This means that the applications and any resources are
   made available to LG3D.
   
   Note: In future releases the jar file shall be loadable while LG3D is running.

2. The jar files within the ${LG3DHOME}/ext/app directory are then processed to
   determine the configuration of the application. This processing occurs by examining
   the Manifest file within the jar file. From the information in the jar file a
   standard configuration event is created and passed to the lg3d event system,
   resulting in an appropriate launcher being added to the taskbar

Manifest files provide a range of meta information about a jar file. This information can
be used by java programs to determine certain characteristics, and help load environment
variables required by the application within the jar file for successful execution. More
information about manifest files can be read on at:
http://java.sun.com/docs/books/tutorial/jar/manifest/index.html

LG3D uses/defines the following application parameters:
1. Implementation Title: The name of the application is passed to the launcher
2. Main-Class: This is the standard manifest parameter that specifies the class file to
   execute when executing a far file (e.g. java -jar syntax). When used in LG3D this
   provides a mechanism for quickly deploying applications without having to define a
   full lgcfg file.
3. Icon-Filename: This is a special parameter used by LG3D, it provides the name of the
   icon to display on the taskbar, when using the Main-Class parameter. If this parameter
   isn't provided a default icon will be used.
4. Config-File: This is a special parameter used by LG3D. It determines the location of
   the lgcfg configuration file. More information on the lgcfg format can be found at:
   http://wiki.java.net/bin/view/Javadesktop/FirstIncubatorapp

Note 1: The order of priority is Config-File, then Main-Class. If both are provided, then
        the Config-File configuration will be used.
Note 2: The Config-File and Icon-Filename are accessed as resources, therefore they can be
        anywhere on the CLASSPATH. LG3D automatically adds the jar files in ext/app to the
	CLASSPATH therefore any resources bundled with the jar are accessible.

For examples on deploying applications via this method please visit the LG3D Tutorials at:
https://lg3d-core.dev.java.net/tutorial
