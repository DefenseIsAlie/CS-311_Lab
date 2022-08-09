import os

def compile_java_file(path_to_javaFile):
    os.system("javac "+path_to_javaFile)

def run_java_file(path_to_folder):
    os.chdir(path_to_folder)
    os.system("java Main")