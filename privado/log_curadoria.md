# Log de curadoria

Responsável: curador (curador). Este arquivo fica fora do repositório compartilhado até o fim da avaliação.

## 2026-10-30 15:47 — Congelamento do protocolo (antes do sorteio)

- Ambiente: imagem `vev-d4j` (Ubuntu 22.04, OpenJDK 11.0.32, Defects4J v3.0.1). Teste de sanidade com Lang-1: com bug, 1 teste falhando; corrigida, 0.
- Teto C3: 300 NCLOC. Semente: `07202620` (inteiro 7202620).
- Elegíveis (C1 a C3): 20 do Lang e 63 do Math.
- Sorteio executado com Python 3.14.7 no host.

| SHA-256 | Arquivo |
|---|---|
| `d040a2ab4e5969f31821fcf3fe5594317c06ca0c7a373cad482ebe07c76853f8` | docs/protocolo_selecao.md |
| `57b205ce6214363411d542bbe7a7e1d2278376f170fc067bc311198379968574` | scripts/sorteio.py |
| `a974d9ff89477374c1496aa619c90a0eaa608c6bc3c1aed11229e2738f16eb68` | scripts/tamanho_classes.sh |
| `c1cdd3691f3080422f522c5c0c66146305099c933dcb1ca6b77c7f160abcdcf7` | env/Dockerfile |
| `3b91984486967931df35195057119bdddf4eeb639ec409602362378593914d79` | privado/tamanho_classes.csv |
| `f542e32a9223dff03850766afaacf6388e80d3b47c16297aca3d23c7f3bbaf2c` | privado/elegiveis.csv |

## 2026-10-30 — Sorteio

- Comando: `python scripts/sorteio.py privado/elegiveis.csv > privado/ordem_sorteio.csv`. Uma segunda execução gerou um arquivo idêntico.
- SHA-256 de `privado/ordem_sorteio.csv`: `705f33dad7e8d9b76f68930e37d4e2bc631bb1bf85436a140c586dcb1ac1bc39`
- Próximo passo: aplicar C4 a C6 na ordem do sorteio.

## 2026-10-30 — Esclarecimento de interpretação do C4 (antes da validação)

- O Commons Lang mudou de pacote entre versões (`org.apache.commons.lang` → `org.apache.commons.lang3`).
- Decisão do curador: para o C4, a classe é identificada pelo **nome simples, sem pacote**. Exemplo: `lang.text.ExtendedMessageFormat` e `lang3.text.ExtendedMessageFormat` contam como a mesma classe.
- Motivo: evitar que a amostra tenha duas falhas na mesma classe em versões diferentes do projeto. É a interpretação mais conservadora.
- Registrado antes de qualquer validação C5 ou C6.

## 2026-10-30 — Validação C5

- Comando: `scripts/validar.sh` nas posições 1 a 13 do Lang e 1 a 12 do Math (fila em `privado/fila_validacao.txt`). Resultado em `privado/validacao.csv`.
- **Todas as 25 foram APROVADAS no C5**: as duas versões compilam, a versão com bug falha exatamente nos testes gatilho nas 2 execuções e a versão corrigida tem 0 falhas nas 2 execuções.

## 2026-10-30 — C4 e C6 na ordem do sorteio

- C4: Lang-19 (posição 6) **descartada**, porque `NumericEntityUnescaper` já entrou com Lang-28 (posição 4).
- C6: Math-83 (posição 1) **descartada**, porque `SimplexTableau` não é pública (visibilidade de pacote). O bug só é alcançável por `SimplexSolver`, outra classe.
- C6 pendente: Lang-6 (`CharSequenceTranslator`), Lang-64 (`ValuedEnum`) e Math-39 (`EmbeddedRungeKuttaIntegrator`) são **classes abstratas públicas**. A decisão está registrada na próxima entrada.

## 2026-10-30 — Esclarecimento de interpretação do C6: classes abstratas

- Decisão do curador: uma **classe abstrata não atende ao C6**, porque não pode ser instanciada diretamente pelo testador. O bug só seria alcançado por meio de uma subclasse, que é outra classe.
- Descartadas pelo C6: Lang-6 (`CharSequenceTranslator`), Lang-64 (`ValuedEnum`), Math-39 (`EmbeddedRungeKuttaIntegrator`). Também ficam descartadas, se chegarem na ordem, Lang-17 (`CharSequenceTranslator`) e Math-51 (`BaseSecantSolver`).

## 2026-10-30 — Fechamento da amostra

Percurso na ordem do sorteio (✓ = entra, ✗ = descartada):

- **Lang:** 1 Lang-43 ✓ · 2 Lang-6 ✗ C6 abstrata · 3 Lang-51 ✓ · 4 Lang-28 ✓ · 5 Lang-13 ✓ · 6 Lang-19 ✗ C4 · 7 Lang-55 ✓ · 8 Lang-64 ✗ C6 abstrata · 9 Lang-29 ✗ C6 (`toJavaVersionInt` tem visibilidade de pacote, e o valor público vem de propriedade do sistema) · 10 Lang-23 ✗ C4 · 11 Lang-17 ✗ C6 abstrata · 12 Lang-57 ✓ · 13 Lang-54 ✗ C4 · 14 Lang-4 ✓ **piloto**
- **Math:** 1 Math-83 ✗ C6 · 2 Math-90 ✓ · 3 Math-91 ✓ · 4 Math-23 ✓ · 5 Math-55 ✓ · 6 Math-62 ✓ · 7 Math-39 ✗ C6 abstrata · 8 Math-57 ✓ (método privado alcançado por `cluster`, que é público) · 9 Math-51 ✗ C6 abstrata · 10 Math-58 ✓ **piloto**
- As posições 14 a 16 do Lang (Lang-4, Lang-52, Lang-45) foram validadas no C5 depois, todas APROVADAS. Só a 14 foi usada.
- Códigos F01 a F12 atribuídos por `python scripts/codigos.py privado/amostra_sem_codigo.csv > privado/amostra.csv` (mesma semente). Pilotos: P01 = Lang-4, P02 = Math-58.

| SHA-256 | Arquivo |
|---|---|
| `3935dafefde89c0aa2f19d138255e006606e89411b829c1b4978052c6a94b221` | scripts/validar.sh |
| `6ba1297161a96f91e114457e7a282762b9852527354b023eacc921e27017cec2` | scripts/codigos.py |
| `60ffbf8bf73b494783043b6cba55c50520f47b3f5c15685f3212c25982097d5e` | privado/validacao.csv |
| `f8ba021989d782310842808fe72fb7e0487cec46275736d639835c7b81e51b82` | privado/amostra.csv |
| `7423e95eb8f69e6b568d3757fb8e6925b8ed2e6e29030dab1cb5a4271cd87546` | privado/piloto.csv |

## 2026-10-30 — Script de medição

- JaCoCo 0.8.12 adicionado ao `env/Dockerfile` e imagem reconstruída. O hash do Dockerfile registrado no congelamento muda a partir daqui, mas as camadas do Defects4J continuam as mesmas.
- `scripts/medir.sh` e `scripts/Executor.java` criados e testados nos pilotos com suítes de sanidade (em `privado/sanidade/`):
  - P01 original: 2 testes, detecta (só `testLang882`). Linhas 24/25 e branches 10/14 na versão com bug.
  - P02 original: 8 testes, detecta (só `testMath519`). Linhas 64/96 e branches 36/58 na versão com bug.
  - P01 e P02 trivial: não detectam.
  - P01 não compila: marcada como não compila. P01 falso alarme: 1 FALSO_ALARME, não detecta.
- Correção durante o teste: as classes internas eram passadas ao JaCoCo sem `--classfiles` (erro "Invalid execution data file" no P02).

## 2026-10-30 — Congelamento do protocolo de medição

- Definição de detecção aprovada pelo curador: a suíte detecta se tem pelo menos 1 teste que falha na versão com bug e passa na corrigida, nas 3 repetições. Falsos alarmes e instáveis são contados à parte e não anulam a detecção. A cobertura principal é medida na versão com bug.

| SHA-256 | Arquivo |
|---|---|
| `8ced620983e76ab344bbd7aa9e691cf28bd49563ca61e5877c6f6152cfe92ab0` | docs/protocolo_medicao.md |
| `ef3672808b0953310ae230ac30370211348c727703a7ac2dca60cf1039d93516` | scripts/medir.sh |
| `3dd09e3292e45a0c4c83807085a17d10df07b8cc63be0e73e39bdb4a9dc9e88a` | scripts/Executor.java |
| `74398bc4dca722d1b5d3e95c8df3365039da8456bf35b401d2c8d79fc2154d88` | env/Dockerfile (com JaCoCo) |

## 2026-10-30 — Kit: primeira geração (não distribuída)

- `scripts/gerar_kit.sh` gerou `kit/` com a classe sob teste (versão com bug) e o LEIA-ME de F01 a F12, P01 e P02.
- A busca por pistas no código encontrou comentários que apontam o bug:
  - F11 (Lang-28), linha 63: `// TODO: if(entityValue > 0xFFFF) {`, que é exatamente a correção.
  - F01 (Math-62), linhas 185 e 186: comentários `XXX` dizendo que `startValue` deveria ser usado, que é o bug.
  - P01 (Lang-4), Javadoc: referência a LANG-882, a issue do próprio bug.
  - F02 e F09: referências a issues antigas (MATH-181, LANG-626), sem relação aparente com o bug.
- O kit **não foi distribuído**. Aguardando decisão sobre a neutralização dos comentários.

