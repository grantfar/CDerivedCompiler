package grant.farnsworth.cminusparcer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import grant.farnsworth.cminusparcer.Ast.*;
import grant.farnsworth.cminusparcer.assemblygen.*;

public class Pcminus 
{
	public static List<String> extractConstants(AstNode n){
		List<String> ret = new LinkedList<>();
		if(n instanceof ConstantNode) {
			String tmp = null;
			switch(((ConstantNode)n).getType()) {
			case STRING:
				tmp = AssemblyCommand.stringConst(((ConstantNode)n).getVal());
				break;
			case FLOAT:
				tmp = AssemblyCommand.floatConst(((ConstantNode)n).getVal());
				break;
			default:
				break;
			}
			if(tmp != null)
				ret.add(tmp);
		}
		if(n.getChildren() != null)
			for(AstNode node: n.getChildren())
				ret.addAll(extractConstants(node));
		
		return ret;
	}
	
	public static void proccessMethod(DeclarationNode n, Program p) {
		p.addAllDataSection(extractConstants(n));
		AssemblyCommand.pushFunStack();
		AssemblyCommand.pushStack();
		
		List<StackVariable> stackVars = new ArrayList<>();
		
		for(AstNode a : n.getChildren()) {
			if(a instanceof DeclarationNode) {
				DeclarationNode tmp = (DeclarationNode) a;
				
				for(int i = 1; i<tmp.getChildren().size();i++) {
					stackVars.add(new StackVariable(
							((TypeNode)tmp.getChildren().get(0)).getType(),
							((IdentifierNode)tmp.getChildren().get(i)).getId()
								));
				}
				
			}
		}
		
		for(AstNode a : n.getChildren()) {
			if(a instanceof StatementNode) {
				StatementNode s = (StatementNode) a;
				switch(s.getStatementType()) {
				case assignment:
					
					if(s.getChildren().get(1) instanceof ExpressionNode) {
						TempVariable t = new TempVariable();
						
						p.addAllTextSection(
								AssemblyCommand.ExpressionEvaluation(
										(ExpressionNode) s.getChildren().get(1), t)
								);
						
						p.addAllTextSection(AssemblyCommand.assignment(t,
								((VariableNode)s.getChildren().get(0)).getDeclaration().getId())
								);
						
					}
					else if(s.getChildren().get(1) instanceof ConstantNode) {
						ConstantNode cn = (ConstantNode) s.getChildren().get(1);
						p.addAllTextSection(AssemblyCommand.assignmentc(cn.getVal(),
								((VariableNode)s.getChildren().get(0)).getDeclaration().getId()
								, cn.getType()));
					}
					else if(s.getChildren().get(1) instanceof VariableNode){
						VariableNode v = (VariableNode) s.getChildren().get(1);
						p.addAllTextSection(AssemblyCommand.assignment(v.getDeclaration().getId(),
								((VariableNode)s.getChildren().get(0)).getDeclaration().getId()));
					}
					
					break;
				case callstatement:
					break;
				case ifstatement:
					break;
				case whilestatement:
					break;
				case writestatement:
					break;
				case readstatement:
					break;
				case returnstatement:
					break;
				case exitstatement:
					break;
				case cpdstatement:
					break;
				}
			}
		}
		
		AssemblyCommand.popFunStack();
	}
	
	/**
	 * @param args
	 * args[0] should be file location
	 * opens file and feeds it to parser generated by antlr
	 */
	
    public static void main( String[] args )
    {
    	
    	CharStream cs = null;    	
    	
    	try {
    		//creates a char stream from the file inputed
    		cs = CharStreams.fromFileName(args[0]);
    	}
    	catch(IOException e) {
    		System.out.println("file not found");
    		System.exit(1);
    	}
    	
    	// creates a Cminus lexer for the charstream created from the file
    	CminusLexer cml = new CminusLexer(cs);
    	
    	//creates a stream of tokens
    	//that the parser can use for parsing to file.
    	CommonTokenStream tokenStream = new CommonTokenStream(cml);
    	
    	//create a parser to parse to file
    	CminusParser cmp = new CminusParser(tokenStream);
    	
    	//have the parser create a parse tree and store it
    	ParseTree tree = cmp.program();
    	
    	//visitor that generates AST
    	
    	CminusASTVisitor vis = new CminusASTVisitor();
    	CmAst ast = (CmAst) vis.visit(tree);
    	
    	if(cmp.getNumberOfSyntaxErrors()!=0 || vis.hasParseError()) {
    		System.exit(0);
    	}
    	
    	ArrayList<DeclarationNode> methods = new ArrayList<>();
    	Program p = new Program();
    	
    	AssemblyCommand.init();
    	AssemblyCommand.pushStack();
    	
    	for(AstNode n:ast.getChildren()) {
    		if(((IdentifierNode) n.getChildren().get(1)).isFunction()) {
    			methods.add((DeclarationNode) n);
    		}
    		else {
    			for(int i = 1; i< n.getChildren().size(); i++) {
    				p.addBssSection(AssemblyCommand.globalVar(
    						((IdentifierNode)n.getChildren().get(i)).getId(),
    						((TypeNode)n.getChildren().get(0)).getType()
    						));
    			}
    		}
    	}
    	
    	for(DeclarationNode n:methods) {
    		if(((IdentifierNode)n.getChildren().get(1)).getId().equals("main")) {
    			p.addTextSection(".text");
    			p.addTextSection(".globl main");
    			p.addTextSection(".type main,@function");
    		}
    	}
    	
    	try {
			p.writeAssemblyFile(new File("test.out"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}