import os

def compile_java_file(path_to_folder):
    os.chdir(path_to_folder)
    os.system("javac Main.java")

def run_java_file(path_to_folder):
    os.chdir(path_to_folder)
    os.system("java Main")