## 2026-10-30 — Kit: neutralização e correção da versão da classe

- **Decisão do curador:** aplicar a regra uniforme de neutralização a todas as classes do kit (remover comentários `//` e trocar referências a issues no Javadoc por `[ref. removida]`). Regra e contagens em `docs/neutralizacao_kit.md`; texto exato de cada remoção em `privado/kit_remocoes.tsv`.
- **Erro encontrado e corrigido no gerador do kit:** a primeira geração extraía a classe do commit original (`revision.id.buggy`). A versão com bug do Defects4J (checkout `b`) é a versão corrigida com o patch revertido, e pode diferir desse commit. No F02, o método `compareTo` da primeira geração era diferente do que é medido. O gerador passou a extrair a classe do checkout `b`.
  - Com a versão correta, os comentários que entregavam o bug no F11 (`TODO: if(entityValue > 0xFFFF)`) e no F01 (`XXX ... startValue`) **não existem**: eles eram do commit original. A referência LANG-882 do P01 existe e foi trocada pela regra.
  - Verificação: as 14 classes neutralizadas compilam e geram bytecode idêntico ao da classe do checkout `b`. Não sobrou `TODO`, `FIXME`, `XXX` nem referência a issue.
- **Desvio registrado no C3:** `scripts/tamanho_classes.sh` mediu o NCLOC no commit original, não no checkout `b`. A lista de elegíveis segue a medida definida e congelada no protocolo. Remedido no checkout `b`, todas as 14 classes da amostra e dos pilotos continuam ≤ 300 NCLOC (maior: F07, 291). Bugs fora da amostra com NCLOC perto de 300 podem ter sido classificados de forma diferente; isso fica como limitação no relatório.

| SHA-256 | Arquivo |
|---|---|
| `9279add5c02330d34a6b9c7bf3c694a1daf87781d5f702bece5963ba5181060c` | docs/neutralizacao_kit.md |
| `d795c8d9c523a52fecf90932a1ca27c543cae05774d96a709ad45b4f1b6e6096` | docs/instrucoes_kit.md |
| `d0be6b25f37b39ca16cfd3c768959b7fd84520af49e25b4905a3950c1eb305e4` | scripts/Neutralizar.java |
| `29429627182e2c864d92a9e167ad9030c6baf7c4cd067cfb520be3c951b1beec` | scripts/gerar_kit.sh |
| `4059fb73f68fb0149f820c60865faef5d8d96554589c1eb42d1c99afbdb8ed0b` | kit/SHA256SUMS (hash de cada arquivo do kit) |
| `ee218bfae9c58122e1d92586a6f7f13463019fd9e01136e2c43c1aaa8269980c` | privado/kit_remocoes.tsv |

## 2026-10-30 — Distribuição das falhas entre as pessoas

- Comando: `python scripts/distribuir.py privado/amostra.csv kit privado/distribuicao` (semente 7202620). Uma segunda execução gerou a mesma distribuição.
- Restrições: 1 suíte humana por falha. Quem tem cota 2 recebe 1 Lang + 1 Math, e os dois de cota 3 ficam com 2+1 e 1+2. Cada operador de IA recebe 2 Lang + 2 Math e nunca uma falha em que escreveu a suíte humana. Conferido: 0 conflitos.

| Pessoa | Suítes humanas (S2) | Execuções de IA (S3) |
|---|---|---|
| T4 | F07, F12 | F01, F06, F08, F09 |
| T1 | F02, F09, F10 | — |
| T2 | F05, F06, F08 | — |
| T5 | F03, F04 | F02, F10, F11, F12 |
| T3 | F01, F11 | F03, F04, F05, F07 |

- Aquecimento (S1): `aquecimento.zip` com P01, para todos os testadores.

| SHA-256 | Arquivo |
|---|---|
| `6a41d5139241b062523bf5561e1e0e6f33acde9071440b115a5075524c2c9fc6` | scripts/distribuir.py |
| `c02c0e263d0e8596b7c961cf210e14507a10715d831cd8e329698e5d9c0cb81f` | privado/distribuicao/distribuicao.csv |
| `756898838122e482d331ec6e527adb3aeaa5d4e871cffc6cbae6eb9d218448c3` | privado/distribuicao/aquecimento.zip |
| `649ccec19d98aa6c9020c1b39472423c559327ee13c6c2403596a40e4add70e5` | privado/distribuicao/humano_T4.zip |
| `3231e7f7bd333e913d42f3b1f9b6e2d75bca3c1d63a44bbcfbc9b1e103f8543b` | privado/distribuicao/humano_T1.zip |
| `f3d9815cdd3730d9d003ed6d61de328b47ad42a393ab9ffc3e2d3b7e684f03b7` | privado/distribuicao/humano_T5.zip |
| `01068c34376870814aae3338f3683177b42b6bc79b362ef37c1c6f47b7923f47` | privado/distribuicao/humano_T3.zip |
| `618f68e27151edb59927e88f97131fa575ec5bc82ea5792f7758abece7573d48` | privado/distribuicao/humano_T2.zip |
| `0c5a32f64865a7935d36bb6ebcc6d771a490332cf0a63f75b10221b48aec15bb` | privado/distribuicao/ia_T4.zip |
| `8634c58efbc7683848b6b94f39e48ce3b7506f81e0c6d8333ee87eeeb15825e8` | privado/distribuicao/ia_T5.zip |
| `417fe58fa581ca16c708524711d2210ece3166f331ca5e36ac549c29c7661f89` | privado/distribuicao/ia_T3.zip |

## 2026-10-30 — Instruções: ambiente controlado (kit e zips regerados antes da distribuição)

- Motivo: o editor dos testadores pode ter IA (Copilot e chat embutido) e analisadores (a extensão Java marca erros e faz análise de `null`), que dariam pistas ou contaminariam a suíte humana. Além disso, o editor mostra falsos erros de compilação, porque o kit não inclui o resto do projeto.
- Decisão do curador: todos usam um perfil vazio do VS Code (`Experimento`, sem extensões) com `"chat.disableAIFeatures": true`, ou um editor de texto simples. IA e analisadores são proibidos e os falsos erros devem ser ignorados. Testado pelo curador no VS Code dele: com a configuração, `Ctrl+I` não abre nada.
- Também ficou explícito que as instruções valem para as suítes humanas (os zips de IA levam o mesmo arquivo).
- Kit e zips regerados. As classes e o relatório de remoções não mudaram (`kit_remocoes.tsv` idêntico), e a distribuição é a mesma (`distribuicao.csv` com o mesmo hash). Os hashes abaixo **substituem** os da entrada anterior para os zips.

| SHA-256 | Arquivo |
|---|---|
| `f509d6b0cc1976e975fba04b81534cc5794801106ed76530ff3aef1d539b6ab1` | docs/instrucoes_kit.md |
| `74319f65f7e8e0e2910e6850e479011ae2bef4baeea00a4ab6f7588f7f35e1d0` | kit/SHA256SUMS |
| `c118e0ba46d10752114c83c7c0a03eafbab69126c37d78b091d382fcca9cbbc1` | privado/distribuicao/aquecimento.zip |
| `ff620106db8977658caf33c014aa03f86cb8f1c3ca765dddc2df66f551d042c1` | privado/distribuicao/humano_T4.zip |
| `e118708194ba18ca7d206a86efba6eb2ada5661c9c9d1b74deecd9365e0aef9d` | privado/distribuicao/humano_T1.zip |
| `18714bb3690b56d7420a6e71beb52c279a8399fffd8a4e700c0abec0e8b03207` | privado/distribuicao/humano_T5.zip |
| `6b39d7603d374a3403cf1766a9516f87a3858f4580ad67b592f4dcba7d4bce12` | privado/distribuicao/humano_T3.zip |
| `d7292fef567ba6bf67b58945b3f0ebe412c745a7c61ba698fce55a28d9d76a81` | privado/distribuicao/humano_T2.zip |
| `8903d257723d966d2edcacaba14a219cb0a75c9ecbeda073230eeb52bdbac5de` | privado/distribuicao/ia_T4.zip |
| `c1a41ef12c403429376a31b7f92a36c3c18c70a24769799a025f5695fb85917f` | privado/distribuicao/ia_T5.zip |
| `b7af1f2edbcba0b7ec40bba6cb150b35d423d50dff1b5cde2d8e4cd66e5b11a3` | privado/distribuicao/ia_T3.zip |

## 2026-10-30 — Alinhamento ao artigo (antes da S2; kit e zips regerados)

Leitura do rascunho do artigo (seções 3.4 a 3.6) mostrou divergências com o que tinha sido preparado. Decisão do curador: alinhar ao artigo.

- **Protocolo de medição (emenda):** a cobertura principal passa a ser medida na versão corrigida (artigo 3.4). Os métodos que continuam sem compilar após a revisão mecânica são comentados. Nenhuma suíte tinha sido medida, e `scripts/medir.sh` não muda (já registra as duas versões).
- **IDE com feedback do compilador (artigo 3.5):** o perfil `Experimento` deixa de ser vazio e passa a ter só a extensão `redhat.java`, com IA desativada, análise de `null` desligada e sem Test Runner (`docs/perfil_vscode.md`). Isso substitui a decisão anterior de perfil sem nenhuma extensão.
- **Kit compilável:** cada falha virou um projeto com `src/` (classe neutralizada), `test/` (esqueleto `<código>Test.java`), `lib/` (`dependencias.jar` com o resto do projeto compilado da versão com bug, **sem a classe sob teste**, e o JUnit 4.12) e `.vscode/` (fontes, bibliotecas, nível Java 11 e avisos do compilador desligados).
  - Verificação: as 14 pastas compilam com `javac --release 11` usando só `lib/`. A classe sob teste não está no jar. As classes e o relatório de remoções não mudaram.
