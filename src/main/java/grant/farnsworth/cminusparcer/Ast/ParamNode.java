package grant.farnsworth.cminusparcer.Ast;

import grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class ParamNode extends AstNode {
	public ParamNode() {
		super();
	}
	@Override
	public type getType() {
		// TODO Auto-generated method stub
		return this.Children.get(0).getType();
	}

}
