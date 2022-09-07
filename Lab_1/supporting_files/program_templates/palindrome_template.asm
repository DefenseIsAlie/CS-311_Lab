	.data
a:
	101
	.text
main:
	load %x0, $a, %x3
	load %x0, $a, %x4
	add  %x0, %x0, %x5
reversingloop:
	divi %x4, 10, %x4
	beq  %x4, %x0, comparenums
	add  %x5, %x31, %x5
	muli %x5, 10, %x5
	jmp reversingloop
comparenums:
	add %x31, %x5, %x5
	beq %x3, %x5, ispalindrome
	jmp isnotpalindrome
ispalindrome:
	addi %x0, 1, %x10
	end
isnotpalindrome:
	subi %x0, 1, %x10
	end