- **Esqueleto e cola (artigo 3.5):** esqueleto sem sugestão de métodos, e `JUNIT.md` com asserts, exceções, `@Before` e tolerância de `double`.
- **Instruções:** "um comportamento por método", foco em valores-limite, caminhos normais e erros, uma falha por vez, e o tempo conta a partir da abertura da classe.
- Distribuição inalterada (`distribuicao.csv` com o mesmo hash). Os hashes abaixo **substituem** os anteriores do kit e dos zips.
- Pendente: o curador confirmar no VS Code que o `.vscode/java.prefs` desliga os avisos e que o projeto abre sem erros.

| SHA-256 | Arquivo |
|---|---|
| `3faba166b7e1538fe990dbf61741ed52803f60008a345a6acfa6a90b2d9e7b78` | docs/protocolo_medicao.md (com emenda) |
| `a2b5a81b4e96845bc5650d25a09281b7183c014a150e950f7e68b6e5a1b8937d` | docs/instrucoes_kit.md |
| `90b64c06ac0a893f16fb088533db5b4f6ba731e49a3e3cf80f1181315e1f333c` | docs/perfil_vscode.md |
| `1914221eaf53d2fe3e3ecefd0be9eac5ffe58db3a99dbe25cf79585d2c289b88` | docs/junit_cola.md |
| `47f2e5ca9addcc38e460610c39e3d81a99196f29591cda1230062f304a2725e0` | scripts/gerar_kit.sh |
| `339866dd0a148fd8fabce14cfcf6d6cfa0d1c8e9634b2b095652f9a60fcaa697` | scripts/distribuir.py |
| `244ae171be993ec9b2ce2a440912a8678411a0859f40d5da83c1532145ae4cab` | kit/SHA256SUMS |
| `9198a5799ffed33d6a3a8ca3aa55a117cbc8d8539016d1d2065cb7330ffed934` | privado/distribuicao/aquecimento.zip |
| `a1954dc7683de3c7288225f36fbb43bbd0a718417571b798bfb3e94d3686a659` | privado/distribuicao/humano_T4.zip |
| `263d363187fc75087910ada0acc25abce4e8a1f6dfef7e471542655e31ebd05f` | privado/distribuicao/humano_T1.zip |
| `5e45dc1f07fd12e6fad9e8cc989fe60e2ad4db085d26865fb63d51e2badaf294` | privado/distribuicao/humano_T5.zip |
| `a2542f9f2893b85fe687033f3ce456fa6d1c42508e288ddf811a529e5aeb0711` | privado/distribuicao/humano_T3.zip |
| `a6a65da02bdec7dadd05c74fa30ba615a3e3525fdf0c46d353122f6ea991d9be` | privado/distribuicao/humano_T2.zip |
| `3039ff253caeeee4cf99c2d5a2fb9f4198696b869e8eee623222544cec1df0f3` | privado/distribuicao/ia_T4.zip |
| `cbc73909f20c0ffc24464c9745e5be547e5098b2aee3c2ad87fb1fb04fbf00e0` | privado/distribuicao/ia_T5.zip |
| `097fc5429df53dbc291186cea3ff35f984c8b4c4c7c64530e78e3c1a47c19157` | privado/distribuicao/ia_T3.zip |

## 2026-10-30 — Ensaio do processo completo com P01 (não é dado do experimento)

- Objetivo: testar o fluxo entrega → congelamento → revisão mecânica → medição antes da S2.
- O curador abriu `kit/P01` no perfil `Experimento` (só `redhat.java`). A suíte de ensaio foi **escrita pelo Claude**, a pedido do curador, e **não é cega**: quem escreveu conhecia o defeito. A linha de horário do arquivo é fictícia. Serve só para testar o processo.
- Entrega: `scripts/receber.py` congelou `privado/entregas/ensaio/P01/Claude/P01Test.java` (SHA-256 `f28c3c3c13aac713bb6c06fab388941b159ff6ff0767b477185bae384665328d`) e recusou uma segunda entrega para o mesmo destino.
- Medição da entrega bruta: não compila (`StringWriter` sem import, deixado de propósito).
- Revisão mecânica (categoria "import faltando"): cópia revisada em `privado/revisado/ensaio/P01/Claude/`, com o registro em `privado/revisado/revisao.csv` (hash do original e do revisado).
- Medição da versão revisada: 6 testes, detecta (1 DETECTA), 0 falsos alarmes, 0 instáveis. Cobertura na versão corrigida: linhas 24/25 e branches 11/14.
- Lições:
  1. O fluxo funciona de ponta a ponta.
  2. A classe do P01 depende de um método herdado (`translate(CharSequence)`) que não aparece no arquivo. Sugestão: dica no `JUNIT.md` sobre `Ctrl+Espaço` para ver os métodos herdados.
  3. A revisão mecânica foi feita à mão. Para a S3, vale um script que copie, registre e congele a versão revisada.

## 2026-10-30 — Melhorias do ensaio (kit e zips regerados)

- `JUNIT.md`: nova seção sobre métodos herdados (`Ctrl+Espaço` depois do ponto).
- `scripts/revisar.py`: revisão mecânica em três passos (preparar, registrar, congelar), com categorias da lista fechada (import, cast, assinatura, throws, pontuacao, metodo_comentado) e recusa de revisor que seja o autor. Para a condição de IA, o autor é o operador da falha. Testado no ensaio (`privado/entregas/ensaio/P01/teste_script`): recusou o autor, recusou a categoria "assert", registrou o import, congelou, e a medição deu o mesmo resultado do ensaio manual.
- `scripts/receber.py` e `scripts/revisar.py` passaram a gravar em UTF-8. Uma linha de `privado/revisado/revisao.csv` gravada em cp1252 durante o teste foi convertida para UTF-8 (conteúdo igual).
- Classes, relatório de remoções e distribuição inalterados. Os hashes abaixo **substituem** os anteriores do kit e dos zips.

| SHA-256 | Arquivo |
|---|---|
| `9576a8f18bca0183c8a2a7ff19a44044d2c4b86034eb7e740ea49c27843a54ef` | docs/junit_cola.md |
| `2f4a158a6ea67200c7538202422930b55e10fb6d8e1d50e5abce9144850193c0` | scripts/receber.py |
| `95e6217b730c401151b23a9aa1615dde739e2cadd9b8e4558d4171a575d76508` | scripts/revisar.py |
| `0cba88eec682a043e9ee85b2524247faca51858fc30bf67810b0214c24c04525` | kit/SHA256SUMS |
| `4980fb181c47bfeb93e622cf65a14e5bd0f800138c02cb77b08e06be600051a1` | privado/distribuicao/aquecimento.zip |
| `4bab5195fde48404b0902018091127cd2e7d108f053d1e1c22cdab33e2c83892` | privado/distribuicao/humano_T4.zip |
| `086dc0e7208ca3b000e143f42b2ff8bdee26ea4eaa40087af765e675bdbf97b4` | privado/distribuicao/humano_T1.zip |
| `2d4a6b90a1476955c734bd67c66e1d3fd1e90409207ffe718469fa519c847f96` | privado/distribuicao/humano_T5.zip |
| `53b0e9941c5d09ad7f9c04858f63051f2daffc89cd2cba740b0378925eb896cd` | privado/distribuicao/humano_T3.zip |
| `8d5b7b2eae084183b62e92fb5b74bfa2173e644cd6725cd547cbe00b15201c76` | privado/distribuicao/humano_T2.zip |
| `f4ba60460aee0d90a72aa434b375037b2e690142afd1c54528e366b2115f8032` | privado/distribuicao/ia_T4.zip |
| `cbb3313a8324b840067d8c3f5d12c1ec04b00faa70f4512e694898dd5a2c530c` | privado/distribuicao/ia_T5.zip |
| `652c7ba3c94949c4b1b9e449f3642107314097adf3a0f5beb681cf7db643a650` | privado/distribuicao/ia_T3.zip |

## 2026-10-30 — Zip por falha, com ordem sorteada

- Decisão do curador: cada pessoa recebe **um zip por falha**, e o próximo só é enviado depois que a falha anterior é entregue. Isso impede que alguém leia as próximas classes enquanto faz a atual.
- `scripts/distribuir.py` sorteia a ordem das falhas de cada pessoa (mesma semente, sorteada **depois** da distribuição). A distribuição continua a mesma (`distribuicao.csv` idêntico), e a ordem é reprodutível (duas execuções iguais).
- Ordem em `privado/distribuicao/ordem.csv`. Zips em `privado/distribuicao/humano/` e `privado/distribuicao/ia/`. Os zips por pessoa da entrada anterior **deixam de existir** e foram substituídos por estes.

