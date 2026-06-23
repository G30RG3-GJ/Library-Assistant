#!/bin/bash
mkdir -p build/test/stubs
mkdir -p build/test/classes

javac -d build/test/stubs \
test/stubs/library/assistant/database/DatabaseHandler.java \
test/stubs/library/assistant/ui/listmember/MemberListController.java \
test/stubs/library/assistant/alert/AlertMaker.java

javac -sourcepath "" -cp "libs/*:libs/test/*:build/test/stubs" -d build/test/classes \
src/library/assistant/data/model/Book.java \
src/library/assistant/data/model/MailServerInfo.java \
src/library/assistant/database/DataHelper.java \
src/library/assistant/ui/settings/Preferences.java \
test/library/assistant/database/DataHelperTest.java \
test/library/assistant/ui/settings/PreferencesTest.java

java -cp "build/test/classes:build/test/stubs:libs/*:libs/test/*" org.junit.runner.JUnitCore library.assistant.database.DataHelperTest library.assistant.ui.settings.PreferencesTest
