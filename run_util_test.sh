#!/bin/bash
rm -rf build/test/stubs
rm -rf build/test/classes
mkdir -p build/test/stubs
mkdir -p build/test/classes

echo "Compiling stubs..."
find test/stubs -name "*.java" > sources_stubs.txt
javac -d build/test/stubs @sources_stubs.txt
if [ $? -ne 0 ]; then
    echo "Stub compilation failed!"
    exit 1
fi

# Construct classpath excluding jfoenix because we are stubbing it
CP="libs/test/*:build/test/stubs"
for jar in libs/*.jar; do
    if [[ $jar != *"jfoenix"* ]]; then
        CP="$CP:$jar"
    fi
done

echo "Classpath: $CP"

echo "Compiling LibraryAssistantUtil and Test..."
javac -sourcepath "" -cp "$CP" -d build/test/classes \
src/library/assistant/util/LibraryAssistantUtil.java \
test/library/assistant/util/LibraryAssistantUtilTest.java
if [ $? -ne 0 ]; then
    echo "Source compilation failed!"
    exit 1
fi

echo "Running Test..."
java -cp "build/test/classes:$CP" org.junit.runner.JUnitCore library.assistant.util.LibraryAssistantUtilTest