| SHA-256 | Arquivo |
|---|---|
| `0a01fe195d660dffe8df6022728b93a4011d8421a67503164b2a92f12e85bade` | scripts/distribuir.py |
| `6806cbaa5d7998d984a8ee6437086b09338561a311a768a18793f802488ff9c8` | privado/distribuicao/ordem.csv |
| `4980fb181c47bfeb93e622cf65a14e5bd0f800138c02cb77b08e06be600051a1` | privado/distribuicao/aquecimento.zip |
| `4901a43e4dcbff7dbfb4c35b799b0e1fc3643fb27dce76e0b1ece947f642a851` | privado/distribuicao/humano/T4_1_F07.zip |
| `ae1e5bd3787c44076873e53b3192c26e36f0f48c47b5ca26f53343374463f4f9` | privado/distribuicao/humano/T4_2_F12.zip |
| `4dacca8d5eaa19eee24f22fdb99dafbc52551dd463b820b77185543a81d5405e` | privado/distribuicao/humano/T1_1_F10.zip |
| `d4c00d4575d42ae739e21820d0185082fc33d1294aff55c6b977a7d15f5a5b89` | privado/distribuicao/humano/T1_2_F02.zip |
| `3aaa297d1fdd2767d3bba67229377d3aaabf59bbde6a7d01363106abc1e033c3` | privado/distribuicao/humano/T1_3_F09.zip |
| `093eef3d2e44d06be6123a2f88bff92dd4d8b95aa3aab9dcc89eb5eaaeec5261` | privado/distribuicao/humano/T5_1_F03.zip |
| `5eb21027b3613a66cd7904d3e2f6a645d5d90e66dc6cfa4c9dc4eb530ac22cfb` | privado/distribuicao/humano/T5_2_F04.zip |
| `1a99bb5aed2f24d9ee20b633016a76a5dcd447c4c7df549b24888fd9c910510c` | privado/distribuicao/humano/T3_1_F01.zip |
| `2f6ce3a5e01b3c86356b7abba1d35ec7a81380470e4b647509a4239e7c06c6cc` | privado/distribuicao/humano/T3_2_F11.zip |
| `766c250aec63243334eb8ecf1cdf843c83b197e620bcc55bf43ddd13e81d8044` | privado/distribuicao/humano/T2_1_F05.zip |
| `2df3337b2eb08b80706f63dfee625811018bbb36b2f0a50e99aa38a83153c8ef` | privado/distribuicao/humano/T2_2_F08.zip |
| `434bcd007a2204d070228d77c4608e00cfb46c7a549b9f82b465e4b61f9e592f` | privado/distribuicao/humano/T2_3_F06.zip |
| `434bcd007a2204d070228d77c4608e00cfb46c7a549b9f82b465e4b61f9e592f` | privado/distribuicao/ia/T4_1_F06.zip |
| `3aaa297d1fdd2767d3bba67229377d3aaabf59bbde6a7d01363106abc1e033c3` | privado/distribuicao/ia/T4_2_F09.zip |
| `2df3337b2eb08b80706f63dfee625811018bbb36b2f0a50e99aa38a83153c8ef` | privado/distribuicao/ia/T4_3_F08.zip |
| `1a99bb5aed2f24d9ee20b633016a76a5dcd447c4c7df549b24888fd9c910510c` | privado/distribuicao/ia/T4_4_F01.zip |
| `4dacca8d5eaa19eee24f22fdb99dafbc52551dd463b820b77185543a81d5405e` | privado/distribuicao/ia/T5_1_F10.zip |
| `2f6ce3a5e01b3c86356b7abba1d35ec7a81380470e4b647509a4239e7c06c6cc` | privado/distribuicao/ia/T5_2_F11.zip |
| `d4c00d4575d42ae739e21820d0185082fc33d1294aff55c6b977a7d15f5a5b89` | privado/distribuicao/ia/T5_3_F02.zip |
| `ae1e5bd3787c44076873e53b3192c26e36f0f48c47b5ca26f53343374463f4f9` | privado/distribuicao/ia/T5_4_F12.zip |
| `4901a43e4dcbff7dbfb4c35b799b0e1fc3643fb27dce76e0b1ece947f642a851` | privado/distribuicao/ia/T3_1_F07.zip |
| `5eb21027b3613a66cd7904d3e2f6a645d5d90e66dc6cfa4c9dc4eb530ac22cfb` | privado/distribuicao/ia/T3_2_F04.zip |
| `766c250aec63243334eb8ecf1cdf843c83b197e620bcc55bf43ddd13e81d8044` | privado/distribuicao/ia/T3_3_F05.zip |
| `093eef3d2e44d06be6123a2f88bff92dd4d8b95aa3aab9dcc89eb5eaaeec5261` | privado/distribuicao/ia/T3_4_F03.zip |

## 2026-11-10 — S2: sessão das suítes humanas

- Início: 15:25. Presentes: T1, T2, T4 e T5. **Ausente: T3**; as falhas dessa pessoa (F01, F11) ficam para uma sessão posterior, nas mesmas condições.
- Canal: zips enviados um por vez no privado do WhatsApp, na ordem de `privado/distribuicao/ordem.csv`. Os testadores registram início e fim no cabeçalho de cada arquivo.
- A S3 (IA) só começa depois das 12 suítes humanas congeladas, incluindo as de T3.
- Entregas (congeladas com `scripts/receber.py`; detalhes em `privado/entregas/registro.csv`):
  - 1ª rodada, congelada às 16:03:18 (arquivos baixados do WhatsApp entre 15:56 e 16:02):
    - F03, T5, cabeçalho 15:25–15:55, `5598262c975b5d4f5fa8f7c396bac79677a70642ceaca2a3531a88a8b2ac554c`
    - F05, T2, cabeçalho 15:25–15:55, `692edf71551a23a9846398e6d2b3b79aacca350893a769ce7c384bf1dc7e7dc0`
    - F07, T4, cabeçalho 15:25–15:55, `30e15ae8802d8944844160078123c3dbb5b74466b6dd6d1ed67d075db65f0a55`
    - F10, T1, **cabeçalho sem horário de fim** (`fim: __:__`), arquivo baixado às 16:02. Congelado como recebido, sem alteração. `543bd0d8ad90175c85360bb88c5d3bff983f0127d59f9dba8806c5324603de1d`
    - Esclarecimento sobre o F10 (informado por T1 ao curador durante a sessão): a pessoa parou às **15:55** e esqueceu de preencher o horário de fim no cabeçalho. O envio atrasou até cerca de 16:02 por lentidão do computador usado (4 GB de RAM). O arquivo congelado não foi alterado. Esse horário de fim vem do relato de quem testou, não do cabeçalho.
  - 2ª rodada: início às 16:15, com os mesmos participantes (T1, T2, T4 e T5). Zips: T1_2_F02, T2_2_F08, T4_2_F12, T5_2_F04.
  - 2ª rodada, congelada em 16:47 (arquivos baixados do WhatsApp entre 16:45 e 16:46):
    - F02, T1, cabeçalho 16:15–16:45, `7985f7d5cdf8e3d7ba1482de7a844a44c4ae4655cb311886defdab1fd8e3cb13`
    - F12, T4, cabeçalho 16:15–16:45, `63f1e36fe4c072d9033cd86e25f97ae3f5497df1a57ed27a1953b4ba485f4ff2`
    - F04, T5, cabeçalho 16:15–16:45, `b72e94ba8b21540108980aefd3ed67d66d20ca8599ddaeecec5ac497277173b7`
    - F08, T2: o arquivo recebido (baixado às 16:45) é **idêntico ao esqueleto do kit**, sem testes e sem horários. **Não foi congelado.** O curador pediu o arquivo correto.
    - F08, T2: arquivo correto reenviado (baixado às 16:50:37), cabeçalho 16:15–16:45, 6 métodos `@Test`. Congelado: `0c8ccbe4f626fa03dae69bc726b8b89ba94cb82b3484f9ab65cb5c648cad1a86`. Foi pedido que o arquivo fosse enviado como estava às 16:45, sem continuar escrevendo. Não há como verificar se houve edição entre 16:45 e 16:50; fica registrado como possível desvio (cerca de 5 min).
- Encerramento da sessão de 2026-11-10 às 16:52. **8 de 12** suítes humanas congeladas (F02, F03, F04, F05, F07, F08, F10, F12).
- Pendentes para outra sessão, nas mesmas condições: F09 (T1) e F06 (T2), a 3ª falha de cada pessoa, cujos zips **não foram enviados** hoje; F01 e F11 (T3, ausente).
- O grupo deve ser orientado a não comentar a sessão até o fim do experimento.
- Evidência de presença: relatório de participação do Google Meet da sessão, salvo em `privado/evidencias/2026-11-10_S2_presenca_meet.xlsx` (SHA-256 `b9a475faeb0d5c2b879aac62bb1502629574931864d4e0c9c5fad14f71b3823c`). Entradas entre 14:28 e 14:43 (o horário da S2, 15:25, conta a partir da abertura da sessão), e **todos saíram às 16:51**. Presentes: curador, T1, T2, T4 e T5; T3 não aparece. Todos os testadores permaneceram na chamada durante as duas rodadas, inclusive T2 no reenvio do F08 (16:50). O arquivo contém e-mails dos participantes e não deve ser publicado como está.

## 2026-11-11 — S2 (continuação): suítes humanas restantes

- Presentes: T3, T1 e T2, com o curador. Mesmas condições da sessão de 2026-11-10 (perfil `Experimento`, um zip por vez pelo WhatsApp, 30 min, horários no cabeçalho).
- 1ª rodada: início às 16:00 (pelos cabeçalhos). Arquivos baixados entre 16:30 e 16:32 e congelados às 16:33:
  - F01, T3, cabeçalho 16:00–16:30, `3aaf72075e431698d7a347d71cf74f534e3cfae2840d5a15d1463ca31d1bc864`
  - F09, T1, cabeçalho 16:00–16:30, `1cf730c0b817fdaf1008a2330385e88d34ba9c48101e94e6372d4f7dab01a15a`
  - F06, T2, cabeçalho 16:00–16:30, `ac15df0e728f46671b16c9218bcf9156936ce839efde5aae1b2d244ab21f1955`
