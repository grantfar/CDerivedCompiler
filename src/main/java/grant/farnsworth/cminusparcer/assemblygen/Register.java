package grant.farnsworth.cminusparcer.assemblygen;

import java.util.LinkedList;
import java.util.List;

public class Register {
	private static Register[] gpRegisters;
	private static Register[] floatRegisters;
	private String bit32Name;
	private String bit64Name;
	
	public Register(String bit32Name, String bit64Name) {
		super();
		this.bit32Name = bit32Name;
		this.bit64Name = bit64Name;
	}


	public String get32BitName() {
		return bit32Name;
	}

	public String get64BitName() {
		return bit64Name;
	}
	
	public static Register eax() { return gpRegisters[0]; } 
	public static Register rax() { return gpRegisters[0]; }
	public static Register ebx() { return gpRegisters[1]; }
	public static Register rbx() { return gpRegisters[1]; }
	public static Register ecx() { return gpRegisters[2]; }
	public static Register rcx() { return gpRegisters[2]; }
	public static Register edx() { return gpRegisters[3]; }
	public static Register rdx() { return gpRegisters[3]; }
	public static Register esi() { return gpRegisters[4]; }
	public static Register rsi() { return gpRegisters[4]; }
	public static Register edi() { return gpRegisters[5]; }
	public static Register rdi() { return gpRegisters[5]; }
	public static Register r8d() { return gpRegisters[6]; }
	public static Register r8() { return gpRegisters[6]; }
	public static Register r9d() { return gpRegisters[7]; }
	public static Register r9() { return gpRegisters[7]; }
	public static Register r10d() { return gpRegisters[8]; }
	public static Register r10() { return gpRegisters[8]; }
	public static Register r11d() { return gpRegisters[9]; }
	public static Register r11() { return gpRegisters[9]; }
	public static Register r12d() { return gpRegisters[10]; }
	public static Register r12() { return gpRegisters[10]; }
	public static Register r13d() { return gpRegisters[11]; }
	public static Register r13() { return gpRegisters[11]; }
	public static Register r14d() { return gpRegisters[12]; }
	public static Register r14() { return gpRegisters[12]; }
	public static Register r15d() { return gpRegisters[13]; }
	public static Register r15() { return gpRegisters[13]; }
	public static Register xmm0() { return floatRegisters[0]; }
	public static Register xmm1() { return floatRegisters[1]; }
	public static Register xmm2() { return floatRegisters[2]; }
	public static Register xmm3() { return floatRegisters[3]; }
	public static Register xmm4() { return floatRegisters[4]; }
	public static Register xmm5() { return floatRegisters[5]; }
	public static Register xmm6() { return floatRegisters[6]; }
	public static Register xmm7() { return floatRegisters[7]; }
	public static Register xmm8() { return floatRegisters[8]; }
	public static Register xmm9() { return floatRegisters[9]; }
	public static Register xmm10() { return floatRegisters[10]; }
	public static Register xmm11() { return floatRegisters[11]; }
	public static Register xmm12() { return floatRegisters[12]; }
	public static Register xmm13() { return floatRegisters[13]; }
	public static Register xmm14() { return floatRegisters[14]; }
	public static Register xmm15() { return floatRegisters[15]; }
	
	public static List<Register> getFloatRegisters(){
		List<Register> ret = new LinkedList<>();
		ret.add(new Register("%xmm0","%xmm0"));
		ret.add(new Register("%xmm1","%xmm1"));
		ret.add(new Register("%xmm2","%xmm2"));
		ret.add(new Register("%xmm3","%xmm3"));
		ret.add(new Register("%xmm4","%xmm4"));
		ret.add(new Register("%xmm5","%xmm5"));
		ret.add(new Register("%xmm6","%xmm6"));
		ret.add(new Register("%xmm7","%xmm7"));
		ret.add(new Register("%xmm8","%xmm8"));
		ret.add(new Register("%xmm9","%xmm9"));
		ret.add(new Register("%xmm10","%xmm10"));
		ret.add(new Register("%xmm11","%xmm11"));
		ret.add(new Register("%xmm12","%xmm12"));
		ret.add(new Register("%xmm13","%xmm13"));
		ret.add(new Register("%xmm14","%xmm14"));
		ret.add(new Register("%xmm15","%xmm15"));

		floatRegisters = (Register[]) ret.toArray();
		return ret;
	}
	
	public static List<Register> getNonReservedRegisters(){
		List<Register> ret = new LinkedList<>();
		ret.add(new Register("%eax","%rax"));
		ret.add(new Register("%ebx","%rbx"));
		ret.add(new Register("%ecx","%rcx"));
		ret.add(new Register("%edx","%rdx"));
		ret.add(new Register("%esi","%rsi"));
		ret.add(new Register("%edi","%rdi"));
		ret.add(new Register("%r8d","%r8"));
		ret.add(new Register("%r9d","%r9"));
		ret.add(new Register("%r10d","%r10"));
		ret.add(new Register("%r11d","%r11"));
		ret.add(new Register("%r12d","%r12"));
		ret.add(new Register("%r13d","%r13"));
		ret.add(new Register("%r14d","%r14"));
		ret.add(new Register("%r15d","%r15"));
		gpRegisters = (Register[]) ret.toArray();
		return ret;
	}
}
