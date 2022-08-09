import os
import python_scripts.compile_and_run
import python_scripts.plot

# Check if numpy and python are installed
try:
    import numpy
    import matplotlib
except:
    print("Numpy and Matplotlib must be installed")
    print("try --> $ pip install -r requirements.txt")

# Dangeorus , change it
def Search_dirs_for_file():
    dirs = os.listdir()
    
    for directory in dirs:
        pth = os.getcwd() + "/" + directory
        try:
            if "Main.java" in os.listdir(pth) and directory == "Lab_0":
                os.chdir(pth)
                collect_data_and_plot()
        except:
            pass 

def collect_data_and_plot():
    folder_pth = os.getcwd()

    # Compiles the java file
    python_scripts.compile_and_run.compile_java_file(folder_pth)

    # Test runs the java file
    python_scripts.compile_and_run.run_java_file(folder_pth)

    # Runs the program for 10 times and plots the data
    python_scripts.plot.collect_data(folder_pth)
    python_scripts.plot.plot_data()


if "Main.java" not in os.listdir():
    Search_dirs_for_file()
else: 
    collect_data_and_plot()