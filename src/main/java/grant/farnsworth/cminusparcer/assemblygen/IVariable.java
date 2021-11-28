package grant.farnsworth.cminusparcer.assemblygen;

import  grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public interface IVariable {
	public boolean inRegister();
	public Register getRegister();
	public void setRegister(Register register);
	public boolean Volitile();
	public String variableLocation();
	public type getType();
}
