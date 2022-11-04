import matplotlib.pyplot as plt
import  numpy as np


plot1fib = np.array([[0 ,0.019328], [16, 0.01725], [128, 0.0566], [512, 0.07013], [1024, 0.0655]])
plot1des = np.array([[ 0, 0.014695], [ 16, 0.01345], [128, 0.0523], [512, 0.0701], [1024, 0.0712]])

print(plot1fib[:,0])

fig, ax = plt.subplots()
ax.plot( plot1fib[:,0],  plot1fib[:,1], label="fibonacci")
ax.plot( plot1des[:,0],  plot1des[:,1], label="descending")
ax.set_xlabel('cache size')
ax.set_ylabel('IPC')
ax.set_title('Vary L1d size')
ax.legend()
fig.savefig("L1d.jpeg")


plot2fib = np.array([[0 ,0.019328], [16, 0.0349], [128, 0.0566], [512, 0.0567], [1024, 0.0565]])
plot2des = np.array([[ 0, 0.014695], [ 16, 0.0475], [128, 0.0523], [512, 0.0531], [1024, 0.0512]])

fig, ax = plt.subplots()
ax.plot( plot2fib[:,0],  plot2fib[:,1], label="fibonacci")
ax.plot( plot2des[:,0],  plot2des[:,1], label="descending")
ax.set_xlabel('cache size')
ax.set_ylabel('IPC')
ax.set_title('Vary L1i size')
ax.legend()
fig.savefig("L1i.jpeg")