package grant.farnsworth.cminusparcer.Ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class CmAst extends AstNode{
	
	
	public CmAst() {
		super();
	}
	
	
	@Override
	public type getType() {
		return type.ERROR;
	}
}
