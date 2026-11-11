# Protocolo da condição de IA

Guia para os operadores de IA (Dyonathan, Rafaela e Sidnei), conforme o artigo (seções 3.6 a 3.8). Tem duas etapas: o **piloto do prompt**, feito agora com a falha P02 (fora da amostra), e a **sessão de IA (S3)**.

## Etapa 1: piloto do prompt (P02)

### 1.1 Modelo e configuração

No Google AI Studio, escolham **um único modelo** e **uma configuração**, que valem para todas as execuções do experimento:

| Item | Regra |
|---|---|
| Modelo | Um só, por exemplo da família Flash. Anotem o **ID exato** que aparece no AI Studio |
| Temperatura | Valor fixo, anotado |
| Nível de raciocínio (thinking) | Valor fixo, anotado |
| Busca do Google (grounding) | **Desligada** |
| Contexto de URL | **Desligado** |
| Execução de código (code execution) | **Desligada**. Os testadores não puderam executar nada |
| Saída estruturada e function calling | Desligadas |
| Instruções de sistema | Se usarem, fazem parte do prompt e são congeladas junto |

### 1.2 Requisitos do prompt

O prompt é **o mesmo para todas as falhas**. Só podem variar dois campos:

- `{CODIGO}`: o código neutro da falha (ex.: `F03`)
- `{CLASSE}`: o conteúdo completo do arquivo `.java` da classe sob teste, em `src/` na pasta da falha, colado sem alteração

O prompt deve pedir **as mesmas coisas que os testadores receberam** no `INSTRUCOES.md`, e nada além:

1. Uma suíte de testes **JUnit 4.12** para **Java 11**, sem outras bibliotecas
2. **Um comportamento por método de teste**
3. Testar o **comportamento esperado** descrito no Javadoc e nos nomes dos métodos, cobrindo **valores-limite das entradas**, **caminhos normais** e **tratamento de erros**
4. Usar **somente métodos públicos** da classe
5. Formato da saída: **um único arquivo Java**, com a classe pública `{CODIGO}Test`, no **mesmo pacote** da classe sob teste

É proibido:

- Mencionar tipos de defeito, áreas suspeitas ou qualquer dica sobre a classe específica
- Pedir para a IA executar os testes
- Usar exemplos de teste de outras classes do projeto

### 1.3 Como fazer o piloto

1. Abram a pasta `P02/` do zip do piloto. A classe sob teste está em `src/`.
2. Escrevam o prompt, rodem numa **conversa nova** e confiram a saída: veio um único arquivo Java? Com o nome de classe e o pacote certos? Só JUnit 4?
3. Ajustes no piloto podem ser **só de formato** (como a saída vem). Nunca de conteúdo ("teste mais tal coisa").
4. Guardem **todas as versões** do prompt testadas e as respostas brutas.
5. Opcional: mandem a resposta final ao curador, que pode compilá-la e medi-la. O P02 está fora da amostra, então isso não afeta o experimento.

### 1.4 O que entregar ao curador (antes da S3)

- `prompt_final.txt`: o texto final, com os campos `{CODIGO}` e `{CLASSE}`
- Modelo (ID exato), configuração completa e a data
- Print da tela de configuração do AI Studio
- As versões anteriores do prompt e as respostas do piloto

O curador congela tudo com hash. **Depois disso, nada muda.**

## Etapa 2: sessão de IA (S3)

Só começa depois do prompt congelado. Cada operador recebe **um zip por falha** (4 falhas) e faz **3 execuções** de cada uma, seguidas.

Para cada execução:

1. Abram uma **conversa nova** no AI Studio
2. Confiram o **modelo e a configuração** (iguais aos congelados)
3. Colem o **prompt congelado**, preenchendo `{CODIGO}` e `{CLASSE}`
4. **Uma geração só**: sem pedir correção, sem gerar de novo, sem editar a resposta
5. Copiem a **resposta inteira** para `{CODIGO}_exec{N}.md` (ex.: `F06_exec1.md`)
6. Mandem ao curador **na hora**, com o horário

Na primeira execução do dia, mandem também um print da configuração. Se algo der errado (resposta cortada, erro do AI Studio), **não repitam por conta própria**: avisem o curador, que decide e registra.
