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
	load %x0, $n, %x3
	add %x0, %x0, %x4
	add %x0, %x0, %x5
	addi %x0, 0, %x10
firstloop:
	beq %x4, %x3, exit
	jmp secondloop
continuefirstloop:
	add %x0, %x0, %x5
	addi %x4, 1, %x4
	jmp firstloop
secondloop:
	beq %x5, %x3, continuefirstloop
	load %x5, $a, %x6
	addi %x5, 1, %x7
	load %x7, $a, %x8
	bgt %x8, %x6, swap
continuesecondloop:
	addi %x5, 1, %x5
	jmp secondloop
swap:
	addi %x5, $a, %x20
	addi %x7, $a, %x21
	store %x8, 0, %x20
	store %x6, 0, %x21
	jmp continuesecondloop
exit:
	end