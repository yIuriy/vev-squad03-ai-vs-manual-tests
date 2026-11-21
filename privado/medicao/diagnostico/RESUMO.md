# Diagnóstico complementar da medição (2026-11-21)

Análise feita depois da medição final, sobre as mesmas 24 suítes. **Não altera nenhum número das Tabelas 3 e 4**: só lê as suítes congeladas e os arquivos `jacoco-*-1.exec` gravados pelo `medir.sh`.

- [scripts/diagnosticar.sh](../../../scripts/diagnosticar.sh) (com [scripts/Diagnostico.java](../../../scripts/Diagnostico.java)): por suíte, a mensagem de falha de cada teste que não foi classificado como PASSA, nas duas versões, e quantas linhas alteradas pelo patch são executáveis e foram executadas.
- [scripts/resumo_diagnostico.py](../../../scripts/resumo_diagnostico.py): junta tudo em `cobertura_patch.csv` e `falhas.csv` e imprime o resumo abaixo.

Linhas do patch: comparação da classe sob teste entre a versão com bug e a corrigida (`diff`). Na versão corrigida contam as linhas novas ou modificadas; na versão com bug, as removidas ou modificadas. Uma linha é executável quando o JaCoCo a instrumenta.

## 1. A suíte executou as linhas do patch?

| | Todas | Parte | Nenhuma | Detectou |
|---|---|---|---|---|
| Humano | 2 (F06, F11) | 1 (F09) | 9 | 1 (F11) |
| IA | 8 (F01, F02, F03, F04, F06, F07, F10, F12) | 3 (F05, F09, F11) | 1 (F08) | 0 |

As suítes de IA executaram todas as linhas do patch em 8 das 12 falhas e não detectaram nenhuma. Ou seja, nesses 8 casos, o código defeituoso foi executado, mas nenhum teste verificou o resultado que o defeito altera. Nas suítes humanas, a não detecção se explica em geral pelo alcance: 9 das 12 nem chegaram às linhas do patch.

## 2. Falsos alarmes

Falso alarme: teste que falha na versão corrigida nas 3 repetições. Os 11 contados nas Tabelas 3 e 4 (8 humanos, 3 de IA):

| Falha | Suíte | Teste | Mensagem na versão corrigida | Na versão com bug | Causa |
|---|---|---|---|---|---|
| F01 | IA | `testSubsequentStartsUseGeneratorBoundedMinAndMax` | `expected:<12.0> but was:<10.0>` | passa | **Oráculo invertido**: o teste afirma que os limites das partidas seguintes são sorteados (12 e 18), justamente o comportamento que o patch do Math-62 corrige. Esse comportamento não está documentado no Javadoc; o teste afirma os valores que o código com bug calcula. |
| F12 | IA | `testAddNonComparableObjectThrowsIllegalArgumentException` | esperava `IllegalArgumentException`, veio `ClassCastException` | passa | **Oráculo invertido**: o teste espera a exceção que a versão com bug lança. O Javadoc da versão com bug admite essa leitura: declara `IllegalArgumentException` para valor não comparável com as entradas anteriores e `ClassCastException` para valor que não é `Comparable`; um `new Object()` se encaixa nos dois casos, e a correção escolheu o segundo. |
| F08 | IA | `testCustomFormatWithArgumentsParsedAndPassedToFactory` | `expected:<Result: TAG:[ ]message> but was:<Result: TAG:[]message>` | falha igual | Valor esperado errado: supõe que o espaço final do argumento do formato é preservado. |
| F02 | Humano | `testGetDenominator` | `expected:<4> but was:<3>` | falha igual | Valor esperado errado (denominador de 2/3). |
| F02 | Humano | `testIntValue` | `expected:<2> but was:<0>` | falha igual | Valor esperado errado (parte inteira de 2/3). |
| F02 | Humano | `testFractionInDouble` | `Use assertEquals(expected, actual, delta)…` | falha igual | Uso incorreto do JUnit (`assertEquals` de `double` sem delta) e valor esperado errado (2.5 para 2/3). |
| F03 | Humano | `tempoAtualValido` | `AssertionError` | falha igual | Suposição de tempo: espera mais de 2 ms decorridos logo depois de `start()`. |
| F03 | Humano | `testeSuspende` | `IllegalMonitorStateException` | falha igual | Uso incorreto de Java: `wait()` fora de bloco `synchronized`. |
| F05 | Humano | `testeNormalizacaoZero` | esperava `ArithmeticException`, veio `MathArithmeticException` | falha igual | Exceção esperada errada. |
| F11 | Humano | `leavesUnterminatedEntityUnchanged` | `StringIndexOutOfBoundsException` em `NumericEntityUnescaper.translate` | falha igual | A classe sob teste lança exceção com uma entidade sem `;` nas duas versões. Pode ser outro defeito da classe, fora do patch do Lang-28; **não verificado** no histórico do projeto. |
| F12 | Humano | `getSumFreqTest` | `expected:<3> but was:<25>` | falha igual | Valor esperado errado: espera a soma dos valores (25), mas `getSumFreq()` retorna o total de ocorrências (3). |

Fora da tabela: o F01 humano não tem nenhum `@Test` ativo, e o JUnit gerou um `initializationError` ("No runnable methods"). O `consolidar.py` já o conta como 0 testes e 0 falsos alarmes.

## 3. O que isso permite e não permite dizer

- **Permite:** nesta amostra, a cobertura alta da IA veio de executar o código, inclusive as linhas defeituosas em 8 das 12 falhas, e não de verificar o comportamento que o defeito altera. Em 2 falhas (F01, F12), os testes de IA distinguiram as duas versões, mas tomaram o comportamento com bug como o correto, o que é coerente com oráculos derivados do código recebido.
- **Não permite:** generalizar para outros modelos, prompts ou execuções (uma geração por falha); afirmar que a IA "memoriza" os oráculos; afirmar que o falso alarme do F11 humano é um defeito real sem conferir o histórico do Commons Lang.
- Os oráculos invertidos **não contam como detecção** pelo critério do protocolo (o teste precisa falhar na versão com bug e passar na corrigida). A FDR da IA continua 0/12.