- 2ª rodada (só T3): F11, cabeçalho 16:40–17:10, baixado às 17:11 e congelado às 17:11: `65c96024577d15d56f24be7ec38287701d26486663ecae9e0cd799d272f70b57`
- **As 12 suítes humanas estão congeladas.** A S3 (IA) está liberada a partir de agora.
- T3 (informado pelo curador): fez o **aquecimento** com o P01 e a conferência do perfil `Experimento` antes de começar, e **ninguém comentou** nada sobre a sessão de 2026-11-10.
- Evidência de presença: relatório do Google Meet em `privado/evidencias/2026-11-11_S2_presenca_meet.xlsx` (SHA-256 `d99bc1cfaee38b29b62917b152ff29228612d539b5f92cea2948252c3e2460e8`). curador 15:29–17:13; T1 15:30–16:32; T2 15:31–16:31; T3 15:44–17:13. Todos estavam na chamada durante a 1ª rodada (16:00–16:30), e T3 também durante a 2ª (16:40–17:10). T1 e T2 saíram logo depois de entregar a última falha de cada uma das duas pessoas. O arquivo contém e-mails e não deve ser publicado como está.
- **Fim da S2.** Diferença entre as sessões, a citar em Ameaças à Validade: as suítes humanas foram escritas em dois dias (2026-11-10 e 2026-11-11), nas mesmas condições, com supervisão por chamada e orientação para não comentar a sessão.

## 2026-11-11 — Preparação da S3: piloto do prompt

- Decisão do curador: **o prompt é escrito pelos operadores de IA** (T4, T5 e T3), não pelo curador, que conhece os defeitos (artigo 3.8). O mesmo prompt vale para todas as falhas, e só variam `{CODIGO}` e `{CLASSE}`.
- Requisitos e procedimento em `docs/protocolo_ia.md`: o prompt espelha o `INSTRUCOES.md` dos testadores, busca e execução de código desligadas, ajustes no piloto só de formato e piloto com o P02 (fora da amostra).
- Enviado aos operadores: `privado/distribuicao/piloto_ia.zip` (PROTOCOLO_IA.md, INSTRUCOES.md e a pasta P02).

| SHA-256 | Arquivo |
|---|---|
| `6f8616cf48dce49ad4bb157b77fe935000ed7df3fa6f6220160c83b5e88d24e7` | docs/protocolo_ia.md |
| `05b03ad0687ff58e2eaf38c26dd816568210c4e90b36d417851d6aa7e05461a4` | privado/distribuicao/piloto_ia.zip |

## 2026-11-13 — Piloto do prompt (P02)

- Prompt escrito por T4 e aprovado pelos demais operadores. O curador apontou só problemas de formato e de equivalência com as instruções humanas (formato de chat em vez de agente, sem Hamcrest, sem "maximizar cobertura", métodos públicos, nome da classe e pacote). Nenhuma sugestão de conteúdo sobre o que testar.
- Testes de formato feitos pelo curador no AI Studio (respostas em `privado/piloto_ia/P02/`):
  1. Resposta em JSON: saída estruturada ligada. Corrigido.
  2. Copiado da tela: o texto veio quebrado. Passou a ser copiado pelo botão da mensagem.
  3. Busca do Google estava **ligada** no print de configuração. Desligada.
  4. Classe colada no texto: o AI Studio **bloqueou o envio** ("Enable URL Context") por causa do link da licença Apache, presente nas 14 classes. Anexo `.java` deu erro "Inline data requires a mime_type". **Solução: anexar a classe como `.txt`**, com o conteúdo idêntico ao dos testadores.
  5. Nome da classe errado enquanto o prompt tinha marcadores não substituídos (`{CODIGO}`, `{PO2}`). Com `P02` escrito, veio `public class P02Test`.
- Resposta final do piloto (`teste5_final/P02Test.java`) medida com `scripts/medir.sh`: compila, 5 testes, 0 detecções, 0 falsos alarmes, 0 instáveis, linhas 61/96 e branches 35/58. O fluxo da IA até a medição funciona.
- `docs/protocolo_ia.md` atualizado com o procedimento (anexo `.txt`, substituição do `{CODIGO}` sem chaves, não ativar URL Context, copiar pelo botão). SHA-256: `2e557bd721332fdf714d3388ec8dafee691d8729c5777b5fcc832b946f7f47cc`
- Configuração vista no piloto: Gemini 3.1 Flash Lite (`gemini-3.1-flash-lite`), thinking High, busca, mapas, URL Context, execução de código, function calling e saída estruturada desligados, output length 65532, sem instruções de sistema. A temperatura não aparece no painel para esse modelo. **Ainda não congelado**: falta a confirmação dos operadores e o texto final do prompt.

## 2026-11-13 — Prompt da IA e zips da S3 com prompt preenchido

- Prompt dos operadores salvo como modelo em `docs/prompt_ia.txt`. O texto recebido tinha dois marcadores diferentes para a mesma coisa (`{SUBSTITUIR}` na primeira frase e `{CODIGO}` na regra 1). Foram unificados como `{CODIGO}`, sem nenhuma outra alteração de texto. **Pendente: confirmação dos operadores e escolha do modelo.** O prompt só é congelado junto com o modelo e a configuração.
- Sugestão do curador, para evitar erro manual: os zips da S3 passaram a trazer `PROMPT.txt` já preenchido com o código da falha e a classe sob teste em `.txt`, idêntica byte a byte à do kit (conferido nos 12 zips). `scripts/distribuir.py` interrompe se sobrar algum `{` no prompt.
- Incidente corrigido: `kit/P01/test/.../P01Test.java` tinha sido editado no dia do ensaio (abertura do kit no VS Code). A edição foi guardada em `privado/ensaio/P01Test_editado_no_kit.java` e o esqueleto original foi restaurado. Ele confere com o `kit/SHA256SUMS`, e o `aquecimento.zip` voltou ao hash original (`4980fb18…`).
- Conferido: `distribuicao.csv`, `ordem.csv`, `aquecimento.zip` e os 12 zips humanos **inalterados**. Só os zips de IA foram regerados. **Os zips de IA precisam ser regerados de novo quando o prompt for congelado**, se o texto mudar.

| SHA-256 | Arquivo |
|---|---|
| `40b3e70e7bfa442cc73db8f6185ca382578944b2353ca051ace64f6477954664` | docs/prompt_ia.txt |
| `97ede1d2e79dd5bffcb8fa6c0e0ed68fcd503d3a69be99967857930494757693` | docs/protocolo_ia.md |
| `8b38d6f7c2184f244ce9f6ade01212984ed92bb34b543e4ffad20f5c637567bc` | scripts/distribuir.py |
| `32423d5f1af773557eed4982723816e800376d8bd47e880b3c0e908116df4e79` | privado/distribuicao/ia/T4_1_F06.zip |
| `4d7ef480eae7db0aa322979e4b93596d72de4bf5eb382bff9813d78fb3595416` | privado/distribuicao/ia/T4_2_F09.zip |
| `ab60491c0321821e6f52e02cae0af28d9296b853f7729e0d6663d146bfd2de83` | privado/distribuicao/ia/T4_3_F08.zip |
| `fd49853707da5d39d0d11db066ced996fb7512d30b0a0d17ee3a0fda3ca5e556` | privado/distribuicao/ia/T4_4_F01.zip |
| `6d07ab6eea3974fe072ab2d9b5b25fbf97a136ba20790ce5f89bd9bc7401dd07` | privado/distribuicao/ia/T5_1_F10.zip |
| `7797edff8a7046592a86171ff77027661bf6b89f41f2e90a3ec4d056a70ac8a7` | privado/distribuicao/ia/T5_2_F11.zip |
| `ef370695003ce576f1a7d134a6417f10d09adba4807b837ec8de6674712cd84c` | privado/distribuicao/ia/T5_3_F02.zip |
| `78e509cd08a8963aa713b3f9f12176f5516c0cac6a03b19ccb12fead9dc36743` | privado/distribuicao/ia/T5_4_F12.zip |
| `81b16088bf8c3cd5671f239d7adff3e1fe722a228942ef86ae0fc04aa72901ca` | privado/distribuicao/ia/T3_1_F07.zip |
| `c8a852dd98798212a98d825efb302c7f8dc39c9e0412ec1a001259aa14103030` | privado/distribuicao/ia/T3_2_F04.zip |
| `0bbd4012335a1fbb475d31acca69d35ef4cf9de43fc699ea3f9f19fdedb4d02c` | privado/distribuicao/ia/T3_3_F05.zip |
| `057f4979bba039fdb67f016faa83f50f4c0c9850ea390b2139a3a7c557b89a50` | privado/distribuicao/ia/T3_4_F03.zip |

## 2026-11-13 — Piloto com Gemini 3.8 Flash (T3)

- Modelos considerados: Gemini 3.1 Flash-Lite (piloto inicial), Gemini 4 Argon (não é família Flash e provavelmente é pago) e **Gemini 3.8 Flash** (`gemini-3.8-flash`: família Flash, versão estável, com nível gratuito no AI Studio, segundo fontes secundárias). O artigo (3.10) fala em "Flash family in a preview version"; se o 3.8 Flash for escolhido, o texto precisa dizer versão estável.
- O operador T3 rodou o prompt de `docs/prompt_ia.txt` no P02 com o Gemini 3.8 Flash. Resposta salva em `privado/piloto_ia/P02/T3_3.8flash/P02Test.java`.
- Medição: compila, `public class P02Test` no pacote correto, só JUnit 4. 30 testes, 0 detecções, 2 falsos alarmes (valores esperados do exemplo do Javadoc estimados pela IA), 0 instáveis. Cobertura na versão corrigida: linhas 87/96 e branches 48/58.
- O formato funciona com o 3.8 Flash. **Pendente para congelar:** decisão final do grupo sobre o modelo, a confirmação de que os três operadores têm o modelo no AI Studio e um print da configuração usada.

