package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.ExecutionCompleteEvent;
import generic.Simulator;
import generic.Statistics;
import processor.Clock;
import processor.Processor;

public class Execute implements Element {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	ALU aluObject = new ALU();
	co_unit controlunit = new co_unit();
	boolean is_end = false;
	public void event_scheduler(String opcode, Execute execute){
		long lat;
		switch(opcode) {
			case "00100":
			case "00101":
				lat=Configuration.multiplier_latency;
				break;
			case "00110":
			case "00111":
				lat=Configuration.divider_latency;
				break;
			default:
				lat=Configuration.ALU_latency;
				break;
		}
		Simulator.getEventQueue().addEvent(
				new ExecutionCompleteEvent(
						Clock.getCurrentTime()+lat,
						execute,execute));
		OF_EX_Latch.setEX_busy(true);
	}
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	public void setEnableDisable()
	{
		EX_IF_Latch.setIF_enable(true);
		containingProcessor.getOFUnit().IF_OF_Latch.OF_enable=false;
		containingProcessor.getOFUnit().hasEnded = false;
		containingProcessor.getIFUnit().IF_EnableLatch.IF_enable = false;
		containingProcessor.getIFUnit().hasEnded = false;
		OF_EX_Latch.setEX_enable(false);
	}
	public void setDefaults()
	{
		controlunit.opcode="";
		controlunit.rs1="";
		controlunit.rs2="";
		controlunit.rd="";
		controlunit.Imm = "";
	}
	public void performEX() {
		if(OF_EX_Latch.isEX_enable() && !is_end){
			int isBranchTaken = 0, branchedPC = 0;
			if(OF_EX_Latch.isEX_busy()){
				return;
			}
			int I = OF_EX_Latch.getInstruction();
			controlunit.setInstruction(I);
			EX_MA_Latch.setInstruction(I);
			int op1 = OF_EX_Latch.getoperand1() ;
			int op2 =  OF_EX_Latch.getoperand2();
			String opcode = controlunit.opcode;
			int imm = OF_EX_Latch.getimmx();
			int ALUres = 0;
			if(controlunit.isimm() ){
				aluObject.setop1(op1);
				aluObject.setop2(imm);
				ALUres=aluObject . eval(opcode);
				if(opcode.equals("00111")) {
					containingProcessor.getRegisterFile().setValue(31, op1%imm);
				}
				EX_MA_Latch.setop2(op2);
				EX_MA_Latch.setaluRes(ALUres);
				event_scheduler(opcode,this);
			}
			else if(!opcode.equals("11000") && !opcode.equals("11001") && !opcode.equals("11010") && !opcode.equals("11011") && !opcode.equals("11100") && !opcode.equals("11101")){
				aluObject.setop1(op1);
				aluObject.setop2(op2);
				ALUres=aluObject . eval(opcode);
				if(opcode.equals("00110")) containingProcessor.getRegisterFile().setValue(31, op1%op2);
				EX_MA_Latch.setaluRes(ALUres);
				event_scheduler(opcode,this);
			} else {
				switch(Integer.parseInt(controlunit.opcode, 2)){
					case 24:{
						isBranchTaken= 1;
						branchedPC = OF_EX_Latch.getbranchtarget();
						break;
					}
					case 25:{
						if(op1 == op2){
							isBranchTaken=1;
							branchedPC = OF_EX_Latch.getbranchtarget();
						}
						break;
					}
					case 26:{
						if(op1 != op2){
							isBranchTaken= 1;
							branchedPC = OF_EX_Latch.getbranchtarget();
						}
						break;
					}
					case 27:{
						if(op1 <op2){
							isBranchTaken= 1;
							branchedPC = OF_EX_Latch.getbranchtarget();
						}
						break;
					}
					case 28:{
						if(op1>op2){
							isBranchTaken= 1;
							branchedPC = OF_EX_Latch.getbranchtarget();
						}
						break;
					}
					case 29:{
						is_end = true;
						
						EX_MA_Latch.setMA_enable(true);
					}
				}
			}
			if(isBranchTaken==1) {
				Statistics.controlHazard +=1; // every time branch is taken, controlHazard increments by 1
				EX_IF_Latch.setisbranchtaken();
				EX_IF_Latch.setbranchtarget(branchedPC);
				setEnableDisable();
				Simulator.getEventQueue().deleteeventqueue(Clock.getCurrentTime());
			} else {
				EX_MA_Latch.setrd(OF_EX_Latch.getrd());
			}
		}
		else {
			setDefaults();
		}
	}
	@Override
	public void handleEvent(Event event) {
		if(EX_MA_Latch.isMA_busy()) {
			event.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(event);
		}
		else {
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);
			OF_EX_Latch.setEX_busy(false);
		}	
	}
}