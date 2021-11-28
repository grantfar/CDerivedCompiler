package grant.farnsworth.cminusparcer.assemblygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import grant.farnsworth.cminusparcer.Ast.ConstantNode;
import grant.farnsworth.cminusparcer.Ast.ExpressionNode;
import grant.farnsworth.cminusparcer.Ast.TypeNode.type;
import grant.farnsworth.cminusparcer.Ast.VariableNode;
public class AssemblyCommand {
	
	static int stringConstPostfix;
	static int floatConstPostfix;
	static Stack<Stack<Map<String,IVariable>>> StackVarLocation;
	static Map<String,IVariable> GlobalVarLocation;
	static Stack<Integer> stackSize;
	static Stack<Integer> padding;
	static List<Register> freeRegisters;
	static Map<String,String> constants;
	static Map<Register,IVariable> registerAlocMap;

	public static void init() {
		stringConstPostfix = 0;
		floatConstPostfix = 0;
		StackVarLocation = new Stack<>();
		freeRegisters = Register.getNonReservedRegisters();
		registerAlocMap= new HashMap<>();
		stackSize = new Stack<>();
		padding = new Stack<>();
		constants = new HashMap<>();
	}
	
	
	public static void pushFunStack() {
		StackVarLocation.add(new Stack<Map<String,IVariable>>());
		stackSize.push(0);
	}
	
	public static void pushStack() {
		StackVarLocation.peek().push(new HashMap<>());
	}
	
	public static void popFunStack() {
		StackVarLocation.pop();
		stackSize.pop();
	}
	
	public static void popStack() {
		StackVarLocation.peek().pop();
	}
	
	
	public static IVariable findVar(String s) {
		Stack<Map<String, IVariable>> lStack = StackVarLocation.peek();
		for(int i = lStack.size() -1; i >= 0; i++) {
			if(lStack.get(i).containsKey(s)) {
				return lStack.get(i).get(s);
			}
		}
		if(GlobalVarLocation.containsKey(s)) {
			return GlobalVarLocation.get(s);
		}
		return null;
	}
	
	private static String findLocation(String s) {
		IVariable tmp = findVar(s);
		if(tmp == null) {
			return null;
		}
		return tmp.variableLocation();
	}
	
	
	public static List<String> freeRegister(IVariable var,boolean swapRegisters){
		List<String> ret = new LinkedList<>();
		Register r = var.getRegister();
		
		if(swapRegisters) {
			String tmp = assignGPRegister(var);
			if(tmp != null)
				ret.add(tmp);
			switch(var.getType()) {
			case INT:
				ret.add("xchgl %" + r.get32BitName() 
				+ ", %" + var.getRegister().get32BitName());
				break;
			case FLOAT:
				ret.add("xchgq %" + r.get64BitName() 
				+ ", %" + var.getRegister().get64BitName());
			case CHAR:
				ret.add("xchgb %" + r.get32BitName() 
				+ ", %" + var.getRegister().get32BitName());
			default:
				break;
			}
		}
		else {
			switch(var.getType()) {
			case INT:
				ret.add("movl %" + var.getRegister().get32BitName() + 
				", " + var.variableLocation());
				break;
			case FLOAT:
				ret.add("movq %" + var.getRegister().get64BitName() +
				", " + var.variableLocation());
			case CHAR:
				ret.add("movb %" + var.getRegister().get32BitName() + 
				", " + var.variableLocation());
			default:
				break;
			}
		}
		registerAlocMap.remove(r);
		return ret;
	}
	
	public static List<String> freeRegister(Register r, boolean swapRegisters){
		
		return freeRegister(registerAlocMap.get(r),swapRegisters);	
		
	}
	
	private static String assignGPRegister(IVariable v) {
		String ret = null;
		if(freeRegisters.size()>0) {
			v.setRegister(freeRegisters.get(0));
			registerAlocMap.put(freeRegisters.get(0), v);
			freeRegisters.remove(0);
		}
		
		for(IVariable iv : registerAlocMap.values()){
			if(!(iv instanceof TempVariable)) {
				switch(iv.getType()) {
				case INT:
					ret = "movl %" + iv.getRegister().get32BitName() + 
					", " + iv.variableLocation();
					break;
				case FLOAT:
					ret = "movq %" + iv.getRegister().get64BitName() +
					", " + iv.variableLocation();
				case CHAR:
					ret = "movb %" + iv.getRegister().get32BitName() + 
					", " + iv.variableLocation();
				default:
					break;
				}
				v.setRegister(iv.getRegister());
				registerAlocMap.replace(iv.getRegister(), v);
				iv.setRegister(null);
				break;
			}
		}
		return ret;
	}
	