## 2026-11-13 — Congelamento da condição de IA (antes da S3)

- Decisão do grupo: **Gemini 3.8 Flash** (`gemini-3.8-flash`), thinking **High**, todas as ferramentas desligadas (structured outputs, code execution, function calling, Google Search, Google Maps, URL context), sem system instructions, output length 65536. A temperatura não aparece no painel e fica o padrão do modelo. Detalhes em `docs/configuracao_ia.md`.
- Print da configuração (enviado pelo curador, mesmo painel usado no piloto de T3): `privado/evidencias/2026-11-13_configuracao_ai_studio_gemini-3.8-flash.png`.
- Prompt congelado: `docs/prompt_ia.txt`, sem alterações desde a entrada anterior. Os zips de IA foram regerados e saíram idênticos; os zips humanos, a distribuição e o aquecimento continuam inalterados.
- Para o artigo (3.10): trocar "Flash family in a preview version" por "Gemini 3.8 Flash (versão estável)".
- Antes da primeira execução, cada operador confirma que o painel dele está igual e manda um print.

| SHA-256 | Arquivo |
|---|---|
| `bdf866221ac813084fa2a0cd882885dbe7493f0a6c3d26efba681652123617ef` | docs/configuracao_ia.md |
| `40b3e70e7bfa442cc73db8f6185ca382578944b2353ca051ace64f6477954664` | docs/prompt_ia.txt |
| `97ede1d2e79dd5bffcb8fa6c0e0ed68fcd503d3a69be99967857930494757693` | docs/protocolo_ia.md |
| `5b0703039b46550e8f13efee43f315bbc9397ac29fc7030a5517a1e54e78efa1` | privado/evidencias/2026-11-13_configuracao_ai_studio_gemini-3.8-flash.png |
| `92986b72551ab863a50538da85eccf68285d42dc0d53f98bcfa037e0df7d7328` | privado/distribuicao/ia/T4_1_F06.zip |
| `653db6cdeeab3c7b6135c556366307cdce31471a4c0088f80150d33e28964510` | privado/distribuicao/ia/T4_2_F09.zip |
| `0f56dcfe00456187ebd4222ddb22336074386b04c3e532ca8748852af1ea4beb` | privado/distribuicao/ia/T4_3_F08.zip |
| `6dbd09631bb61bb15c653b7cfc580dfc3e00be44269b4e442a0cb0c6c3bdddcd` | privado/distribuicao/ia/T4_4_F01.zip |
| `16efbc4f6238b54257fd1eb36584cc19f09db6df3d9da179fd9a8cf7c0986e62` | privado/distribuicao/ia/T5_1_F10.zip |
| `14af61bc7f6141ddb0091f5e52ec77da6221f12e1c3b150c5488c49de7890c03` | privado/distribuicao/ia/T5_2_F11.zip |
| `b6850bbd7a75136d037df2c2251d65e1ea3712496c7d86e9c473638424c448d4` | privado/distribuicao/ia/T5_3_F02.zip |
| `d3e7bae98b67dbe5a717bd66a6cf26e37552294503fd72bda0c6a302b4ca298b` | privado/distribuicao/ia/T5_4_F12.zip |
| `f61b0ce659b418db1807bff24fb0973cf5687a39dfa3e90fcb9084dec706efb9` | privado/distribuicao/ia/T3_1_F07.zip |
| `fcab92af4c4be4dde38b5dde7ead9f76b1ad52ed084584049fb7dc18d49c2878` | privado/distribuicao/ia/T3_2_F04.zip |
| `1d9ef1957b34719c5ed774219c7b88bc4f1a9f6a8c44a28b00604580aadc07f7` | privado/distribuicao/ia/T3_3_F05.zip |
| `f83331e009bbef5394aef4d6290f1df45481409ded502a4303d401b0bb74d5a7` | privado/distribuicao/ia/T3_4_F03.zip |
- Correção da linha acima: os zips de IA têm **conteúdo idêntico** ao da geração anterior (mesmo prompt e mesma classe), mas **bytes diferentes**, porque o `PROMPT.txt` é gravado dentro do zip com a data e hora da geração. Os hashes válidos dos zips de IA são os da tabela acima (2026-11-13, congelamento), e são esses os arquivos a enviar.
- Evidência da reunião do piloto com o Gemini 3.8 Flash: relatório do Google Meet em `privado/evidencias/2026-11-13_piloto_ia_presenca_meet.xlsx` (SHA-256 `210cc9e15bbc12adf8d47596d42144820ab207a9e42b69275ac57ce462651efe`). curador 15:19–15:33 e T3 15:20–15:33. Reunião curta para T3, que tinha acesso ao modelo, rodar o piloto no P02 acompanhado pelo curador. O arquivo contém e-mails e não deve ser publicado como está.

## 2026-11-14 — S3: sessão de IA

- Execução 1 recebida pelo WhatsApp (arquivos baixados entre 14:42 e 14:44) e copiada para `privado/entregas/ia/<código>/execucao1/` às 14:45:41, somente leitura:
  - F06, T4, execucao1, 17 `@Test`, `c3ca73cde0baa810e28da02a69dd4a5bfaf6313d36f163b070c9c8d4afd17809`
  - F07, T3, execucao1, 129 `@Test`, `3af937bedab47ffb77b156513b9afd1fe171f8e5c2eb61f1063846f492d28c14`
  - F10, T5, execucao1, 44 `@Test`, `22d3305e52a6b5a79585c311b104f882047500f8b656b417f1a4d2729fe17585`
- Incidente: o `privado/entregas/registro.csv` estava aberto e bloqueado (provavelmente no Excel). O `receber.py` copiou e congelou os três arquivos (cópias idênticas às recebidas, conferido com `cmp`), mas **não conseguiu gravar as linhas no registro**. As linhas serão acrescentadas assim que o arquivo for liberado, com o horário real da cópia (14:45:41).
- **Desvio de formato:** os operadores enviaram `.java` só com o conteúdo do bloco de código, e não a resposta inteira em `.md` copiada pelo botão da mensagem, como pede o `PROTOCOLO_IA.md`. Os arquivos foram congelados como recebidos. Como o prompt exige "apenas um único bloco de código, sem explicações", o bloco tende a ser a resposta inteira, mas isso não pode ser verificado só pelo arquivo.
- **Decisão do curador (emenda ao procedimento da S3):** as respostas da IA passam a ser entregues como `.java`, com o conteúdo do bloco de código copiado pelo botão do bloco, sem edição. Se a resposta tiver texto fora do bloco, entrega-se a mensagem inteira em `.md`. O operador confirma ao enviar que a resposta tinha só o bloco. A execução 1 de F06, F07 e F10 já veio nesse formato. Nomes passam a incluir a execução (`F06_exec2.java`), para evitar sobrescrita em `privado/recebidos/ia/`. `docs/protocolo_ia.md` atualizado (SHA-256 `b90c0063bd1d4e8154170fa50557c610962c8999195e98d7f1f0852973bb9143`).
- `registro.csv` liberado: as três linhas da execução 1 de F06, F07 e F10 foram acrescentadas com o horário real da cópia (2026-11-14T14:45:41).
- Mais três entregas (baixadas entre 14:47 e 14:48), congeladas às 14:49:42:
  - F04, T3, execucao1, 18 `@Test`, `7db07db08924b1ba3edcfdc35fcc3fffe151734b0e4c9d058456e5101eaa20d3`
  - F09, T4, execucao1, 31 `@Test`, `fbcd9f8b58f01ce23bc138504a86a1d63f34efe918f335e1e68970433eb7859a`
  - F11, T5, execucao1, 21 `@Test`, `df79ddf32682f010dc5fa24cc29b127dc28ce1c5f567df1b01f2b043a5a3f9a6`
- **Ordem das execuções:** cada operador passou para a 2ª falha depois de uma única execução da 1ª. O protocolo previa as 3 execuções de cada falha em sequência (artigo 3.7: mesmo operador e janela curta). Registrado como desvio de ordem; ver a decisão a seguir.
- **Citação de fonte no F04 (T3), mesmo com a busca desligada:** o AI Studio mostrou na resposta uma fonte citada, `https://alvinalexander.com/java/jwarehouse/commons-math3-3.6.1/src/test/java/org/apache/commons/math3/optimization/univariate/BrentOptimizerTest.java.shtml`, uma cópia pública da classe de teste **original do projeto** para a classe do F04. O arquivo congelado (só o bloco de código) não contém a URL. Interpretação: com busca, URL context e grounding desligados, a citação vem do mecanismo de **recitação** do Gemini, que aponta a fonte quando o texto gerado é muito parecido com algo do treinamento. É um sinal concreto de **contaminação** (memorização do benchmark), ameaça já prevista no artigo (3.10). A execução foi mantida, sem descarte, e deve ser analisada à parte. Pedido ao curador: print da resposta com a citação e confirmação de que a busca estava desligada.
- **Decisão do curador (emenda ao método, 2026-11-14, durante a S3):** a condição de IA passa a ter **uma execução por falha** (12 suítes), em vez de três (36). Tomada **antes de qualquer suíte de IA ser medida**: nenhum resultado de detecção ou cobertura da IA era conhecido. Com isso, as 6 execuções já congeladas (F04, F06, F07, F09, F10, F11, todas `execucao1`) são as suítes definitivas dessas falhas, e o desvio de ordem registrado acima deixa de existir. Faltam 6: F08 e F01 (T4), F02 e F12 (T5), F05 e F03 (T3).
- Consequências, a refletir no artigo: seção 3.7 (Runs and Stability) reescrita ou removida; seção 3.4 (a FDR da IA deixa de ser a média de três execuções); seções 3.6 e 3.8 (três execuções pelo mesmo operador); seção 3.10 (sai a assimetria "1 suíte humana × 3 de IA", e entra como ameaça o não-determinismo do modelo, com uma única amostra por falha).
- `docs/protocolo_ia.md` (uma execução, arquivo `<código>Test.java`) e `docs/protocolo_medicao.md` (emenda) atualizados.

