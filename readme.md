Geomajas GWT Client 1.x
=======================

What is Geomajas
----------------

Geomajas is an Open Source Web Mapping Framework written in Java. It provides both server and client components. This project contains the Client 1.x. It is based upon the GWT technology and uses the smartGWT widget library.

For more details about the project, how to use it, manuals and other information, take a look at the website at http://www.geomajas.org/ .

For commercial support see http://www.geosparc.com/ .


Build Process
-------------

In order to build Geomajas, we recommend using Maven (see http://maven.apache.org/). Following Maven best practices, the pom.xml files do not contain any Maven repositories.

You'll have to add the Geomajas Maven repository (http://maven.geomajas.org/) to your settings.xml file, which can be located in:
 
<pre>~/.m2/settings.xml</pre>

Next go to the root of the source code and run:

<pre>mvn install</pre>
