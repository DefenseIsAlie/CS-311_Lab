f = open("/home/abhishekj/labs/CS-311_Lab/Lab_6/working_dir/test_cases/descending.asm", "a")

import random

f.write("\n")

for i in range(300):
    f.write(f"    {random.randint(10, 500)}\n")