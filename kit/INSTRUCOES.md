# Instruções da tarefa

Cada pasta de falha (por exemplo `F03/`) é um pequeno projeto Java pronto para abrir no VS Code:

```
F03/
├── src/…/StopWatch.java      ← classe sob teste (não altere)
├── test/…/F03Test.java       ← esqueleto: escreva seus testes aqui
├── lib/                      ← resto do projeto compilado e o JUnit (só para o compilador)
├── .vscode/settings.json     ← configuração do projeto (não altere)
└── LEIA-ME.md
```

A tarefa é escrever, em `F03Test.java`, uma suíte de testes JUnit para a classe sob teste.

O código da classe é o original do projeto, com uma única alteração aplicada igualmente a todas as classes: **os comentários de linha (`//`) foram removidos** e as referências a issues no Javadoc aparecem como `[ref. removida]`. O Javadoc foi mantido.

> Estas instruções valem para as **suítes humanas**. Os operadores de IA seguem o protocolo do prompt definido no piloto.

## Antes de começar (fora do tempo)

1. Configure o perfil `Experimento` do VS Code conforme o `PERFIL_VSCODE.md`, e faça a conferência no final dele usando a pasta `P01/` do aquecimento.
2. Leia a cola do JUnit (`JUNIT.md`). Você pode consultá-la durante a tarefa.

## Regras

1. **Tempo:** 30 minutos por falha, contados a partir do momento em que você abre a classe sob teste.
2. **Abra uma falha por vez**: `Arquivo → Abrir Pasta` na pasta da falha, no perfil `Experimento`.
3. **Não execute os testes** nem a classe. Use só a leitura do código e do Javadoc e os erros de compilação que o editor mostra.
4. **Não pesquise** a classe, o projeto ou bugs conhecidos na internet, no Defects4J ou em repositórios.
5. **Não use IA** de nenhuma forma (no editor, no navegador ou no celular), **nem analisadores de código**, nem extensões fora do perfil.
6. Teste o **comportamento esperado** da classe, como descrito no Javadoc e nos nomes dos métodos. Escreva os testes que você acha que encontrariam defeitos, cobrindo **valores-limite das entradas**, **caminhos normais** e **tratamento de erros**.
7. **Um comportamento por método de teste.**
8. Use **somente métodos públicos** da classe.

## Formato da entrega

- Escreva no esqueleto `test/…/<código>Test.java`, sem mudar o nome do arquivo, da classe nem o `package`.
- **JUnit 4.12** (`org.junit.*`). Não use outras bibliotecas.
- Não altere a classe sob teste nem o `.vscode/settings.json`.

## Ao terminar

- Preencha o horário de início e de fim na primeira linha do arquivo:
  `// inicio: 14:05  fim: 14:35`
- Entregue **só o arquivo `<código>Test.java`** ao curador. Depois de entregue, **o arquivo fica congelado** e não pode mais ser alterado. Correções de compilação são feitas só pelo revisor mecânico, com base na lista fechada de correções.
