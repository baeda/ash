/*
 * The Ash Project
 * Copyright (C) 2017  Peter Skrypalle
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

grammar Ash;

file
    : functions+=funcDeclaration+
    ;

funcDeclaration
    : 'func' id=IDENTIFIER '(' params=paramList? ')'
      ':' type=IDENTIFIER body=block
    ;

paramList
    : paramDeclaration (',' paramDeclaration)*
    ;

paramDeclaration
    : id=IDENTIFIER ':' type=IDENTIFIER
    ;

varDeclaration
    : id=IDENTIFIER ':' type=IDENTIFIER
    ;

varAssign
    : id=IDENTIFIER '=' value=expression
    ;

block
    : '{' statements+=statement* '}'
    ;

funcCall
    : id=IDENTIFIER '(' args=argumentList? ')'
    ;

argumentList
    : argument (',' argument)*
    ;

argument
    : expr=expression
    ;

statement
    : ref=varDeclaration        ';' #VarDeclarationStatement
    | ref=varAssign             ';' #VarAssignStatement
    | ref=block                     #BlockStatement
    | expr=expression           ';' #ExpressionStatement
    | 'return' expr=expression? ';' #ReturnStatement
    | 'dump' expr=expression    ';' #DumpStatement
    ;

expression
    : '(' expr=expression ')'                        #ParenExpression
    | lhs=expression op=('/'|'*'|'%') rhs=expression #ArithmeticExpression
    | lhs=expression op=('-'|'+')     rhs=expression #ArithmeticExpression
    | call=funcCall                                  #FuncCallExpression
    | value=('true'|'false')                         #BoolLiteralExpression
    | value=IDENTIFIER                               #IdExpression
    | value=INTEGER                                  #IntExpression
    ;

PLUS      : '+' ;
MINUS     : '-' ;
ASTERISK  : '*' ;
SLASH     : '/' ;
PERCENT   : '%' ;
EQUALS    : '=' ;
L_BRACE   : '{' ;
R_BRACE   : '}' ;
L_PAREN   : '(' ;
R_PAREN   : ')' ;
COMMA     : ',' ;
SEMICOLON : ';' ;
COLON     : ':' ;

KW_FUNC   : 'func'   ;
KW_RETURN : 'return' ;
KW_DUMP   : 'dump'   ;
KW_TRUE   : 'true'   ;
KW_FALSE  : 'false'  ;

INTEGER    : [0-9]+                        ;
IDENTIFIER : [a-z][a-zA-Z0-9_]*            ;
WHITESPACE : [ \t\r\n]+ -> channel(HIDDEN) ;
