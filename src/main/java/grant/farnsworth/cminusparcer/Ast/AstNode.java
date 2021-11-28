package grant.farnsworth.cminusparcer.Ast;

import java.util.ArrayList;

public abstract class AstNode {
	protected ArrayList<AstNode> Children;
	
	protected AstNode() {
		Children = new ArrayList<>();
	}
	
	public void addChildNode(AstNode an) {
		Children.add(an);
	}
	
	public ArrayList<AstNode> getChildren(){
		return Children;
	}
	
	abstract public TypeNode.type getType();
}
