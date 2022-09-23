package processor.pipeline;
import processor.Processor;
import generic.*;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public static String complement(StringBuffer str){
		int n = str.length();
        int i = n - 1;
        for (; i >= 0 ; i--) {
            if (str.charAt(i) == '1') break;
		}
    
        if (i == -1) return "1" + str;
    
        for (int k = i-1 ; k >= 0; k--) {   
            if (str.charAt(k) == '1') str.replace(k, k+1, "0");
            else str.replace(k, k+1, "1");
        }

        return str.toString();
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{	
			Instruction.OperationType[] op_codes = OperationType.values();
			Instruction ret_inst = new Instruction();
			int Inst = IF_OF_Latch.getInstruction();
			int Inst_type = Integer.parseInt(Integer.toBinaryString(Inst).substring(0, 5),2);
			OperationType OP = op_codes[Inst_type];
			ret_inst.setOperationType(op_codes[Inst_type]);

			Boolean nonImmediate = (OP == OperationType.add || OP == OperationType.sub || OP == OperationType.mul || OP == OperationType.div || OP == OperationType.and || OP == OperationType.or || OP == OperationType.xor || OP == OperationType.slt || OP == OperationType.sll || OP == OperationType.srl || OP == OperationType.sra);

			Boolean branchConditions = (OP == OperationType.beq || OP == OperationType.bgt || OP == OperationType.blt || OP == OperationType.bne);

			if (nonImmediate) { // shift right arithmetic
				String instruction = Integer.toBinaryString(Inst);

				Operand rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				int registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(registerNo);
				ret_inst.setSourceOperand1(rs1);

				Operand rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
				rs2.setValue(registerNo);
				ret_inst.setSourceOperand2(rs2);

				Operand rd = new Operand();
				rd.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(15, 20), 2);
				rd.setValue(registerNo);
				ret_inst.setDestinationOperand(rd);
			}else if(OP == OperationType.end){
				// do nothing is the operation type is end
			}else if (branchConditions){
				String instruction = Integer.toBinaryString(Inst);

				Operand rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				int registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(registerNo);
				ret_inst.setSourceOperand1(rs1);

				Operand rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
				rs2.setValue(registerNo);
				ret_inst.setSourceOperand2(rs2);

				Operand rd = new Operand();
				rd.setOperandType(OperandType.Immediate);
				String immediate = instruction.substring(15, 32);
				int immediate_value = Integer.parseInt(immediate,2);
				if(immediate.charAt(0) == '1'){
					StringBuffer conv = new StringBuffer(immediate);
					immediate = complement(conv);
					immediate_value = Integer.parseInt(immediate,2)*-1;
				}
				rd.setValue(immediate_value);
				ret_inst.setDestinationOperand(rd);
			} else if (OP == OperationType.jmp){
				String instruction = Integer.toBinaryString(Inst);

				Operand destination_op = new Operand();
				String immediate = instruction.substring(10, 32);
				int immediate_value = Integer.parseInt(immediate,2);
				if(immediate.charAt(0) == '1'){
					StringBuffer conv = new StringBuffer(immediate);
					immediate = complement(conv);
					immediate_value = Integer.parseInt(immediate,2)*-1;
				} 
				
				if (immediate_value != 0){
						destination_op.setOperandType(OperandType.Immediate);
						destination_op.setValue(immediate_value);
					}
				else{
						int registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
						destination_op.setOperandType(OperandType.Register);
						destination_op.setValue(registerNo);
					}
				ret_inst.setDestinationOperand(destination_op);
			}else{
				String instruction = Integer.toBinaryString(Inst);
				Operand rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				int registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(registerNo);
				ret_inst.setSourceOperand1(rs1);

				Operand rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
				rs2.setValue(registerNo);
				ret_inst.setSourceOperand2(rs2);

				Operand rd = new Operand();
				rd.setOperandType(OperandType.Immediate);
				
				String immediate = instruction.substring(15, 32);
				int immediate_value = Integer.parseInt(immediate,2);
				if(immediate.charAt(0) == '1'){
					StringBuffer conv = new StringBuffer(immediate);
					immediate = complement(conv);
					immediate_value = Integer.parseInt(immediate,2)*-1;
				}
				rd.setValue(immediate_value);
				ret_inst.setDestinationOperand(rd);
			}

			OF_EX_Latch.setInstruction(ret_inst);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}