# Revisão mecânica pendente (rodada 1 da medição, 2026-11-14)


> **Atualização:** por decisão do curador, todas as revisões são feitas por ele (curador). A coluna "Revisor" abaixo fica só como referência do plano original.
9 das 24 suítes não compilam. Revisor = o próximo na roda (T4 → T1 → T2 → T5 → T3 → T4); suíte de IA: qualquer pessoa que não seja o operador.

| Suíte | Autor | Revisor | Erros do compilador (versão com bug) | Categoria provável |
|---|---|---|---|---|
| F01 humano | T3 | T5 | `generator.getSeed(42L)`: método inexistente; `new JKDRandomGenerator()`: classe inexistente (erro de digitação) | fora da lista → comentar métodos |
| F02 humano | T1 | T4 | `import.org.junit.Bafore` (sintaxe e nome); `catch (ArithmeticException)` sem variável | pontuação e import; o `catch` sem variável provavelmente vira método comentado |
| F03 humano | T5 | T2 | `public String numero = ...` dentro de método; trecho com chaves que quebra a classe | pontuação; o que não resolver → comentar |
| F04 humano | T5 | T2 | `@Before` sem import | import |
| F04 ia | operador T3 | T1 | `FastMath.nextDown(...)` não existe nessa versão | fora da lista → comentar método |
| F05 humano | T2 | T1 | `Vector3D(-1, -2, -3)` sem `new` | fora da lista → comentar métodos |
| F09 humano | T1 | T4 | `byte bytes = SerializationUtils.serialize(...)` (devolve `byte[]`) | revisor decide (cast ou comentar) |
| F10 humano | T1 | T4 | `new locale("pt")` (minúsculo; classe `Locale`) | revisor decide (import/assinatura ou comentar) |
| F12 humano | T4 | T3 | `assertEquals(frequencia, freq.getSumFreq())` ambíguo | cast |

A "categoria provável" é só uma sugestão do curador: a decisão é do revisor, dentro da lista fechada.
