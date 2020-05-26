# LAU-java-callout

## Overview

This program calculate and verify LAU Signature value for the following APIs in the Sandbox:
* gpi API
* g4c API
* gCase API

The jar is used in the Apigee gateway as a Java Callout Policy. The policy is used in the ``PreFlow`` in both ``Request`` and ``Response``.

## Getting Started

### Prerequisites

* Java 1.7 and above
* maven 3.5.* and above

### Before Install

Two of the Apigee dependency jar files not available in any
public repo and it's added in the repo. You need to install them manually in your local .m2 directory in order to build successfully.

```
mvn install:install-file \
   -Dfile=dependencies/expressions-1.0.0.jar \
   -DgroupId=com.apigee.edge \
   -DartifactId=expressions \
   -Dversion=1.0.0 \
   -Dpackaging=jar \
   -DgeneratePom=true

mvn install:install-file \
 -Dfile=dependencies/message-flow-1.0.0.jar \
 -DgroupId=com.apigee.edge \
 -DartifactId=message-flow \
 -Dversion=1.0.0 \
 -Dpackaging=jar \
 -DgeneratePom=true
```


### Install & Deploy

To run the server, please execute the following:

```
mvn clean install
```

## Authors

yin.xu@swift.com

## License

To be determined, private to SWIFT.
