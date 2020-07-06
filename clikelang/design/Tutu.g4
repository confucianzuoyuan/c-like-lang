grammar Tutu;

@header {
package Parser;
}

// 程序开始的地方，`*`：0次或多次
program
    :   programSection* EOF
    ;

// 程序块，类的声明，变量的声明，函数的声明
// `#`可以命名别名
programSection
    :   classDeclaration          # class
    |   functionDeclaration       # func
    |   variableDeclaration       # var
    ;

// 语句：语句块；表达式语句；条件语句；循环语句；跳转语句
statement
    :   blockStatement            # block
    |   expressionStatement       # expr
    |   selectionStatement        # select
    |   iterationStatement        # iter
    |   jumpStatement             # jump
    ;

// 语句块，由大括号包围
blockStatement
    :  '{' blockItem* '}'
    ;

// 语句项：变量声明，语句；这里会递归
blockItem
    : variableDeclaration         # vardecl
    | statement                   # stmt
    ;

// 表达式语句：表达式
expressionStatement
    :   expression? ';'
    ;

// 条件语句
selectionStatement
    :   'if' '(' expression ')' statement ('else' statement)?
    ;

// 循环语句
// while (i > 0) i--;
// for (int i = 0; ; ;)
// for (; ; ;)
iterationStatement
    :   'while' '(' expression ')' statement                  # while
    |   'for' '(' declinit=variableDeclaration
                  cond=expression? ';'
                  step=expression? ')'
            statement                                         # for
    |   'for' '(' init=expression? ';'
                  cond=expression? ';'
                  step=expression? ')'
            statement                                         # for
    ;

// 跳转语句
jumpStatement
    :   'continue' ';'              # continue
    |   'break' ';'                 # break
    |   'return' expression? ';'    # return
    ;

// 非数组类型的声明
nonArrayTypeSpecifier
    :   type='int'
    |   type='bool'
    |   type='string'
    |   type='void'
    |   type=Identifier
    ;

// int[][][]
typeSpecifier
    :   typeSpecifier '[' ']'     # arrayType
    |   nonArrayTypeSpecifier     # nonArrayType
    ;

// int a = 1, b = 1;
variableDeclaration
    :   typeSpecifier variableInitDeclarator (',' variableInitDeclarator)* ';'
    ;

// a = 1;
variableInitDeclarator
    :   Identifier ('=' expression)?
    ;

// class C { int a; }
// 类的声明，类语法很简单，类似结构体
classDeclaration
    :   'class' Identifier '{' memberDeclaration* '}'
    ;

// int a;
// 成员变量声明
memberDeclaration
    :   typeSpecifier Identifier ';'
    ;

// int func() {}
// 函数声明语法
functionDeclaration
    :   typeSpecifier Identifier '(' parameterDeclarationList? ')' blockStatement
    ;

// 形式参数列表声明语法
parameterDeclarationList
    :   parameterDeclaration (',' parameterDeclaration)*
    ;

// 形式参数声明语法
parameterDeclaration
    :   typeSpecifier Identifier
    ;

//------ Expression: http://en.cppreference.com/w/cpp/language/operator_precedence
// 表达式语法
expression
    :   expression op=('++' | '--')                  # PostfixIncDec    // Precedence 1
    |   expression '(' parameterList? ')'            # FunctionCall // 函数调用
    |   expression '[' expression ']'                # Subscript    // 利用下标访问数组元素
    |   expression '.' Identifier                    # MemberAccess // 访问类成员变量

    |   <assoc=right> op=('++'|'--') expression      # UnaryExpr        // Precedence 2
    |   <assoc=right> op=('+' | '-') expression      # UnaryExpr // 正负号
    |   <assoc=right> op=('!' | '~') expression      # UnaryExpr
    |   <assoc=right> 'new' creator                  # New       // 新建数组、变量等

    |   expression op=('*' | '/' | '%') expression   # BinaryExpr       // Precedence 3
    |   expression op=('+' | '-') expression         # BinaryExpr       // Precedence 4
    |   expression op=('<<'|'>>') expression         # BinaryExpr       // Precedence 5
    |   expression op=('<' | '>') expression         # BinaryExpr       // Precedence 6
    |   expression op=('<='|'>=') expression         # BinaryExpr
    |   expression op=('=='|'!=') expression         # BinaryExpr       // Precedence 7
    |   expression op='&' expression                 # BinaryExpr       // Precedence 8
    |   expression op='^' expression                 # BinaryExpr       // Precedence 9
    |   expression op='|' expression                 # BinaryExpr       // Precedence 10
    |   expression op='&&' expression                # BinaryExpr       // Precedence 11
    |   expression op='||' expression                # BinaryExpr       // Precedence 12

    |   <assoc=right> expression op='=' expression   # BinaryExpr       // Precedence 14 赋值表达式

    |   Identifier                                   # Identifier       // 变量
    |   constant                                     # Literal          // 常量
    |   '(' expression ')'                           # SubExpression    // 括号扩住的表达式
    ;

