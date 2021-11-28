package grant.farnsworth.cminusparcer.Ast;

import grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class DeclarationNode extends AstNode {

	public DeclarationNode() {
		super();
	}
	
	
	@Override
	public type getType() {
		//typeNode location
		return this.Children.get(0).getType();
	}

}
