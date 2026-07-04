grammar MiauData;

programa: declaracao+ EOF ;

// --- SINTATICO ---

declaracao: decIngrediente | decPedido | decSpawner ;

decIngrediente: 'Ingrediente' ID '{'
                'tipo' ':' TIPO_INGREDIENTE ';'
                'tier' ':' NUM_INT ';'
                '}' ;

decPedido: 'Pedido' ID '{'
           'valor' ':' NUM_INT ';'
           'tempo' ':' NUM_FLOAT ';'
           'itens' ':' '{' itemPedido+ '}'
           '}' ;

itemPedido: ID ':' NUM_INT ';' ;

decSpawner: 'Spawner' ID '{' dia+ '}' ;

dia: 'Dia' '{' 'clientes' ':' '[' ID (',' ID)* ']' ';' '}' ;

// --- LEXICO ---

TIPO_INGREDIENTE: 'Cafe' | 'Sorvete' | 'Bolo' | 'Pao' ;

ID: [a-zA-Z_][a-zA-Z0-9_]* ;
NUM_INT: [0-9]+ ;
NUM_FLOAT: [0-9]+ '.' [0-9]+ ;

WS: [ \t\r\n]+ -> skip ;
COMENTARIO: '//' ~[\r\n]* -> skip ;