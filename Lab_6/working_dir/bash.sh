#!/bin/bash

#find src -type f -name "*.class" -delete

java -jar jars/simulator.jar src/configuration/config.xml ./stat$1.txt test_cases/$1