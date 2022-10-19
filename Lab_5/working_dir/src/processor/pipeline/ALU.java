package processor.pipeline;


public class ALU{
	int operand1;
	int operand2;

	// settnig both the operands
	public void setop1(int operand1){
		this.operand1 = operand1;
	}
	public void setop2(int operand2){
		this.operand2 = operand2;
	}

	// evalutaing the result based on opcode and using operand1 and operand2 as input
	public int eval(String opcode){
		int res=0;
		switch(Integer.parseInt(opcode, 2)) 
		{
			case 0:{res=operand1+operand2;break;}
			case 1:{res=operand1+operand2;break;}
			case 2:{res=operand1-operand2;break;}
			case 3:{res=operand1-operand2;break;}
			case 4:{res=operand1*operand2;break;}
			case 5:{res=operand1*operand2;break;}
			case 6:{res=operand1/operand2;break;}
			case 7:{res=operand1/operand2;break;}
			case 8:{res=operand1 & operand2;break;}
			case 9:{res=operand1 & operand2;break;}
			case 10:{res=operand1 | operand2;break;}
			case 11:{res=operand1 |operand2;break;}
			case 12:{res=operand1 ^ operand2;break;}
			case 13:{res=operand1 ^ operand2;break;}
			case 14:{
				if(operand1<operand2){ res = 1;}
				else{res = 0;}
				break;
			}
			case 15:{
				if(operand1<operand2){ res = 1;}
				else{res = 0;}
				break;
			}
			case 16:{res=operand1 << operand2;break;}
			case 17:{res=operand1 << operand2;break;}
			case 18:{res=operand1 >>> operand2;break;}
			case 19:{res=operand1 >>>operand2;break;}
			case 20:{res=operand1 >> operand2;break;}
			case 21:{res=operand1 >> operand2;break;}
			case 22:{res=operand1 + operand2;break;}
			case 23:{res=operand1 + operand2;break;}
		}
		return res;
	}
}
