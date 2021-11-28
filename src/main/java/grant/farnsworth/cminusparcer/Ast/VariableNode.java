package grant.farnsworth.cminusparcer.Ast;

import grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class VariableNode extends AstNode{
	private IdentifierNode declaration;

	public IdentifierNode getDeclaration() {
		return declaration;
	}

	public VariableNode(IdentifierNode dec) {
		super();
		this.declaration = dec;
	}
	
	@Override
	public type getType() {
		// TODO Auto-generated method stub
		return declaration.getType();
	}

}
