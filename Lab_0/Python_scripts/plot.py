import os
import numpy
import matplotlib.pyplot as plt

sample_size = 10


prob_ = [x/100 for x in range(1,96)]
time_taken_sum_prob     = [0]*len(prob_)
time_taken_average_prob = [0]*len(prob_)

width_ = [x for x in range(10,500,10)]
time_taken_sum_width     = [0]*len(width_)
time_taken_average_width = [0]*len(width_)


def collect_data(pth_to_folder):
    os.chdir(pth_to_folder)
    compile_arg = "javac " + pth_to_folder + "Assignment-0.java"
    os.system(compile_arg)

    for i in range(sample_size):
        path_to_class = pth_to_folder + "Main" 
        run_arg = "java " + pth_to_folder + "Assignment-0.java"

        os.system(run_arg)

        prob_txt = open(pth_to_folder + "probability.txt")
        width_txt = open(pth_to_folder + "width.txt")

        for line in prob_txt:
            prob,time = line.split()
            indx = int(float(prob)*100) - 1
            time = int(time)
            time_taken_sum_prob[indx] += time

        for line in width_txt:
            wid,time = line.split()
            indx = int(int(wid)/10) - 1
            time = int(time)
            time_taken_sum_width[indx] += time

def plot_data():
    time_taken_average_prob = numpy.asarray(time_taken_sum_prob)
    time_taken_average_width = numpy.asarray(time_taken_sum_width)

    plt.plot(prob_,time_taken_average_prob)
    plt.scatter(prob_,time_taken_average_prob)
    plt.savefig("probvstime")
    plt.show()

    plt.close()

    plt.plot(width_,time_taken_average_width)
    plt.scatter(width_,time_taken_average_width)
    plt.show()
    plt.savefig("widthvstime")

collect_data("/home/abhishekj/Documents/Sem5/COA/CS-311_Lab/Lab_0/")
plot_data()