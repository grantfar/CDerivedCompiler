package grant.farnsworth.cminusparcer.assemblygen;

import  grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class GlobalVariable implements IVariable {
	private String name;
	private type varType;
	private Register inRegister;
	
	
	public GlobalVariable(type varType,String name) {
		super();
		this.name = name;
		this.varType = varType;
	}




	@Override
	public String variableLocation() {
		return name + "(%rip)";
	}
	
	@Override
	public type getType() {
		return varType;
	}



	@Override
	public boolean inRegister() {
		return inRegister != null;
	}



	@Override
	public Register getRegister() {
		return inRegister;
	}



	@Override
	public boolean Volitile() {
		return true;
	}


	@Override
	public void setRegister(Register register) {
		this.inRegister = register;
	}

}
