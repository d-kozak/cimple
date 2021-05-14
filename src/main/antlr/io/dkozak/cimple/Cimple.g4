grammar Cimple;

@header {
    package io.dkozak.cimple;
}

file : func*;

func : 'fun' ID '(' params? ')' '{' body '}' ;

params : ID (',' ID )*;

body : stat*;

stat : def ';' #defAttr | expr ';' #exprAttr | ifStat #ifAttr | forStat #forAttr | 'return' expr? ';' #retAttr ;

ifStat : 'if' cond=expr '{' thenPart=body  '}' ('else' '{' elsePart=body '}')?;

forStat : 'for' init=def? ';' cond=expr? ';' inc=def? '{' body '}' ;

def : ID '=' expr ;

expr :
    expr op='*' expr  |
    expr op='/' expr  |
    expr op='+' expr  |
    expr op='-' expr  |
    expr op='==' expr |
    expr op='!=' expr |
    expr op='>' expr  |
    expr op='>=' expr |
    expr op='<' expr  |
    expr op='<=' expr |
    call |
    NUM  |
    '(' expr ')' |
    ID
    ;

call : ID '(' args? ')' ;

args : expr (',' expr )*;

ID :[A-Za-z_][A-Za-z0-9]*;
NUM : [0-9]+;

/** ignore whitespace */
WS : [ \t\r\n] -> skip;