	private static int calcFrameSize(int size) {
		int req = size - padding.peek();
		int ret = (req + (16 - (req%16)));
		padding.pop();
		padding.push(ret-req);
		return ret;
	}
	
	public static List<String> methodStart() {
		ArrayList<String> ret = new ArrayList<>();
		ret.add("pushq %rbp");
		ret.add("movq %rsp, %rbp");
		return ret;
	}
	
	
	public static List<String> methodEnd() {
		ArrayList<String> ret = new ArrayList<>();
		ret.add("leave");
		ret.add("ret");
		return ret;
	}
	
	public static List<String> assignment(IVariable src,IVariable dst){
		List<String> ret = new LinkedList<>();
		
		if(!src.inRegister() && !dst.inRegister()) {
			String tmp = assignGPRegister(dst);
			if(tmp != null)
				ret.add(tmp);
		}
		
		
		if(dst.inRegister() && !dst.Volitile() && src.inRegister()) {
			switch(src.getType()) {
			
			case CHAR:
				ret.add("movb %" + 
						src.getRegister().get32BitName() + 
						", %" + dst.getRegister().get32BitName());
			case INT:
				ret.add("movl %" + 
						src.getRegister().get32BitName() + 
						", %" + dst.getRegister().get32BitName());
				break;
			case FLOAT:
				ret.add("movq %" + 
						src.getRegister().get64BitName() + 
						", %" + dst.getRegister().get64BitName());
				break;
			default:
				break;
			}
		}
		
		else if(src.inRegister() && (!dst.inRegister() || dst.Volitile())) {
			
			if(dst.inRegister()) {
				freeRegisters.add(dst.getRegister());
				dst.setRegister(null);
			}
			
			switch(src.getType()) {
			
			case CHAR:
				ret.add("movb %" + 
						src.getRegister().get32BitName() + 
						", " + dst.variableLocation());
			case INT:
				ret.add("movl %" + 
						src.getRegister().get32BitName() + 
						", " + dst.variableLocation());
				break;
			case FLOAT:
				ret.add("movq %" + 
						src.getRegister().get64BitName() + 
						", " + dst.variableLocation());
				break;
			default:
				break;
			}
		}
		
		else{
			
			switch(src.getType()) {
			
			case CHAR:
				ret.add("movb " + 
						src.variableLocation() + 
						", %" + dst.getRegister().get32BitName());
			case INT:
				ret.add("movl " + 
						src.variableLocation() + 
						", %" + dst.getRegister().get32BitName());
				break;
			case FLOAT:
				ret.add("movq " + 
						src.variableLocation() + 
						", %" + dst.getRegister().get64BitName());
				break;
			default:
				break;
			}
			if(dst.Volitile()) {
				freeRegister(dst,false);
			}
		}
		
		return ret;
	}
	public static List<String> assignment(IVariable src,String dst){
		return assignment(src,findVar(dst));	
	}
	
	public static List<String> assignment(String src,String dst){
		return assignment(findVar(src),findVar(dst));	
	}
	
	public static List<String> assignmentc(String constant,String dst, type t){
		List<String> ret = new LinkedList<>();
		IVariable dest = findVar(dst);
		
		switch(t) {
		case CHAR:
			if(dest.inRegister())
				ret.add("movb $" + 
					((int)constant.charAt(0)) + 
					", %" + dest.getRegister().get32BitName()); 
			else
				ret.add("movb $" + 
						((int)constant.charAt(0)) + 
						", " + dest.variableLocation());
		case INT:
			if(dest.inRegister())
				ret.add("movl $" + 
					constant + 
					", %" + dest.getRegister().get32BitName());
			else
				ret.add("movl $" + 
						constant + 
						", " + dest.variableLocation());
			break;
		case FLOAT:
			if(dest.inRegister())
				ret.add("movq " + 
					constants.get(constant) + 
					", %" + dest.getRegister().get64BitName());
			else
				ret.add("movq " + 
						constants.get(constant) + 
						", " + dest.variableLocation());
			break;
		default:
			break;
		}
		return ret;
	}
	
