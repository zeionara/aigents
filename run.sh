#!/bin/bash

# java -jar Aigents.jar

java -cp Aigents.jar:*:lib/* -Xms2048m -Xmx3072m net.webstructor.agent.Farm -Dsun.zip.disableMemoryMapping=true
