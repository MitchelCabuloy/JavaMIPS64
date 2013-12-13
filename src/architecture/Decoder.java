package architecture;

public class Decoder {
	public static int decode(String code){
		int result = 0b00000000000000000000000000000000;
		String type = "";
		
		int opcode_size = 6;
		int rs_size = 5;
		int rt_size = 5;
		int rd_size = 5;
		int zero_size = 5;
		int func_size = 6;
		int immediate_size = 16;
		int offset_size = 26;
		
		int IR_size = 32;
		
		int i = 0;
		int command_opcode = 0;
		int command_func = 0;
		int rs = 0;
		int rt = 0;
		int rd = 0;
		int imm = 0;
		int label = 0;
		int zero = 0;
		
		String delims = "[, ()#]+";
		String[] tokens = code.split(delims);
		
		//To-do While loop
		switch(tokens[i])
		{
			case "DADD": 	command_func = 44;
							type = "R";
						 	break;
						  
			case "DSUB": 	command_func = 46;
							type = "R";
						 	break;
			
			case "OR": 		command_func = 37;
							type = "R";
					   		break;  
				
			case "XOR": 	command_func = 38;
							type = "R";
							break;
				
			case "SLT": 	command_func = 42;
							type = "R";
							break;
						
			case "BNEZ": 	command_opcode = 5;
							type = "I";
						 	break;
			
			case "LD": 		command_opcode = 55;
							type = "I";
					   		break;
			
			case "SD": 		command_opcode = 63;
							type = "I";
					   		break;
			
			case "DADDI": 	command_opcode = 24;
							type = "I";
						  	break;
			
			case "J": 		command_opcode = 2;
							type = "J";
					  		break;
							
			default: System.out.println("Command not found");
		}
		
		switch(type){
			case "R": 	if(tokens.length == 4){
							for(i += 1; i < tokens.length; i++){
								if(tokens[i].contains("R")){
									if(i == 1)
										rd = Integer.parseInt(tokens[i].substring(1));
									
									else if(i == 2)
										rs = Integer.parseInt(tokens[i].substring(1));
									
									else if(i == 3)
										rt = Integer.parseInt(tokens[i].substring(1));
								}
									
							}
							
							result |= (command_opcode << IR_size - opcode_size); 
							IR_size -= opcode_size;
							result |= (rs << IR_size - rs_size); 
							IR_size -= rs_size;
							result |= (rt << IR_size - rt_size);
							IR_size -= rt_size;
							result |= (rd << IR_size - rd_size); 
							IR_size -= rd_size;
							result |= (zero << IR_size - zero_size);
							IR_size -= zero_size;
							result |= (command_func << IR_size - func_size);
							IR_size -= func_size;
						}
						
						break;
			
			case "I": 	if(tokens.length == 3){
							for(i += 1; i < tokens.length; i++){
								if(tokens[i].contains("R")){
									if(i == 1)
										rd = Integer.parseInt(tokens[i].substring(1));
									
									else if(i == 2)
										rs = Integer.parseInt(tokens[i].substring(1));
									
									else if(i == 3)
										imm = Integer.parseInt(tokens[i].substring(1));
								}
								else if(Integer.parseInt(tokens[i]) > 0){
									imm = Integer.parseInt(tokens[i]);
								}
							}
						}
						else if(tokens.length == 4 && tokens[i].equals("DADDI")){
							for(i += 1; i < tokens.length; i++){
								if(tokens[i].contains("R")){
									if(i == 1)
										rd = Integer.parseInt(tokens[i].substring(1));
									
									else if(i == 2)
										rs = Integer.parseInt(tokens[i].substring(1));
								}
								else if(Integer.parseInt(tokens[i]) > 0){
									imm = Integer.parseInt(tokens[i]);
								}	
							}
						}
			
						result |= (command_opcode << IR_size - opcode_size); 
						IR_size -= opcode_size;
						result |= (rs << IR_size - rs_size); 
						IR_size -= rs_size;
						result |= (rd << IR_size - rt_size);
						IR_size -= rt_size;
						result |= (imm << IR_size - immediate_size);
						IR_size -= immediate_size;
						break;
			
			case "J": 	if(tokens.length == 2){
							for(i += 1; i < tokens.length; i++){
								if(tokens[i].contains("L")){
									label = Integer.parseInt(tokens[i].substring(1));
								}
									
							}
							
							result |= (command_opcode << IR_size - opcode_size); 
							IR_size -= opcode_size;
							result |= (label << IR_size - offset_size);
							IR_size -= offset_size;
						}
						break;
		}
		
		return result;
	}
}
