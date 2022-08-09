import os
import numpy
import matplotlib.pyplot as plt

sample_size = 0

if __name__ == "__main__":
    sample_size = 50
else:
    sample_size = 10


prob_ = [x/100 for x in range(1, 91)]
time_taken_sum_prob = [0]*len(prob_)
time_taken_average_prob = [0]*len(prob_)

width_ = [x for x in range(10, 1000, 10)]
time_taken_sum_width = [0]*len(width_)
time_taken_average_width = [0]*len(width_)


def collect_data(pth_to_folder):
    os.chdir(pth_to_folder)

    for i in range(sample_size):
        os.chdir(pth_to_folder)
        run_arg = "java Main"

        os.system(run_arg)

        prob_txt = open(pth_to_folder + "/probability.txt")
        width_txt = open(pth_to_folder + "/width.txt")

        for line in prob_txt:
            prob, time = line.split()
            indx = int(float(prob)*100) - 1
            time = int(time)
            time_taken_sum_prob[indx] += time

        for line in width_txt:
            wid, time = line.split()
            indx = int(int(wid)/10) - 1
            time = int(time)
            time_taken_sum_width[indx] += time


def plot_data():
    time_taken_average_prob = numpy.asarray(time_taken_sum_prob)
    time_taken_average_prob = time_taken_average_prob * (1 / sample_size)
    time_taken_average_width = numpy.asarray(time_taken_sum_width)
    time_taken_average_width = time_taken_average_width * (1 / sample_size)

    plt.plot(prob_, time_taken_average_prob)
    plt.scatter(prob_, time_taken_average_prob)
    plt.title("Probability vs Time")
    plt.xlabel("Probability")
    plt.ylabel("Averge time taken")
    # Width = 500 sample size
    plt.savefig("probvstime")
    plt.show()

    plt.close()

    plt.plot(width_, time_taken_average_width)
    plt.scatter(width_, time_taken_average_width)
    # P_On = 0.5 sample_size
    plt.title("Width vs Time")
    plt.xlabel("Width")
    plt.ylabel("Average time taken")
    plt.show()
    plt.savefig("widthvstime")


if __name__ == "__main__":
    pass