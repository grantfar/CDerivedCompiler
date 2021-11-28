package grant.farnsworth.cminusparcer.assemblygen;

import  grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class StackVariable implements IVariable{
	private type variableType;
	private String name;
	private boolean aliased;
	private Register inRegister;
	private int stackOffset;
	
	public StackVariable(type variableType, String name) {
		super();
		this.variableType = variableType;
		this.name = name;
	}
	
	public StackVariable(type variableType, String name, boolean aliased) {
		this(variableType, name);
		this.aliased = aliased;
	}

	public boolean isAliased() {
		return aliased;
	}

	public void setAliased(boolean aliased) {
		this.aliased = aliased;
	}

	public Register getInRegister() {
		return inRegister;
	}

	public void setInRegister(Register inRegister) {
		this.inRegister = inRegister;
	}

	public int getStackOffset() {
		return stackOffset;
	}

	public void setStackOffset(int stackOffset) {
		this.stackOffset = stackOffset;
	}


	public String getName() {
		return name;
	}

	@Override
	public String variableLocation() {
		if(inRegister != null) {
			switch(variableType) {
			case INT:
				return inRegister.get32BitName();
			case FLOAT:
				return inRegister.get64BitName();
			case CHAR:
				return inRegister.get32BitName();
			default:
				return null;
			}
		}
		return Integer.toString(stackOffset) + "(%rbp)";
	}

	@Override
	public type getType() {
		return variableType;
	}

	@Override
	public boolean inRegister() {
		return this.inRegister == null;
	}

	@Override
	public Register getRegister() {
		return this.inRegister;
	}

	@Override
	public void setRegister(Register register) {
		this.inRegister = register;
		
	}

	@Override
	public boolean Volitile() {
		return this.aliased;
	}	
	
}
