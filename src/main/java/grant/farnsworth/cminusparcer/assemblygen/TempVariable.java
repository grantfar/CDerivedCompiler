package grant.farnsworth.cminusparcer.assemblygen;

import grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class TempVariable implements IVariable {
	private Register location;
	private type vtype;
	
	public TempVariable() {}
	
	
	
	public TempVariable(Register location, type vtype) {
		super();
		this.location = location;
		this.vtype = vtype;
	}
		
	public Register getLocation() {
		return location;
	}



	public void setLocation(Register location) {
		this.location = location;
	}



	public type getVtype() {
		return vtype;
	}



	public void setVtype(type vtype) {
		this.vtype = vtype;
	}



	@Override
	public String variableLocation() {
		switch(vtype) {
		case FLOAT:
			return location.get64BitName();
		default:
			return location.get32BitName();
		}
	}

	@Override
	public type getType() {
		return vtype;
	}



	@Override
	public boolean inRegister() {
		return location != null;
	}



	@Override
	public Register getRegister() {
		return location;
	}



	@Override
	public void setRegister(Register register) {
		this.location = register;
		
	}



	@Override
	public boolean Volitile() {
		return false;
	}

}
