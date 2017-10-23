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
    : expression
    ;

expression
    : lhs=expression op=('/'|'*'|'%') rhs=expression #ArithmeticExpression
    | lhs=expression op=('-'|'+')     rhs=expression #ArithmeticExpression
    | value=INTEGER                                  #IntExpression
    ;

PLUS     : '+' ;
MINUS    : '-' ;
ASTERISK : '*' ;
SLASH    : '/' ;
PERCENT  : '%' ;

INTEGER    : [0-9]+                        ;
WHITESPACE : [ \t\r\n]+ -> channel(HIDDEN) ;
