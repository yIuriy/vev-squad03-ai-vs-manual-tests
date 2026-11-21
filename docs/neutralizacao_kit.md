# Neutralização das classes do kit

> Decisão do curador (curador) em 2026-10-30, antes da distribuição do kit.

## Motivo

O código original dos projetos tem comentários escritos pelos desenvolvedores, como `TODO`, `XXX` e links para issues do JIRA. Alguns deles **descrevem o defeito ou apontam a issue que o corrigiu**. Na primeira inspeção, foram encontrados comentários desse tipo em classes da amostra.

Essas pistas não fazem parte da especificação da classe e favoreceriam quem lê todos os comentários com atenção. A IA faz isso sempre, e o testador em 30 minutos nem sempre. Para não escolher caso a caso o que remover (o que exigiria olhar o defeito de cada classe), foi definida uma **regra única aplicada igualmente às 14 classes do kit** (F01 a F12, P01 e P02).

## Regra

Aplicada por [scripts/Neutralizar.java](../scripts/Neutralizar.java), chamado por [scripts/gerar_kit.sh](../scripts/gerar_kit.sh):

1. **Todos os comentários de linha (`//`) são removidos.** Se a linha fica vazia, a linha inteira sai.
2. **Dentro de comentários de bloco e Javadoc, referências a issues são trocadas por `[ref. removida]`**: identificadores `LANG-<n>` e `MATH-<n>` e links `issues.apache.org`.
3. **Nada mais muda.** O Javadoc, o cabeçalho de licença, o código e as strings ficam exatamente como no original.

Os testadores são avisados da regra no `INSTRUCOES.md` do kit.

## Verificação

- As 14 classes neutralizadas compilam, e o **bytecode gerado é idêntico** ao da classe original (comparado com `javac -g:none` e `javap -c -p -v`). A regra só mexeu em comentários.
- Depois da neutralização, nenhuma classe contém `TODO`, `FIXME`, `XXX`, `LANG-<n>`, `MATH-<n>` ou `issues.apache.org`.

## O que foi removido

| Falha | Comentários `//` removidos | Referências a issues trocadas |
|---|---|---|
| F01 | 2 | 0 |
| F02 | 22 | 2 |
| F03 | 2 | 0 |
| F04 | 12 | 0 |
| F05 | 13 | 0 |
| F06 | 17 | 0 |
| F07 | 47 | 0 |
| F08 | 6 | 0 |
| F09 | 14 | 1 |
| F10 | 12 | 0 |
| F11 | 1 | 0 |
| F12 | 9 | 0 |
| P01 | 1 | 1 |
| P02 | 2 | 0 |

A lista completa, com a linha original e o texto exato de cada remoção, fica com o curador até o fim da avaliação, porque revelaria detalhes das classes antes da sessão. Ela será publicada junto com os demais arquivos do curador, com o SHA-256 registrado no log.
