	.data
n:
	4
	.text
main:
	load %x0, $n, %x3
	subi %x3, 1, %x3	
	addi %x0, 0, %x4	
	addi %x0, 1, %x5	
	addi %x0, 65535, %x30	
	store %x4, 0, %x30	
	beq %x3, 0, finish	
	subi %x30, 1, %x30		
	store %x5, 0, %x30	
	subi %x30, 1, %x30
	addi %x0, 2, %x10
	jmp loop
loop:
	bgt %x10, %x3, finish
	add %x4, %x5, %x15
	store %x15, 0, %x30
	subi %x30, 1, %x30
	add %x0, %x5, %x4
	add %x0, %x15, %x5
	addi %x10, 1, %x10
	jmp loop
finish:
	end