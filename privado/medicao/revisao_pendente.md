# Revisão mecânica pendente (rodada 1 da medição, 2026-11-14)


> **Atualização:** por decisão do curador, todas as revisões são feitas por ele (Iuri). A coluna "Revisor" abaixo fica só como referência do plano original.
9 das 24 suítes não compilam. Revisor = o próximo na roda (Dyonathan → Lara → Tamires → Rafaela → Sidnei → Dyonathan); suíte de IA: qualquer pessoa que não seja o operador.

| Suíte | Autor | Revisor | Erros do compilador (versão com bug) | Categoria provável |
|---|---|---|---|---|
| F01 humano | Sidnei | Rafaela | `generator.getSeed(42L)`: método inexistente; `new JKDRandomGenerator()`: classe inexistente (erro de digitação) | fora da lista → comentar métodos |
| F02 humano | Lara | Dyonathan | `import.org.junit.Bafore` (sintaxe e nome); `catch (ArithmeticException)` sem variável | pontuação e import; o `catch` sem variável provavelmente vira método comentado |
| F03 humano | Rafaela | Tamires | `public String numero = ...` dentro de método; trecho com chaves que quebra a classe | pontuação; o que não resolver → comentar |
| F04 humano | Rafaela | Tamires | `@Before` sem import | import |
| F04 ia | operador Sidnei | Lara | `FastMath.nextDown(...)` não existe nessa versão | fora da lista → comentar método |
| F05 humano | Tamires | Lara | `Vector3D(-1, -2, -3)` sem `new` | fora da lista → comentar métodos |
| F09 humano | Lara | Dyonathan | `byte bytes = SerializationUtils.serialize(...)` (devolve `byte[]`) | revisor decide (cast ou comentar) |
| F10 humano | Lara | Dyonathan | `new locale("pt")` (minúsculo; classe `Locale`) | revisor decide (import/assinatura ou comentar) |
| F12 humano | Dyonathan | Sidnei | `assertEquals(frequencia, freq.getSumFreq())` ambíguo | cast |

A "categoria provável" é só uma sugestão do curador: a decisão é do revisor, dentro da lista fechada.
