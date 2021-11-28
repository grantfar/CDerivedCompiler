package grant.farnsworth.cminusparcer.Ast;

import grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class ConstantNode extends AstNode{
	private String val;
	type cType;
	
	public ConstantNode(String val, type cType) {
		super();
		this.val = val;
		this.cType = cType;
	}

	
	public String getVal(){
		return val;
	}
	
	
	@Override
	public type getType() {
		return cType;
	}

}
