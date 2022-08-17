	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x30
	subi %x30, 1, %x30
	subi %30, 1, %x29
	sub %x0, 1, %x15
	sub %x0, 1, %x3
	jmp mainloop
mainloop:
	beq %x30, %x15, exitsuccess
	addi %x15, 1, %x15
	jmp steploop
steploop:
	beq %x29, %x3, mainloop
	addi %x3, 1, %x3
	addi %x3, 1, %x4
	load %x3, $a, %x10
	addi %x3, $a, %x7
	load %x4, $a, %x11
	addi %x4, $a, %x8
	blt  %x10, %x11, swap
	jmp steploop
swap:
	store %x3, 10, %x3
	store %x4, 10, %x4
	jmp steploop
exitsuccess:
	end