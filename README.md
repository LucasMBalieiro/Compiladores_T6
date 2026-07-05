# Trabalho final - Compiladores (T6)

## Aluno / RA

 - #### Lucas Maciel Balieiro / 800534

## Sobre o projeto

Consiste em um compilador para o jogo [Miau Cafe](https://balieiro.itch.io/miau-cafe).
(Baixe a build `MiauCafe-Comiladores.zip`)

Ele gera um arquivo .json que o jogo consegue desserializar em Scriptable Objects para montar os 
**ingredientes**, **pedidos**, **clientes** e **dias** que compôem a gameplay principal. 

Para rodar, apenas coloque o arquivo custom.json gerado pelo JAR no diretório
base do jogo (junto do executável). 

## Vídeo demonstrando o uso
[Trabalho Compiladores - Miau Cafe](https://www.youtube.com/watch?v=Qkiar_3VG5c)

---

## Pré-requisitos
* **Java Development Kit (JDK):** Versão 21 (ou compatível configurada no `pom.xml`)
* **Apache Maven:** Versão 3.6 ou superior

## Como Compilar e Rodar

Abra o terminal na raiz do projeto e execute o comando:
```bash
mvn clean package
```

Depois execute o programa passando também o caminho para o arquivo de entrada e também o caminho onde está o executável do jogo.
(Se o caminho do output for vazio, cria o custom.json no diretório atual)


```bash
java -jar T6-MiauCafe-jar-with-dependencies.jar ~/CAMINHO/DO/INPUT.txt ~/CAMINHO/DO/JOGO
```
---

## 🔹Sintaxe da Linguagem

A linguagem é declarativa e dividida em três blocos principais. A ordem importa: dependências devem ser 
declaradas antes de serem usadas.

### 1. Declarando Ingredientes

Define a base dos pedidos. Todo ingrediente possui um tipo e um Tier (nível de melhoria).

* **Tipos válidos:** `Cafe`, `Sorvete`, `Bolo`, `Pao`.
* **Regra Semântica:** O `tier` aceita apenas valores inteiros entre **0 e 3**.

```text
Ingrediente CafeSimples {
    tipo: Cafe;
    tier: 1;
}

Ingrediente BoloMorango {
    tipo: Bolo;
    tier: 2;
}

```

### 2. Declarando Pedidos

Agrupa ingredientes, definindo o valor de venda (inteiro) e o tempo máximo (segundos.milisegundos) que o jogador tem para entregá-lo.

* Os itens referenciados dentro de `itens: {}` devem ter sido declarados previamente como `Ingrediente`.

```text
Pedido ComboMatinal {
    valor: 25;
    tempo: 45.5;
    itens: {
        CafeSimples: 1;
        BoloMorango: 1;
    }
}

```

### 3. Declarando Spawners (Fases e Dias)

Monta a estrutura dos dias onde cada dia é composto por uma lista de clientes.

* Os IDs na lista `clientes: []` devem ter sido declarados previamente como `Pedido`.

```text
Spawner TutorialCompiladores {
    Dia {
        clientes: [ComboMatinal, ComboMatinal];
    }
    Dia {
        clientes: [ComboMatinal, ComboMatinal, ComboMatinal];
    }
}

```
