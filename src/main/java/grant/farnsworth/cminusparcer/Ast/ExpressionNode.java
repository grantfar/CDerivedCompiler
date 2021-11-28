package grant.farnsworth.cminusparcer.Ast;

import java.util.HashMap;

import grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class ExpressionNode extends AstNode {
	
	private opperator op;
	
	public ExpressionNode(opperator op) {
		super();
		this.op = op;
	}
	
	public opperator getOpperator() {
		return this.op;
	}
	
	public static enum opperator{
		LT,
		GT,
		LE,
		GE,
		EQ,
		NE,
		NOT,
		OR,
		AND,
		PLUS,
		MINUS,
		TIMES,
		DIVIDE
	}
	
	@Override
	public type getType() {
		type leftType = Children.get(0).getType();
		type rightType = Children.get(1).getType();
		return calcType(leftType,rightType,this.op);
	}
	
	public static type calcType(type t1, type t2, opperator op) {
		if(op == opperator.MINUS || op == opperator.PLUS|| op == opperator.TIMES || op == opperator.DIVIDE) {
			if((t1 != type.FLOAT && t1 != type.INT && t1!= type.ERROR) || 
					(t2 != type.FLOAT && t2 != type.INT && t2 != type.ERROR))
				return type.ERROR;
			else if(t1 == type.FLOAT || t2 == type.FLOAT)
				return type.FLOAT;
			else
				return type.INT;
		}
		else {
			
			if(
					//one char but not both
					(((t1 == type.CHAR || t1 == type.ERROR) ^ (t2 == type.CHAR || t2 == type.ERROR))&&(t1 != type.ERROR || t2 != type.ERROR)) ||
					//t1 wrong type
					((t1 != type.CHAR && t1 != type.INT && t1 != type.FLOAT && t1 != type.ERROR)||
							//t2 wrong type
							(t2 != type.CHAR && t2 != type.INT && t2 != type.FLOAT && t2 != type.ERROR)))
				return type.ERROR;
			return type.INT;
		}
	}
}
