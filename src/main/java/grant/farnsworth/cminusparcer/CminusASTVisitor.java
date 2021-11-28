package grant.farnsworth.cminusparcer;

import java.util.HashMap;
import java.util.Stack;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import grant.farnsworth.cminusparcer.Ast.*;
import grant.farnsworth.cminusparcer.Ast.ExpressionNode.opperator;
import grant.farnsworth.cminusparcer.Ast.FactorNode.factorT;
import grant.farnsworth.cminusparcer.Ast.TypeNode.type;
import grant.farnsworth.cminusparcer.CminusParser.*;

public class CminusASTVisitor extends AbstractParseTreeVisitor<AstNode> implements CminusVisitor<AstNode> {

	private static interface iFold{
		ConstantNode fold(ConstantNode n1, ConstantNode n2, opperator op);
	}
	
	private boolean parseError;
	private Stack<HashMap<String,IdentifierNode>> SymbolMap;
	
	// contains folding lambda functions based off enum ordinals;
	private iFold[][] foldFuncs;
	
	
	
	public boolean hasParseError() {
		return parseError;
	}

	private ConstantNode folding(ConstantNode n1, ConstantNode n2, opperator op) {
		type t = ExpressionNode.calcType(n1.getType(), n2.getType(), op);
		if(t == type.ERROR) {
			return new ConstantNode(null,type.ERROR);
		}
		

		return foldFuncs[n1.getType().ordinal()][n2.getType().ordinal()].fold(n1, n2, op);
	}
	
