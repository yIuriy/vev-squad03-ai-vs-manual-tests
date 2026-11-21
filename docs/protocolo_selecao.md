# Protocolo de seleção das falhas

> **Status: CONGELADO em 2026-10-30**, antes do sorteio. O SHA-256 deste arquivo está no log de curadoria.
> Responsável: curador (curador).

## Ambiente

- Imagem Docker `vev-d4j` ([env/Dockerfile](../env/Dockerfile)): Ubuntu 22.04, OpenJDK 11, Defects4J v3.0.1.

## Critérios de elegibilidade

| # | Critério | Como é verificado |
|---|---|---|
| C1 | Bug ativo (não depreciado) dos projetos **Lang** ou **Math** no Defects4J v3.0.1 | `defects4j query` |
| C2 | O patch altera **exatamente 1 classe** de produção | campo `classes.modified` |
| C3 | A classe modificada tem **NCLOC ≤ T** na versão com bug, com **T = 300** | [scripts/tamanho_classes.sh](../scripts/tamanho_classes.sh) |
| C4 | No máximo **1 falha por classe** na amostra | ordem do sorteio: vale a primeira sorteada |
| C5 | **Reprodutível no ambiente**: a versão com bug e a corrigida compilam; os testes gatilho falham na versão com bug; nenhum teste falha na corrigida; o resultado se repete em 2 execuções | `defects4j compile` e `defects4j test` |
| C6 | O bug é **alcançável pela API pública** da classe modificada | leitura do teste gatilho pelo curador |

C1 a C3 são automáticos e definem a **lista de elegíveis**. C4 a C6 são verificados só nas falhas sorteadas, na ordem do sorteio.

NCLOC é a contagem de linhas que não são vazias nem só comentário (aproximada, feita pelo script).

Justificativa do teto T: cada testador tem 30 minutos por falha para ler a classe e escrever a suíte sem executá-la. Classes muito grandes tornariam a tarefa inviável e favoreceriam a IA, que lê a classe inteira instantaneamente. Com T = 300, restam 16 classes distintas no Lang e 53 no Math, folga suficiente para 6 falhas e 1 piloto por projeto.

## Amostra

- **12 falhas**, estratificadas: **6 do Lang e 6 do Math**.
- **1 falha de piloto** por projeto, fora da amostra, para testar o prompt e o script de medição.

## Sorteio

1. Gerar a lista de elegíveis (C1 a C3), ordenada por projeto e ID do bug.
2. Embaralhar a lista de cada projeto com a **semente `07202620`** (usada como o inteiro 7202620), registrada aqui antes do sorteio. O embaralhamento é feito por [scripts/sorteio.py](../scripts/sorteio.py) com `random.Random(semente).shuffle` do Python 3, primeiro o Lang e depois o Math.
3. Percorrer a lista embaralhada na ordem e aplicar C4 a C6. As **6 primeiras** que passarem entram na amostra, e a **7ª** vira a falha de piloto.
4. Toda falha descartada é registrada no log com o critério que a eliminou.
5. Atribuir os códigos neutros **F01 a F12** em ordem aleatória (mesma semente). A tabela código ↔ bug fica só com o curador até o fim da avaliação.

## Registro

- O log de curadoria registra data, comando executado e resultado de cada passo.
- O SHA-256 deste protocolo, da lista de elegíveis e do resultado do sorteio entra no log no momento do congelamento.
