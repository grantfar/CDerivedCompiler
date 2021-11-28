package grant.farnsworth.cminusparcer.Ast;

import grant.farnsworth.cminusparcer.Ast.TypeNode.type;

public class IdentifierNode extends AstNode {
	private String id;
	private type DeclaredType;
	private boolean array;
	private boolean function;
	private int arraySize;
	
	public IdentifierNode(String id) {
		super();
		this.id = id;
		this.array = false;
		this.function = false;
	}
	
	public IdentifierNode(String id,int arraySize) {
		super();
		this.id = id;
		this.array = true;
		this.function = false;
		this.arraySize = arraySize;
	}
	public IdentifierNode(String id,boolean function) {
		super();
		this.id = id;
		this.array = false;
		this.function = true;
		this.arraySize = arraySize;
	}
	public void setType(type t) {
		this.DeclaredType = t;
	}
	@Override
	public type getType() {
		return DeclaredType;
	}
	public boolean isArray() {
		return array;
	}
	
	public String getId() {
		return this.id;
	}

	public boolean isFunction() {
		return function;
	}

}
