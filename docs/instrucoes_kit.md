# Instruções da tarefa

Você recebeu uma pasta de falha (por exemplo `F03/`) com o código-fonte de **uma classe Java**. A tarefa é escrever uma suíte de testes JUnit para essa classe.

O código é o original do projeto, com uma única alteração aplicada igualmente a todas as classes: **os comentários de linha (`//`) foram removidos** e as referências a issues no Javadoc aparecem como `[ref. removida]`. O Javadoc foi mantido. Detalhes em `docs/neutralizacao_kit.md` no repositório do experimento.

> Estas instruções valem para as **suítes humanas**. Os operadores de IA seguem o protocolo do prompt definido no piloto.

## Ambiente (obrigatório, antes de abrir o kit)

Todos usam o mesmo ambiente, sem IA e sem extensões:

1. No VS Code: ⚙️ (canto inferior esquerdo) → **Perfis** → **Novo Perfil**. Nome: `Experimento`. Em "Copiar de", escolha **Nenhum** (perfil vazio, sem extensões).
2. Com esse perfil ativo: `Ctrl+Shift+P` → `Preferences: Open User Settings (JSON)` e deixe o arquivo assim:
   ```json
   {
       "chat.disableAIFeatures": true
   }
   ```
3. Abra a pasta do kit nesse perfil e confira:
   - `Ctrl+I` não abre nada;
   - não há ícone de chat ou Copilot na barra lateral nem na barra de status;
   - ao digitar, não aparece texto cinza sugerindo código.

Alternativa aceita: um editor de texto simples sem IA, como o Bloco de Notas ou o Notepad++.

O kit contém só a classe sob teste, sem o resto do projeto. Por isso o editor pode mostrar erros em referências a outras classes. **Ignore esses erros**: a classe compila normalmente no projeto completo, e eles não indicam o defeito.

## Regras

1. **Tempo:** 30 minutos por falha, contados a partir da abertura da pasta.
2. **Não execute os testes** e não execute a classe. Escreva só lendo o código e a documentação (Javadoc) que estão no arquivo.
3. **Não pesquise** a classe, o projeto ou bugs conhecidos na internet, no Defects4J ou em repositórios.
4. **Não use IA** de nenhuma forma (no editor, no navegador ou no celular) e **não use analisadores de código** (SonarLint, SpotBugs, PMD etc.).
5. Teste o **comportamento esperado** da classe, como descrito no Javadoc e nos nomes dos métodos. Escreva os testes que você acha que encontrariam defeitos.
6. Use **somente métodos públicos** da classe.

## Formato da entrega

- **JUnit 4** (`org.junit.Test`, `org.junit.Assert.*`). Não use outras bibliotecas.
- Um único arquivo chamado **`<código>Test.java`** (por exemplo `F03Test.java`), com a classe pública `<código>Test`.
- O arquivo deve declarar o **mesmo pacote** da classe sob teste (a linha `package` do arquivo da classe).
- Não altere o arquivo da classe sob teste.

## Ao terminar

- Anote o horário de início e de fim na primeira linha do arquivo, como comentário:
  `// inicio: 14:05  fim: 14:35`
- Entregue o arquivo ao curador. Depois de entregue, **o arquivo fica congelado** e não pode mais ser alterado. Correções de compilação são feitas só pelo revisor mecânico, com base na lista fechada de correções.
