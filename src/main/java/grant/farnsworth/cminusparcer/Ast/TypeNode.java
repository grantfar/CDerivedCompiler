package grant.farnsworth.cminusparcer.Ast;

public class TypeNode extends AstNode {
	private type nType;
	
	public TypeNode(type t) {
		super();
		nType = t;
	}
	
	public TypeNode(String s) {
		this(stringToType(s));
	}
	
	
	/*
	 * Static Section
	 */
	
	// FLOAT,INT, and CHAR must be first
	public static enum type{
		FLOAT, 
		INT,
		CHAR,
		VOID,
		STRING,
		ERROR
	}
	
	public static type stringToType(String s) {
		type returnType = null;
		switch(s) {
		case "int":
			returnType = type.INT;
			break;
		case "float":
			returnType = type.FLOAT;
			break;
		case "char":
			returnType = type.CHAR;
			break;
		case "void":
			returnType = type.VOID;
			break;
		default:
			System.err.println("Invalid Type: " + s);
			returnType = type.ERROR;
		}
		return returnType;
	}

	@Override
	public type getType() {
		return this.nType;
	}
	
}
