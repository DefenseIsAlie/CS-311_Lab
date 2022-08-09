import os
import python_scripts.compile_and_run
import python_scripts.plot

dirs = os.listdir()
if "Lab_0" in dirs:
    os.chdir(os.getcwd()+"/Lab_0")
folder_pth = os.getcwd()

python_scripts.compile_and_run.compile_java_file(folder_pth)

python_scripts.compile_and_run.run_java_file(folder_pth)

python_scripts.plot.collect_data(folder_pth)

python_scripts.plot.plot_data()