	/**
	 * Generates Lambda functions for foldFuncs array;
	 */
	private void genFoldFuncs() {
		foldFuncs = new iFold[3][3];
		
		foldFuncs[type.CHAR.ordinal()][type.CHAR.ordinal()] = 
			(ConstantNode n1, ConstantNode n2, opperator op) -> {
				char ctmp1 = n1.getVal().charAt(0);
				char ctmp2 = n1.getVal().charAt(0);
				switch(op) {
				case LT:
					if(ctmp1 < ctmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case GT:
					if(ctmp1 > ctmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case LE:
					if(ctmp1 <= ctmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case GE:
					if(ctmp1 >= ctmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case EQ:
					if(ctmp1 == ctmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case NE:
					if(ctmp1 != ctmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case OR:
					if(ctmp1!=0||ctmp2!=0) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case AND:
					if(ctmp1!=0&&ctmp2!=0) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				}
				return null;

		};

		foldFuncs[type.INT.ordinal()][type.INT.ordinal()] = 
			(ConstantNode n1, ConstantNode n2, opperator op)->{
				int itmp1 = Integer.parseInt(n1.getVal());
				int itmp2 = Integer.parseInt(n2.getVal());
				switch(op) {
				case LT:
					if(itmp1 < itmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case GT:
					if(itmp1 > itmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case LE:
					if(itmp1 <= itmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case GE:
					if(itmp1 >= itmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case EQ:
					if(itmp1 == itmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case NE:
					if(itmp1 != itmp2) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case OR:
					if(itmp1!=0||itmp2!=0) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case AND:
					if(itmp1!=0&&itmp2!=0) 
						return new ConstantNode("1",type.INT);
					else
						return new ConstantNode("0",type.INT);
				case PLUS:
					return new ConstantNode(Integer.toString(itmp1+itmp2),type.INT);
				case MINUS:
					return new ConstantNode(Integer.toString(itmp1-itmp2),type.INT);
				case TIMES:
					return new ConstantNode(Integer.toString(itmp1*itmp2),type.INT);
				case DIVIDE:
					return new ConstantNode(Integer.toString(itmp1/itmp2),type.INT);
				}
				return null;
		};

			foldFuncs[type.INT.ordinal()][type.FLOAT.ordinal()] = 
				(ConstantNode n1, ConstantNode n2, opperator op) -> {
					int itmp1 = Integer.parseInt(n1.getVal());
					double ftmp2 = Double.parseDouble(n2.getVal());
					switch(op) {
					case LT:
						if(itmp1 < ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case GT:
						if(itmp1 > ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case LE:
						if(itmp1 <= ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case GE:
						if(itmp1 >= ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case EQ:
						if(itmp1 == ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case NE:
						if(itmp1 != ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case OR:
						if(itmp1!=0||ftmp2!=0) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case AND:
						if(itmp1!=0&&ftmp2!=0) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case PLUS:
						return new ConstantNode(Double.toString(itmp1+ftmp2),type.FLOAT);
					case MINUS:
						return new ConstantNode(Double.toString(itmp1-ftmp2),type.FLOAT);
					case TIMES:
						return new ConstantNode(Double.toString(itmp1*ftmp2),type.FLOAT);
					case DIVIDE:
						return new ConstantNode(Double.toString(itmp1/ftmp2),type.FLOAT);
					}
					return null;
		};

			foldFuncs[type.FLOAT.ordinal()][type.FLOAT.ordinal()] = 
				(ConstantNode n1, ConstantNode n2, opperator op) -> {
					double ftmp1 = Double.parseDouble(n1.getVal());
					double ftmp2 = Double.parseDouble(n2.getVal());
					switch(op) {
					case LT:
						if(ftmp1 < ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case GT:
						if(ftmp1 > ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case LE:
						if(ftmp1 <= ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case GE:
						if(ftmp1 >= ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case EQ:
						if(ftmp1 == ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case NE:
						if(ftmp1 != ftmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case OR:
						if(ftmp1!=0||ftmp2!=0) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case AND:
						if(ftmp1!=0&&ftmp2!=0) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case PLUS:
						return new ConstantNode(Double.toString(ftmp1+ftmp2),type.FLOAT);
					case MINUS:
						return new ConstantNode(Double.toString(ftmp1-ftmp2),type.FLOAT);
					case TIMES:
						return new ConstantNode(Double.toString(ftmp1*ftmp2),type.FLOAT);
					case DIVIDE:
						return new ConstantNode(Double.toString(ftmp1/ftmp2),type.FLOAT);
					}
					return null;
			};
			foldFuncs[type.FLOAT.ordinal()][type.INT.ordinal()] = 
				(ConstantNode n1, ConstantNode n2, opperator op) -> {
					double ftmp1 = Double.parseDouble(n1.getVal());
					int itmp2 = Integer.parseInt(n2.getVal());
					switch(op) {
					case LT:
						if(ftmp1 < itmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case GT:
						if(ftmp1 > itmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case LE:
						if(ftmp1 <= itmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case GE:
						if(ftmp1 >= itmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case EQ:
						if(ftmp1 == itmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case NE:
						if(ftmp1 != itmp2) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case OR:
						if(ftmp1!=0||itmp2!=0) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case AND:
						if(ftmp1!=0&&itmp2!=0) 
							return new ConstantNode("1",type.INT);
						else
							return new ConstantNode("0",type.INT);
					case PLUS:
						return new ConstantNode(Double.toString(ftmp1+itmp2),type.FLOAT);
					case MINUS:
						return new ConstantNode(Double.toString(ftmp1-itmp2),type.FLOAT);
					case TIMES:
						return new ConstantNode(Double.toString(ftmp1*itmp2),type.FLOAT);
					case DIVIDE:
						return new ConstantNode(Double.toString(ftmp1/itmp2),type.FLOAT);
					}
					return null;
			};
	}
	
	/**
	 * starts the traversal and creates base node for DotGen Tree
	 */
	@Override
	public AstNode visitProgram(ProgramContext ctx) {
		CmAst ast = new CmAst();
		
		SymbolMap = new Stack<>();
		SymbolMap.add(new HashMap<>());
		
		genFoldFuncs();
		
		for(int i = 0; i < ctx.getChildCount(); i++) {
			//Declarations
			ast.addChildNode(visit(ctx.getChild(i)));
		}
		SymbolMap.pop();
		return  ast;
	}

	@Override
	public AstNode visitType(TypeContext ctx) {
		return new TypeNode(ctx.getText());
	}

	@Override
	public AstNode visitDeclaration(DeclarationContext ctx) {
		//vardecl
		if(ctx.getChildCount() == 1) {
			return visit(ctx.getChild(0));
		}
		DeclarationNode astDot= new DeclarationNode();
		TypeNode t = (TypeNode) visit(ctx.type());
		String id = ctx.ID().getText();
		IdentifierNode idNode  = new IdentifierNode(id, true);
		idNode.setType(t.getType());
		astDot.addChildNode(t);
		astDot.addChildNode(idNode);
		
		
		SymbolMap.lastElement().put(id,idNode);
		SymbolMap.add(new HashMap<>());
		if(ctx.paramdecllist()!=null) {
			astDot.addChildNode(visit(ctx.paramdecllist()));
		}
		
		//visit add all vardecl and statement children to astdot
		for(int i = 5;i<ctx.getChildCount();i++) {
			
			if(ctx.statement().contains(ctx.getChild(i))
					||ctx.vardecl().contains(ctx.getChild(i))) {
				
				astDot.addChildNode(visit(ctx.getChild(i)));
			}
		}
		
		SymbolMap.pop();
		return astDot;
	}

	@Override
	public AstNode visitParamdecllist(ParamdecllistContext ctx) {
		AstNode  astDot = new ParamListNode();
		for(ParamdeclContext c : ctx.paramdecl()) {
			astDot.addChildNode(visit(c));
		}
		return astDot;
	}

	@Override
	public AstNode visitParamdecl(ParamdeclContext ctx) {
		AstNode astDot = new ParamNode();
		TypeNode t = (TypeNode) visit(ctx.type());
		IdentifierNode id = (IdentifierNode) visit(ctx.identifier());
		
		id.setType(t.getType());
		SymbolMap.lastElement().put(ctx.identifier().ID().getText(),id);
		astDot.addChildNode(t);
		astDot.addChildNode(id);
		return astDot;
	}

	@Override
	public AstNode visitVardecl(VardeclContext ctx) {
		DeclarationNode astDot = new DeclarationNode();
		TypeNode t = (TypeNode) visit(ctx.type());
		astDot.addChildNode(t);
		for(IdentifierContext i:ctx.identifier()) {
			IdentifierNode in = (IdentifierNode) visit(i);
			in.setType(t.getType());
			SymbolMap.lastElement().put(in.getId(), in);
			astDot.addChildNode(in);
		}
		return astDot;
	}

	@Override
	public AstNode visitIdentifier(IdentifierContext ctx) {
		String id = ctx.ID().getText();
		AstNode astDot = null;
		if(ctx.INTCON() != null)
			astDot = new IdentifierNode(id, Integer.getInteger(ctx.INTCON().getText())) ;
		else
			astDot = new IdentifierNode(id);
		return astDot;
	}

	@Override
	public AstNode visitStatement(StatementContext ctx) {
		return visit(ctx.getChild(0));
	}

	@Override
	public AstNode visitAssignment(AssignmentContext ctx) {
		AstNode astDot = new StatementNode(StatementNode.statementType.assignment);
		
		VariableNode v = (VariableNode) visit(ctx.variable());
		AstNode exprNd = visit(ctx.expr());
		
		if(exprNd.getType() != v.getType() && exprNd.getType() != type.ERROR) {
			System.out.printf("cannot assign type %s to a variable of type %s\n",
					exprNd.getType().name(),v.getType().name());
		}
		astDot.addChildNode(v);
		astDot.addChildNode(exprNd);
		return astDot;
	}

	@Override
	public AstNode visitIfstatement(IfstatementContext ctx) {
		AstNode astDot = new StatementNode(StatementNode.statementType.ifstatement);
		astDot.addChildNode(visit(ctx.expr()));
		for(CpdstatementContext c:ctx.cpdstatement()) {
			astDot.addChildNode(visit(c));
		}
		return astDot;
	}

	@Override
	public AstNode visitCallstatement(CallstatementContext ctx) {
		AstNode astDot = new StatementNode(StatementNode.statementType.callstatement);
		IdentifierNode id = getIdenifier(ctx.ID().getText());
		if(id == null) {
			System.err.printf("Undeclared function: %s \n", ctx.ID().getText());
			parseError = true;
		}
		astDot.addChildNode(id);
		if(ctx.arglist()!=null) {
			for(ExprContext e:ctx.arglist().expr()) {
				astDot.addChildNode(visit(e));
			}
		}
		return astDot;
	}

	@Override
	public AstNode visitWhilestatement(WhilestatementContext ctx) {
		AstNode astDot = new StatementNode(StatementNode.statementType.whilestatement);
		astDot.addChildNode(visit(ctx.expr()));
		astDot.addChildNode(visit(ctx.statement()));
		return astDot;
	}

	@Override
	public AstNode visitIostatement(IostatementContext ctx) {
		AstNode astDot = null;
		if(ctx.WRITE() != null) {
			astDot = new StatementNode(StatementNode.statementType.writestatement);
			if(ctx.STRING() != null) {
				astDot.addChildNode(new ConstantNode(ctx.STRING().getText(),type.STRING));
			}
			else {
				astDot.addChildNode(visit(ctx.expr()));
			}
		}
		else {
			astDot = new StatementNode(StatementNode.statementType.readstatement);
			astDot.addChildNode(visit(ctx.variable()));
		}
		return astDot;
	}

	@Override
	public AstNode visitReturnstatement(ReturnstatementContext ctx) {
		AstNode astDot = new StatementNode(StatementNode.statementType.returnstatement);
		astDot.addChildNode(visit(ctx.expr()));
		return astDot;
	}

	@Override
	public AstNode visitExitstatement(ExitstatementContext ctx) {
		return new StatementNode(StatementNode.statementType.exitstatement);
	}

	@Override
	public AstNode visitCpdstatement(CpdstatementContext ctx) {
		AstNode astDot = new StatementNode(StatementNode.statementType.cpdstatement);
		SymbolMap.add(new HashMap<>());
		for(StatementContext s: ctx.statement()) {
			astDot.addChildNode(visit(s));
		}
		SymbolMap.pop();
		return astDot;
	}

	@Override
	public AstNode visitExpr(ExprContext ctx) {
		if(ctx.getChildCount() == 1) {
			return visit(ctx.getChild(0));
		}
		ExpressionNode astDot = null;
		if(ctx.OR() != null) {
			astDot = new ExpressionNode(opperator.OR);
		}
		else {
			astDot = new ExpressionNode(opperator.AND);
		}
		AstNode exprNode =  visit(ctx.expr());
		AstNode simpNode =  visit(ctx.simpleexpr());
		
		if(exprNode instanceof ConstantNode && simpNode instanceof ConstantNode) {
			return folding((ConstantNode)exprNode, (ConstantNode)simpNode, astDot.getOpperator());
		}
		
		if(ExpressionNode.calcType(exprNode.getType(),simpNode.getType(),astDot.getOpperator())==type.ERROR) {
			System.err.printf("cannot compare types %s and %s\n", exprNode.getType().name(), simpNode.getType().name());
			parseError = true;
		}
		
		astDot.addChildNode(exprNode);
		astDot.addChildNode(simpNode);
		return astDot;
	}

	@Override
	public AstNode visitSimpleexpr(SimpleexprContext ctx) {
		if(ctx.getChildCount()==1) {
			return visit(ctx.getChild(0));
		}
		ExpressionNode astDot = null;
		
		if(ctx.EQ() != null)
			astDot = new ExpressionNode(opperator.EQ);
		else if(ctx.NE() != null)
			astDot = new ExpressionNode(opperator.NE);
		else if(ctx.LE() != null)
			astDot = new ExpressionNode(opperator.LE);
		else if(ctx.LT() != null)
			astDot = new ExpressionNode(opperator.LT);
		else if(ctx.GE() != null)
			astDot = new ExpressionNode(opperator.GE);
		else if(ctx.GT() != null)
			astDot = new ExpressionNode(opperator.GT);
		
		AstNode snode = visit(ctx.simpleexpr());
		AstNode addNode = visit(ctx.addexpr());
		
		if(snode instanceof ConstantNode && addNode instanceof ConstantNode) {
			return folding((ConstantNode)snode, (ConstantNode)addNode, astDot.getOpperator());
		}
		
		if(ExpressionNode.calcType(snode.getType(),addNode.getType(),astDot.getOpperator())==type.ERROR) {
			System.err.printf("cannot compare types %s and %s\n", snode.getType().name(), addNode.getType().name());
			parseError=true;
		}
		astDot.addChildNode(snode);
		astDot.addChildNode(visit(ctx.addexpr()));
		
		return  astDot;
	}

	@Override
	public AstNode visitAddexpr(AddexprContext ctx) {
		if(ctx.getChildCount()==1) {
			return visit(ctx.getChild(0));
		}
		ExpressionNode astDot = null;
		
		if(ctx.MINUS() != null)
			astDot = new ExpressionNode(opperator.MINUS);
		else
			astDot = new ExpressionNode(opperator.PLUS);
		
		AstNode addNode = visit(ctx.addexpr());
		AstNode multNode = visit(ctx.multexpr());
		
		if(addNode instanceof ConstantNode && multNode instanceof ConstantNode) {
			return folding((ConstantNode)addNode, (ConstantNode)multNode, astDot.getOpperator());
		}
		
		if(ExpressionNode.calcType(addNode.getType(),
				multNode.getType(), astDot.getOpperator())==type.ERROR) {
			System.err.printf("cannot %s type %s with %s: %s\n"
					,astDot.getOpperator().name(),multNode.getType().name()
					,addNode.getType().name(),ctx.getText());
			parseError = true;
		}
		
		astDot.addChildNode(addNode);
		astDot.addChildNode(multNode);
		
		return  astDot;
	}

	@Override
	public AstNode visitMultexpr(MultexprContext ctx) {
		if(ctx.getChildCount()==1) {
			return visit(ctx.getChild(0));
		}
		ExpressionNode astDot = null;
		
		if(ctx.DIVIDE() != null)
			astDot = new ExpressionNode(opperator.DIVIDE);
		else
			astDot = new ExpressionNode(opperator.TIMES);
		
		AstNode multNode = visit(ctx.multexpr());
		AstNode factorNode = visit(ctx.factor());
		if(ExpressionNode.calcType(factorNode.getType(),
				multNode.getType(), astDot.getOpperator())==type.ERROR) {
			System.err.printf("cannot %s type %s with %s: %s\n"
					,astDot.getOpperator().name(),multNode.getType().name()
					,factorNode.getType().name(),ctx.getText());
			parseError = true;
		}
		astDot.addChildNode(multNode);
		astDot.addChildNode(factorNode);
		
		return  astDot;
	}

	@Override
	public AstNode visitFactor(FactorContext ctx) {
		
		if(ctx.getChildCount()==1) {
			return visit(ctx.getChild(0));
		}
		if(ctx.expr() != null) {
			return visit(ctx.expr());
		}
		
		AstNode astDot = null;
		
		if(ctx.NOT() != null) {
			AstNode tmp = visit(ctx.factor());
			if(tmp.getType() != type.INT) {
				System.err.printf("line %i: %s\n\tnot opperation can only be preformed on int\n" 
						,ctx.getStart().getLine(),ctx.getText());
				parseError = true;
			}
			
			else if(tmp instanceof ConstantNode){
				int itemp = Integer.parseInt(((ConstantNode)tmp).getVal());
				itemp = ~itemp;
				return new ConstantNode(Integer.toString(itemp),type.INT);
			}
			astDot = new FactorNode(factorT.not);
			astDot.addChildNode(tmp);
		}
		
		if(ctx.ID() != null) {
			IdentifierNode in = getIdenifier(ctx.ID().getText());
			if(in == null) {	
				System.err.printf("Function: \"%s\" undeclared\n", ctx.ID().getText());
				parseError = true;
			}
			astDot = new FactorNode(factorT.function,in);
			if(ctx.arglist()!=null) {
				for(ExprContext e:ctx.arglist().expr()) {
					astDot.addChildNode(visit(e));
				}
			}
		}
		return  astDot;
	}

	@Override
	public AstNode visitVariable(VariableContext ctx) {
		VariableNode astDot = null;
		
		IdentifierNode dec = getIdenifier(ctx.ID().getText());
		
		if(dec == null) {
			System.err.printf("Variable: \"%s\" undeclared\n", ctx.ID().getText());
			parseError = true;
		}
		
		astDot = new VariableNode(dec);
		if(ctx.expr() != null)
			astDot.addChildNode(visit(ctx.expr()));
		
		return  astDot;
	}

	@Override
	public AstNode visitConstant(ConstantContext ctx) {
		AstNode astDot = null;
		
		if(ctx.INTCON() != null)
			astDot = new ConstantNode(ctx.INTCON().getText(),type.INT);
		else if(ctx.CHARCON() != null)
			astDot = new ConstantNode(ctx.CHARCON().getText(), type.CHAR);
		else
			astDot = new ConstantNode(ctx.FLOATCON().getText(), type.FLOAT);
		return  astDot;
	}

	@Override
	public AstNode visitArglist(ArglistContext ctx) {
		//Don't Visit
		return null;
	}
	
	private IdentifierNode getIdenifier(String name) {
		for(int i = SymbolMap.size()-1; i>=0; i--) {
			HashMap<String,IdentifierNode> tmp = SymbolMap.get(i);
			if(tmp.containsKey(name)) {
				return tmp.get(name);
			}
		}
		//not in symbols;
		return null;
	}

}
