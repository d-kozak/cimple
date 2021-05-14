grammar Cimple;

@header {
    package io.dkozak.cimple;
}

file : func*;

func : 'fun' ID '(' args? ')' '{' body '}' ;

args : ID (',' ID )*;

body : stat*;

stat : def ';' | expr ';' | ifStat | forStat | 'return' expr? ';' ;

ifStat : 'if' expr '{' stat*  '}' ('else' '{' stat* '}')?;

forStat : 'for' init=def? ';' expr? ';' (def | expr)?  '{' stat* '}' ;

def : ID '=' expr ;

expr :
    expr '*' expr  |
    expr '/' expr  |
    expr '+' expr  |
    expr '-' expr  |
    expr '==' expr |
    expr '!=' expr |
    expr '>' expr  |
    expr '>=' expr |
    expr '<' expr  |
    expr '<=' expr |
    call |
    NUM  |
    ID
    ;

call : ID '(' args? ')' ;

ID :[A-Za-z_][A-Za-z0-9]*;
NUM : [0-9]+;

/** ignore whitespace */
WS : [ \t\r\n] -> skip;