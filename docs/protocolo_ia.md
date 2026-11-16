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

O prompt é **o mesmo para todas as falhas**. Só varia o campo `{CODIGO}`, o código neutro da falha (ex.: `F03`). Antes de enviar, ele é **substituído pelo valor real, sem as chaves** (`{CODIGO}Test` vira `F03Test`). Um marcador esquecido faz a IA ignorar o nome pedido.

A classe sob teste **não é colada no texto**: vai como **arquivo anexo** (ver 2.1). Todas as classes têm links no cabeçalho de licença, e o AI Studio bloqueia o envio de texto com links quando o URL Context está desligado. O anexo leva o arquivo idêntico ao que os testadores receberam.

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

- `prompt_final.txt`: o texto final, com o campo `{CODIGO}`
- Modelo (ID exato), configuração completa e a data
- Print da tela de configuração do AI Studio
- As versões anteriores do prompt e as respostas do piloto

O curador congela tudo com hash. **Depois disso, nada muda.**

## Etapa 2: sessão de IA (S3)

Só começa depois do prompt congelado. Cada operador recebe **um zip por falha** (4 falhas) e faz **uma única execução** de cada uma (emenda de 2026-11-14; o plano original era de 3 execuções por falha).

### 2.1 O zip de cada falha

O curador prepara tudo, e não há nada para renomear nem substituir. Cada zip da S3 traz:

```
PROTOCOLO_IA.md
F06/
├── PROMPT.txt        ← prompt congelado, já com o código da falha (F06Test)
└── <Classe>.txt      ← a classe sob teste, idêntica à dos testadores, em .txt para o anexo
```

A classe vai em `.txt` porque o AI Studio rejeita anexos `.java` (*"Inline data requires a mime_type"*). O conteúdo é o mesmo arquivo dos testadores.

### 2.2 A execução (uma por falha)

1. Abram uma **conversa nova** no AI Studio
2. Confiram o **modelo e a configuração** (iguais aos congelados): busca do Google, URL Context, execução de código, function calling e saída estruturada **desligados**
3. Colem o conteúdo do `PROMPT.txt` e **anexem o `<Classe>.txt`**
4. Se aparecer o aviso *"Enable URL Context"*, **não ativem**: fechem no X
5. Enviem **uma vez**: sem pedir correção, sem gerar de novo, sem editar a resposta
6. Copiem o **bloco de código** pelo botão de copiar do próprio bloco (não selecionando com o mouse) e salvem como `{CODIGO}Test.java` (ex.: `F06Test.java`), sem editar nada. Se a resposta tiver **qualquer texto fora do bloco**, copiem a **mensagem inteira** pelo botão da mensagem e salvem como `{CODIGO}_resposta.md`
7. Mandem ao curador **na hora**, com o horário, confirmando que a resposta tinha só o bloco de código

Na primeira execução do dia, mandem também um print da configuração. Se algo der errado (resposta cortada, erro do AI Studio), **não repitam por conta própria**: avisem o curador, que decide e registra.
