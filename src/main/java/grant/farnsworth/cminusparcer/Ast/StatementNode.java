package grant.farnsworth.cminusparcer.Ast;

import java.util.HashMap;

import grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class StatementNode extends AstNode {
	private statementType sType;
	
	public StatementNode(statementType sType) {
		super();
		this.sType = sType;
	}
	
	public statementType getStatementType() {
		return sType;
	}

	@Override
	public type getType() {
		return type.ERROR;
	}
	
	public static enum statementType{
		assignment,
		callstatement,
		ifstatement, 
		whilestatement,
		writestatement,
		readstatement,
		returnstatement, 
		exitstatement,
		cpdstatement
	}
}
