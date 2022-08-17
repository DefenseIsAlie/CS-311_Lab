	.data
a:
	2
	.text
main:
	load %x0, $a, %x3
	addi %x0, 1, %x11
	addi %x0, 2, %x12
	beq %x3, %x0, notprime
	beq %x3, %x11, notprime
	beq %x3, %x12, prime
	addi %x0, 2, %x4
	jmp loop
loop:
	mul %x4, %x4, %x5
	bgt %x5, %x3, prime
	div %x3, %x4, %x6 
	mul %x6, %x4, %x7
	sub %x3, %x7, %x8
	beq %x8, 0, notPrime
	addi %x4, 1, %x4
	jmp loop
prime:
	addi %x0, 1, %x10
	end
notprime:
	subi %x0, 1, %x10
	end