grammar Cminus;

options {
	language = Java;
}


@header {
}

program: declaration+;

CHAR: 'char';
ELSE: 'else';
EXIT: 'exit';
FLOAT: 'float';
IF: 'if';
INT: 'int';
READ: 'read';
RETURN: 'return';
VOID: 'void';
WHILE: 'while';
WRITE: 'write';

AND: '&&';
ASSIGN: '=';
CM: ',';
DIVIDE: '/';
DOT: '.';
DQ: '"';
EQ: '==';
GE: '>=';
GT: '>';
LBK: '[';
LBR: '{';
LE: '<=';
LP: '(';
LT: '<';
MINUS: '-';
NE: '!=';
NOT: '!';
OR: '||';
PLUS: '+';
RBK: ']';
RBR: '}';
RP: ')';
SC: ';';
SQ: '\'';
TIMES: '*';

fragment
LETTER: ('a'..'z' | 'A'..'Z');

fragment
DIGIT: '0'..'9';

ID: LETTER (LETTER | DIGIT)*;

fragment
POSITIVE: '1' ..'9';

INTCON: (POSITIVE DIGIT*) | '0';

FLOATCON: INTCON DOT DIGIT*;

CHARCON: SQ .? SQ;

COMMENT: '/*' .*? '*/' -> channel(HIDDEN);

WS: ( ' ' | '\t' | '\r' | '\n') -> skip;

STRING: DQ .*? DQ;

type: (VOID | FLOAT | INT | CHAR);

declaration: type ID LP (paramdecllist)? RP LBR (vardecl | statement)* RBR | vardecl;

paramdecllist: paramdecl (CM paramdecl)*;

paramdecl: type identifier;

vardecl:type identifier (CM identifier)* SC;

identifier: ID (LBK INTCON RBK)?;

statement: (assignment | callstatement | ifstatement | whilestatement | iostatement | returnstatement | exitstatement | cpdstatement);

assignment: variable ASSIGN expr SC;

ifstatement: IF LP expr RP cpdstatement (ELSE cpdstatement)?;

callstatement: ID LP arglist? RP SC;

whilestatement: WHILE LP expr RP statement;

iostatement: READ LP variable RP SC | WRITE LP (expr | STRING) RP SC;

returnstatement: RETURN expr SC;

exitstatement: EXIT SC;

cpdstatement: LBR (statement)* RBR;

expr: simpleexpr | expr OR simpleexpr | expr AND simpleexpr;

simpleexpr: addexpr | simpleexpr EQ addexpr | simpleexpr NE addexpr | simpleexpr LE addexpr | simpleexpr LT addexpr | simpleexpr GT addexpr | simpleexpr GE addexpr;

addexpr: multexpr | addexpr PLUS multexpr | addexpr MINUS multexpr;

multexpr: factor | multexpr TIMES factor | multexpr DIVIDE factor;

factor: variable | constant | ID LP (arglist)? RP | NOT factor | LP expr RP;

variable: ID | ID LBK expr RBK;

constant: INTCON | FLOATCON | CHARCON;

arglist: expr (CM expr)*;