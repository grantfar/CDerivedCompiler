package grant.farnsworth.cminusparcer.Ast;

import grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class FactorNode extends AstNode {
	private factorT factorType;
	private IdentifierNode id;
	
	public FactorNode(factorT ft) {
		super();
		factorType = ft;
	}
	
	public FactorNode(factorT ft, IdentifierNode id) {
		this(ft);
		this.id = id;
	}
	@Override
	public type getType() {
		switch(factorType) {
		case not:
			if(this.Children.get(0).getType()!= type.INT)
				return type.ERROR;
			else
				return type.INT;
		case function:
			return id.getType();
		}
		return null;
	}
	
	public static enum factorT{
		not,
		function
	}
}
