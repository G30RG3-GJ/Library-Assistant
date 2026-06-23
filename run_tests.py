import os
import shutil
import subprocess
import sys

def main():
    # Clean build directory
    if os.path.exists("build"):
        shutil.rmtree("build")
    os.makedirs("build/stubs")
    os.makedirs("build/classes")
    os.makedirs("build/test-classes")

    # Classpath for compilation
    libs = []
    # Add all jars in libs
    for file in os.listdir("libs"):
        if file.endswith(".jar"):
            path = os.path.join("libs", file)
            libs.append(path)

    classpath_str = ":".join(libs)

    # 1. Compile Stubs
    print("Compiling Stubs...")
    stubs_src = []
    for root, dirs, files in os.walk("libs/stubs"):
        for file in files:
            if file.endswith(".java"):
                stubs_src.append(os.path.join(root, file))

    if stubs_src:
        cmd_stubs = ["javac", "-d", "build/stubs"] + stubs_src
        subprocess.check_call(cmd_stubs)

    # 2. Compile DatabaseHandler.java
    print("Compiling DatabaseHandler.java...")
    # Do not use -sourcepath src, only compile the file
    # Classpath includes build/stubs and libs
    cp_db = f"build/stubs:{classpath_str}"
    cmd_db = ["javac", "-cp", cp_db, "-d", "build/classes", "src/library/assistant/database/DatabaseHandler.java"]
    subprocess.check_call(cmd_db)

    # 3. Compile Test
    print("Compiling Test...")
    test_cp = f"build/classes:build/stubs:{classpath_str}:libs/test/junit-4.13.2.jar:libs/test/hamcrest-core-1.3.jar"
    cmd_test = ["javac", "-cp", test_cp, "-d", "build/test-classes", "test/library/assistant/database/DatabaseHandlerTest.java"]
    subprocess.check_call(cmd_test)

    # 4. Run Test
    print("Running Test...")
    # Need src in classpath for resources
    run_cp = f"build/test-classes:build/classes:build/stubs:{classpath_str}:libs/test/junit-4.13.2.jar:libs/test/hamcrest-core-1.3.jar:src"
    cmd_run = ["java", "-cp", run_cp, "org.junit.runner.JUnitCore", "library.assistant.database.DatabaseHandlerTest"]
    subprocess.check_call(cmd_run)

if __name__ == "__main__":
    main()
