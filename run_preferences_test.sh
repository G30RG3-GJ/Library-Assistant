#!/bin/bash
mkdir -p build/test/stubs
mkdir -p build/test/classes

# Compile AlertMaker stub
javac -d build/test/stubs test/stubs/library/assistant/alert/AlertMaker.java

# Compile Preferences and Test
javac -sourcepath "src" -cp "libs/*:libs/test/*:build/test/stubs" -d build/test/classes \
src/library/assistant/ui/settings/Preferences.java \
test/library/assistant/ui/settings/PreferencesTest.java

# Run Test
java -cp "build/test/classes:build/test/stubs:libs/*:libs/test/*" org.junit.runner.JUnitCore library.assistant.ui.settings.PreferencesTest