| SHA-256 | Arquivo |
|---|---|
| `82f1c8a5bbc0d61b877932f56a6357519a0a06a68c2a5a2024829e9bdd0fee78` | docs/protocolo_ia.md |
| `e4ec30f542a53ff1b2b393d306c23681ff4a71f72bbb4893be46962d6e8e50d3` | docs/protocolo_medicao.md |
- Correção dos dois hashes acima: a edição gravou os arquivos com quebras de linha do Windows (CRLF). Eles foram convertidos de volta para LF, que é o que o Git guarda (`.gitattributes`), sem mudar o conteúdo. Hashes válidos: `docs/protocolo_ia.md` `26bb7ba52594c1a569db5782ae220940c5cd059bef52458ff7c4eed20d0ebb05` e `docs/protocolo_medicao.md` `a4783c2cf90d590dee0310ba5219dc14708cd6016b4c92f784eb8a4638fdd41e`.
- Mais três entregas (baixadas entre 14:53:09 e 14:53:24), congeladas às 14:54:
  - F08, T4, 39 `@Test`, `6631feb8490de495cb351b45c1a769afcef4c88d339341e1cb2b314bb7585693`. **A classe gerada se chama `F09Test`**, embora o pacote (`org.apache.commons.lang.text`) e o conteúdo testem a classe do F08 (`ExtendedMessageFormat`). Hipótese: o operador colou o `PROMPT.txt` da falha anterior (F09) com o anexo correto do F08. Os prompts das duas falhas são idênticos, exceto pelo nome da classe. Congelado como recebido, sem nova geração. Na medição, o arquivo será copiado com o nome `F09Test.java` exigido pelo Java, sem alterar o conteúdo. Pendente: confirmação de T4.
  - F02, T5, 80 `@Test`, `7fb048404565dae09e672e119af0489b431ad967e6c5e77c29b1fd8ab3c1fdd7`
  - F05, T3, 47 `@Test`, `e126d99bc09660be64b09707585a604b4c54f0aa30feffc606abe6a43c24d372`
- Últimas três entregas (baixadas entre 14:56:42 e 15:03:24), congeladas às 15:03:
  - F03, T3, 33 `@Test`, `58f02490fb81e4916ed3c25bf6c4992d3ca1d021cc9f871f63bf2c2908049600`
  - F12, T5, 70 `@Test`, `9aaa07283d47cdc882c259bc26013786badc216b30996101665056ecd5a0863d`
  - F01, T4, 21 `@Test`, `8eced9812303daa86b4cdb93155af968fe3d8250d89ffda02ae4adfb3f13604a`
- **Fim da S3: as 12 suítes de IA estão congeladas**, uma por falha.
- **F08, confirmado por T4:** o operador colou o `PROMPT.txt` do F09 com o anexo correto do F08. A suíte vale como a execução do F08. Os prompts diferem só no nome da classe de teste, e a medição usa o arquivo com o nome `F09Test.java`.
- Evidência de presença: relatório do Google Meet em `privado/evidencias/2026-11-14_S3_presenca_meet.xlsx` (SHA-256 `375caa4d6d4ad6afb8803f6084643a878d140b1e4dcf353906c8e27849496515`). curador 14:27–15:06; T5 14:28–15:06; T4 14:28–15:06; T3 14:32–14:57. Todos estavam na chamada durante as próprias execuções (a última de T3, F03, foi baixada às 14:56:42; a última de T4, F01, às 15:03:24). O arquivo contém e-mails e não deve ser publicado como está.
- Backup da pasta `privado/` feito pelo curador depois da S3.
- Pendente: print da resposta do F04 com a citação de fonte (T3).

## 2026-11-14 — Medição, rodada 1 (antes da revisão mecânica)

- `scripts/preparar_medicao.py` montou as 24 entradas em `privado/medicao/entrada/`, com o conteúdo idêntico às suítes congeladas (conferido por hash no `manifesto.csv`), só renomeando o arquivo para o nome da classe pública (o F08 de IA vira `F09Test.java`).
- `scripts/medir.sh` nas 24 suítes, 3 repetições em cada versão. Resultado bruto em `privado/medicao/resultados_rodada1_bruta.csv` (SHA-256 `dd8b9455d04bcf623fc09a87602995eb4ad839875dedf6b47a4c742fb2635ab1`).
- **Não compilam: 9 suítes**, sendo 8 humanas (F01, F02, F03, F04, F05, F09, F10, F12) e 1 de IA (F04). As 15 que compilam não têm testes instáveis.
- Taxa de compilação bruta (para o artigo): humanas 4/12; IA 11/12.
- Lista para a revisão mecânica, com revisores pela roda: `privado/medicao/revisao_pendente.md`.
- Os resultados de detecção e cobertura desta rodada **não são finais**: as suítes que não compilam serão medidas de novo depois da revisão.
- **Decisão do curador (emenda aos papéis):** a revisão mecânica das 9 suítes que não compilam será feita **pelo próprio curador (curador)**, e não pelos testadores em rodízio. O curador não é autor de nenhuma suíte, então a regra "revisor diferente do autor" é mantida. Risco registrado: o curador conhece os defeitos. Mitigação: só as correções da **lista fechada** (import, cast, assinatura visível, `throws`, pontuação). Tudo o que estiver fora dela é resolvido **comentando o método inteiro**, sem corrigir o teste. Cada alteração é registrada com `scripts/revisar.py` (hash do original e do revisado), e o diff de cada suíte fica disponível para auditoria.

## 2026-11-14 — Revisão mecânica e medição final

- Revisão mecânica das 9 suítes que não compilavam, feita pelo **Claude a pedido do curador** (revisor registrado como "Claude"), só com a lista fechada: 4 imports (F02, F03, F04 humanas; no F02 o import era `import.org.junit.Bafore`), 1 cast (F12 humano), 1 `throws` (F03 humano) e 9 métodos comentados por erros fora da lista (F01 humano ×2, F02, F03, F05, F09, F10 humanos; F04 IA). Nenhum valor, entrada, assert ou lógica alterado. As 9 compilam nas duas versões (verificado só com `javac`, sem executar os testes). Registro detalhado em `privado/revisado/revisao.csv`; versões revisadas congeladas em `privado/revisado/`.
- Rodada 2: as 9 revisadas medidas com `scripts/medir.sh` (`privado/medicao/resultados_rodada2_revisadas.csv`, SHA-256 `b232ce58b97f45cc44ac227342f0fc87cd2c0edb8aac9ac925ba8a30f97c4c81`).
- Consolidação com `scripts/consolidar.py` (rodada 1 para as 15 que já compilavam e rodada 2 para as revisadas). Correção documentada: o F01 humano ficou sem métodos de teste ativos, e o JUnit gera um `initializationError` que o `medir.sh` contava como falso alarme. Ele é contado como 0 testes e 0 falsos alarmes.
- **Resultados finais** (`privado/medicao/resultados_finais.csv`, SHA-256 `a490b2f5e19fb58269ca297181106f8f7c8f87d2d61f2d4d7c622244f3e3b413`), cobertura na versão corrigida:
  - Humanas: FDR 1/12 (8,3%, só o F11); LCov média 25,9% (mediana 15,7%); BCov média 19,4% (mediana 8,8%); 48 testes; 8 falsos alarmes; 2 suítes com 0 testes (F01 e F04).
  - IA (Gemini 3.8 Flash, 1 execução): FDR 0/12 (0%); LCov média 94,7% (mediana 98,2%); BCov média 91,5% (mediana 93,9%); 549 testes; 3 falsos alarmes.
  - Nenhum teste instável em nenhuma suíte.
- Texto da emenda sobre a revisão mecânica em `docs/protocolo_medicao.md` ajustado para refletir exatamente o que foi feito: revisão conduzida pelo curador, com as correções aplicadas pelo Claude a pedido dele. SHA-256: `94a9795c400480b6922b589b68a151ff0a86410dd8c02ceebcc4073a058f53d6`
- Correção da contagem da revisão mecânica (entrada acima e mensagens ao grupo): foram **5 correções da lista fechada** (3 imports: F02, F03 e F04 humanas; 1 cast: F12 humano; 1 `throws`: F03 humano) e **8 métodos comentados** (F01 humano ×2, F02, F03, F05, F09 e F10 humanos, e F04 IA), e não "4 imports" e "9 métodos". Conferido em `privado/revisado/revisao.csv`.

## 2026-11-21 — Anonimização e publicação do pacote de replicação