	public static String intToDouble(Register in,Register out){
		return "cvtsi2sdl %" + in.get32BitName() + ", %" + out.get64BitName();
	}
	
	public static List<String> ExpressionEvaluation(ExpressionNode node, TempVariable Out) {
		LinkedList<String> ret = new LinkedList<>();
		
		IVariable right = null;
		IVariable left = null;
		
		if(node.getChildren().get(0) instanceof ExpressionNode) {
			left = new TempVariable();
			ret.addAll(ExpressionEvaluation((ExpressionNode)node.getChildren().get(0),(TempVariable)right));
		}
		
		else if(node.getChildren().get(0) instanceof VariableNode) {
			VariableNode tmp = (VariableNode) node.getChildren().get(0);
			left = findVar(tmp.getDeclaration().getId());
		}
		
		if(node.getChildren().get(1) instanceof ExpressionNode) {
			right = new TempVariable();
			ret.addAll(ExpressionEvaluation((ExpressionNode)node.getChildren().get(1),(TempVariable)left));
		}
		
		
		else if(node.getChildren().get(1) instanceof VariableNode) {
			VariableNode tmp = (VariableNode) node.getChildren().get(1);
			right = findVar(tmp.getDeclaration().getId());
		}
		
		
		
		return ret;
	}

	
	
	public static List<String> stringPrint(String s){
		ArrayList<String> ret = new ArrayList<>();
		ret.add("leaq " + findLocation(s) + ", %rdi");
		ret.add("movl $0, %eax");
		ret.add("call printf");
		
		return ret;
	}
	
	public static List<String> intPrint(String s){
		ArrayList<String> ret = new ArrayList<>();
		String val = "$" + s;
		try {
			Integer.parseInt(s);
		}
		catch(NumberFormatException e) {
			val = findLocation(s);
		}
		
		ret.add("movl " + val + ", %ebx");
		ret.add("movl %ebx, %esi");
		ret.add("leaq .int_wformat(%rip), %rdi");
		ret.add("movl $0, %eax");
		ret.add("call printf");
		
		return ret;
	}
	
	public static List<String> intRead(String s){
		ArrayList<String> ret = new ArrayList<>();
		String val = s;
		try {
			Integer.parseInt(s);
		}
		catch(NumberFormatException e) {
			val = findLocation(s);
		}
		
		ret.add("movl " + val + ", %rbx");
		ret.add("movl %ebx, %esi");
		ret.add("leaq .int_rformat(%rip), %rdi");
		ret.add("movl $0, %eax");
		ret.add("call printf");
		
		return ret;
	}
	
	public static String stringConst(String s) {
		if(!constants.containsKey(s)) {
			String stringConst = ".string_const" + (stringConstPostfix++);
			constants.put(s,stringConst +"(%rip)");
			return stringConst +": .string \"" + s + "\"";
		}
		return null;
	}
	
	public static String floatConst(String s) {
		if(!constants.containsKey(s)) {
			String floatConst = ".float" + (floatConstPostfix++);
			constants.put(s, floatConst + "(%rip)");
			return floatConst + ": .float " + 
			Long.toString(Double.doubleToRawLongBits(3.2));
		}
		return null;
	}
	
	public static String localVar(List<StackVariable> stackVar) {
		int size = 0;
		for(StackVariable v: stackVar) {
			int tmp = 0;
			switch(v.getType()) {
			case INT:
				tmp = 4;
				break;
			
			case FLOAT:
				tmp = 8;
				break;
			
			case CHAR:
				tmp = 1;
				break;
			
			default:
				System.err.printf("%s is not a valid variable type", v.getType().name());
				break;
			}
			v.setStackOffset(-tmp - stackSize.peek());
			stackSize.push(stackSize.pop() + tmp);
			size += tmp;
			StackVarLocation.peek().peek().put(v.getName(), v);
		}
		return "subq $" 
	+ calcFrameSize(size)
	+ ", %rsp";
	}
	
	
	public static String globalVar(String name,type t) {
		String retComm = ".comm "+ name;
		GlobalVariable v = new GlobalVariable(t,name);
		GlobalVarLocation.put(name, v);
		
		switch(t) {
		case INT:
			retComm += ", 4, 4";
			break;
		
		case FLOAT:
			retComm += ", 8, 8";
			break;
		
		case CHAR:
			retComm += ", 1, 1";
			break;
		
		default:
			retComm = null;	
		}
		
		return retComm;
	}
}