creator
    :   nonArrayTypeSpecifier ('[' expression ']')+ ('[' ']')+ ('[' expression ']')+   # creatorError
    |   nonArrayTypeSpecifier ('[' expression ']')+ ('[' ']')*                         # creatorArray
    |   nonArrayTypeSpecifier                                                          # creatorNonArray
    ;

// 实际参数列表
parameterList
    :   expression (',' expression)*
    ;

// 常量
constant
    :   type=IntegerConstant
    |   type=CharacterConstant
    |   type=StringLiteral
    |   type=NullLiteral
    |   type=BoolConstant
    ;

// 关键字
// fragment定义的东西只在g4可见，相当于局部变量，供其他语法规则使用
Bool : 'bool';
Int : 'int';
String : 'string';
Void : 'void';
If : 'if';
For : 'for';
While : 'while';
Break : 'break';
Continue : 'continue';
Return : 'return';
New : 'new';
Class : 'class';

// 符号
LParen : '(';
RParen : ')';
LBracket : '[';
RBracket : ']';
LBrace : '{';
RBrace : '}';

Less : '<';
LessEqual : '<=';
Greater : '>';
GreaterEqual : '>=';
LeftShift : '<<';
RightShift : '>>';

Plus : '+';
PlusPlus : '++';
Minus : '-';
MinusMinus : '--';
Star : '*';
Div : '/';
Mod : '%';

And : '&';
Or : '|';
AndAnd : '&&';
OrOr : '||';
Caret : '^';
Not : '!';
Tilde : '~';

Question : '?';
Colon : ':';
Semi : ';';
Comma : ',';

Assign : '=';

Equal : '==';
NotEqual : '!=';

Dot : '.';

// 常量
NullLiteral
    :   'null'
    ;

BoolConstant
    :   'true'
    |   'false'
    ;

IntegerConstant
    :   NonzeroDigit Digit*
    |   '0'
    ;

fragment
NonzeroDigit
    :   [1-9]
    ;

CharacterConstant
    :   '\'' CCharSequence '\''
    ;

fragment
CCharSequence
    :   CChar+
    ;

fragment
CChar
    :   ~['\\\r\n]
    |   EscapeSequence
    ;

fragment
EscapeSequence
    :   SimpleEscapeSequence
    ;

fragment
SimpleEscapeSequence
    :   '\\' ['"?abfnrtv\\]
    ;

StringLiteral
    :   '"' SCharSequence? '"'
    ;

fragment
SCharSequence
    :   SChar+
    ;

fragment
SChar
    :   ~["\\\r\n]
    |   EscapeSequence
    ;

// 标识符
Identifier
    :   IdentifierNondigit ( IdentifierNondigit | Digit )*
    ;

fragment
IdentifierNondigit
    :   [a-zA-Z_]
    ;

fragment
Digit
    :   [0-9]
    ;

// 需要跳过的空字符和注释
Whitespace
    :   [ \t]+ -> skip
    ;

Newline
    :   '\r'? '\n' -> skip
    ;

BlockComment
    :   '/*' .*? '*/' -> skip
    ;

LineComment
    :   '//' ~[\r\n]* -> skip
    ;