- **Motivo:** o repositório público continha nomes dos participantes ligados às suítes e aos registros de cada um. Pela regra da disciplina, dados identificáveis de participantes não vão para o repositório. O curador tornou o repositório privado assim que isso foi notado.
- **Anonimização** (feita pelo Claude a pedido do curador, a partir de uma cópia do repositório; detalhes em `ANONYMIZATION.md`):
  - Testadores viraram os códigos T1 a T5, por sorteio não reproduzível que não coincide com a ordem alfabética nem com a ordem de autoria do artigo. Os operadores de IA mantêm o mesmo código de testador, e o `scripts/distribuir.py` continua reproduzindo `distribuicao.csv` e `ordem.csv` idênticos (conferido).
  - O curador aparece como "curador".
  - Nomes trocados no conteúdo dos arquivos e nos nomes de pastas e arquivos. Relatórios de presença reduzidos a código e horários. Marcas de gênero ligadas aos códigos neutralizadas neste log.
  - A tabela de equivalência entre pessoas e códigos fica com o curador, fora do repositório (o `.gitignore` bloqueia `*EQUIVALENCIA*`).
  - Única mudança em uma suíte: o F08 humano (T2) usava o primeiro nome de quem escreveu como dado de teste em duas strings; o nome virou `T2`, na entrada e no valor esperado. Os hashes registrados acima e no `manifesto.csv` se referem ao arquivo original. SHA-256 do arquivo anonimizado: `bbedc01164a00bbaa7e7956cb1e47efede148421267c6b8ccb494f26a8a65c53`. Essa suíte precisa ser medida de novo para confirmar que a cobertura não mudou.
  - `scripts/consolidar.py` reproduz `privado/medicao/resultados_finais.csv` idêntico a partir dos dados anonimizados.
  - Nos 12 zips de IA e no `piloto_ia.zip`, a cópia do `PROTOCOLO_IA.md` citava os nomes dos três operadores; os nomes viraram os códigos. Nomes e datas das entradas e o resto do conteúdo não mudaram, mas os bytes dos zips diferem dos enviados durante o experimento.
- **Histórico descartado:** o repositório novo começa de um único commit já anonimizado. O antigo, com o histórico que contém nomes, será apagado no GitHub e recriado com o mesmo nome. Os membros devem apagar os clones antigos.
- **Pacote de replicação adicionado:** `README.md`, `REPLICATION.md` (passo a passo em três níveis: recalcular as tabelas, medir de novo as 24 suítes, refazer o experimento), `ANONYMIZATION.md`, licenças (`LICENSE`, MIT, para o código; `LICENSE-DATA.md`, CC BY 4.0, para dados e documentos), `NOTICE` (Apache 2.0 das classes do Commons no kit, JUnit e Hamcrest) e `CITATION.cff`, em inglês. Os artefatos do experimento continuam em português, sem tradução, para não mudar os arquivos cujos hashes foram registrados.
- **`scripts/tabelas.py`:** recalcula os números das Tabelas 3 e 4 do artigo a partir de `resultados_finais.csv`, `resultados_rodada1_bruta.csv` e `revisao.csv`. Conferido: todos os valores batem com o artigo.

## 2026-11-21 — Pendências fechadas e diagnóstico complementar

- **Evidência do F04:** o print da resposta com a citação de fonte foi pedido, e T3 informou que não tem. A evidência fica sendo a URL registrada na entrada de 2026-11-14 e o relato do operador. Pendência encerrada sem print.
- **Execuções de IA:** confirmada como definitiva a emenda de 2026-11-14. Não serão rodadas as 24 gerações restantes (2 por falha); a condição de IA fica com uma execução por falha, declarada como desvio no artigo.
- **F08 humano anonimizado medido de novo** (`scripts/medir.sh`, 3 repetições, saída temporária fora de `privado/`): resultado idêntico ao publicado. Foram 6 testes, todos PASSA nas duas versões, com 13/167 linhas e 1/99 branches na versão corrigida. A troca do nome pelo código não mudou nada.
- **Diagnóstico complementar das 24 suítes** (feito pelo Claude a pedido do curador), com `scripts/diagnosticar.sh`, `scripts/Diagnostico.java` e `scripts/resumo_diagnostico.py`. Ele não altera nenhum resultado da medição: só lê as suítes congeladas e os `.exec` já gravados. Saída em `privado/medicao/diagnostico/`; o resumo e a classificação estão em `RESUMO.md`.
  - Execução das linhas alteradas pelo patch: a IA executou todas em 8 falhas, parte em 3 e nenhuma em 1, sem detectar nenhuma. Os humanos executaram todas em 2 (F06, F11), parte em 1 e nenhuma em 9; o F11 foi detectado. `cobertura_patch.csv`: SHA-256 `999f57ce4b344abb910c733cad12619817263d79886374a15f7c4ce386ebb3a7`.
  - Mensagens de falha dos 11 falsos alarmes: `falhas.csv`, SHA-256 `a50cbd65f59a5cc88ce1bac23e5927b51f0cc5ce3963c9212092c2c4e0fdb3b2`. Dois testes de IA (F01, F12) passam na versão com bug e falham na corrigida, ou seja, tomam o comportamento com bug como esperado (oráculo invertido). Pelo critério do protocolo, não contam como detecção, e a FDR da IA continua 0/12.
  - A classificação das causas na tabela do `RESUMO.md` é interpretação do Claude, a partir da mensagem e do código de cada teste. Deve ser conferida pelo grupo antes de entrar no artigo.

## 2026-11-21 — Depósito no Zenodo

- Repositório ativado pelo curador na integração GitHub do Zenodo. A primeira release `v1.0` falhou no Zenodo ("Citation metadata load failed": o `CITATION.cff` listava duas licenças). Foi adicionado um `.zenodo.json` com uma única licença (CC BY 4.0, para os dados; o código continua MIT, conforme o README), e a release `v1.0` foi apagada e criada de novo no commit `77b2c66`.
- Publicado no Zenodo: **DOI 10.5281/zenodo.24423864** (versão v1.0). O DOI foi adicionado ao README e ao `CITATION.cff` depois do depósito, então a cópia arquivada não o contém.
- **Testes gatilho do Defects4J** extraídos com `scripts/gatilhos.sh` para as 12 falhas, em `privado/medicao/diagnostico/gatilhos/<código>.md` (nome e código-fonte de cada teste gatilho). São a referência da tarefa T4 do Andamento 5, a classificação das não detecções da IA em B ou C, que será feita por pessoas. O gatilho do F12 confirma o oráculo invertido da suíte de IA: o teste original espera `ClassCastException` para valor não comparável, e a suíte da IA espera a `IllegalArgumentException` da versão com bug.

## 2026-11-23 — Classificação das não detecções da IA (T4)

- As 12 suítes de IA, nenhuma das quais detectou a falha, foram classificadas por dois membros do grupo, separadamente e sem IA, comparando cada suíte (`privado/medicao/entrada/<falha>/ia/`) com o teste gatilho do Defects4J (`privado/medicao/diagnostico/gatilhos/`). Categorias: A, não executou nenhuma linha do patch; B, executou as linhas do patch sem a entrada que dispara a falha; C, usou entrada equivalente à do teste gatilho, mas a asserção aceitou o comportamento errado.
- Concordância: 7 de 12 (F01, F02, F03, F06, F08, F09, F12); kappa de Cohen 0,31. As 5 divergências (F04, F05, F07, F10, F11) foram resolvidas pelo curador, com a regra "algum teste da IA usa entrada equivalente à do gatilho? sim, C; não, B": F04 C, F05 B, F07 C, F10 B, F11 B.
- Resultado final: A 1 (F08); B 6 (F02, F05, F06, F09, F10, F11); C 5 (F01, F03, F04, F07, F12). Registro em `privado/medicao/diagnostico/classificacao_t4.csv` (SHA-256 `1001ea1f49391af67dc9469b50de518993704367d3abf46c1bab8ec778b33dad`), com as duas classificações e o desempate.

## 2026-11-28 — Release v1.1

- Feedback do Andamento 5: a versão v1.0 no Zenodo (21/11) não contém os testes gatilho (`privado/medicao/diagnostico/gatilhos/`), a classificação da Tabela 5 (`classificacao_t4.csv`) nem a nota sobre o Javadoc do F12, que o artigo cita. Criada a release `v1.1` para arquivá-los.
- O README e o `CITATION.cff` passam a apontar para o DOI conceito do Zenodo (`10.5281/zenodo.24423863`), que resolve sempre para a versão mais recente. O artigo cita o DOI da versão v1.1.
- O README marca a Tabela 5 e o diagnóstico como análise exploratória, definida depois dos resultados (21 e 23/11).

## 2026-11-28 — Checklist de LLM, roteiro da Tabela 5 e release v1.2

- A professora enviou o checklist de relato de estudo com LLM da disciplina (reduzido de Baltes et al., 2026). Aplicado item por item em `docs/llm_reporting_checklist.md`, com onde cada item está no artigo ou por que não se aplica.
- O checklist exige publicar os instrumentos da validação humana e declarar a concordância com limiar. Publicado o roteiro da classificação da Tabela 5 em `docs/roteiro_classificacao_t4.md`, reproduzindo as definições e o procedimento dados aos classificadores em 21/11 e a regra de desempate de 23/11.
- Concordância recalculada com o alfa de Krippendorff (nominal), a medida dos limiares do checklist: alfa = 0,30 (kappa de Cohen 0,31), abaixo de 0,667. **Decisão do curador:** a divisão entre B e C passa a ser apresentada no artigo só como descritiva, sem sustentar conclusão; a divisão entre A e "B ou C" vem do diagnóstico automático e se mantém (concordância de 12 em 12).
- Criada a release `v1.2` com esses dois documentos.
