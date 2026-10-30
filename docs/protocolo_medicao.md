# Protocolo de medição

> **Status: CONGELADO em 2026-10-30**, antes da S3. O SHA-256 deste arquivo está no log de curadoria.
> Responsável: curador (Iuri).

## Ferramentas

- Imagem Docker `vev-d4j` ([env/Dockerfile](../env/Dockerfile)): OpenJDK 11, Defects4J v3.0.1, JUnit 4.12, JaCoCo 0.8.12.
- [scripts/medir.sh](../scripts/medir.sh) roda uma suíte contra uma falha, e [scripts/Executor.java](../scripts/Executor.java) executa cada teste isoladamente.

## Procedimento por suíte

1. A suíte é compilada contra a versão com bug (b) e contra a corrigida (f), só com as classes e bibliotecas do projeto, sem os testes originais dele.
2. Se não compilar, a suíte vai para o revisor mecânico, que aplica só as correções da lista fechada, e depois volta para a medição.
3. Cada teste roda isoladamente, com limite de 30 segundos, **3 vezes em cada versão**.
4. O JaCoCo mede a cobertura só da **classe sob teste**, incluindo as classes internas dela.

## Classificação de cada teste

| Classe | Regra |
|---|---|
| DETECTA | Falha (ou estoura o tempo) na versão com bug e passa na corrigida, nas 3 repetições |
| FALSO_ALARME | Falha na versão corrigida nas 3 repetições |
| PASSA | Passa nas duas versões, nas 3 repetições |
| INSTAVEL | O resultado muda entre repetições |
| NAO_EXECUTADO | Ignorado (`@Ignore`) ou a classe de teste não carregou |

## Métricas por suíte

- **Detecção da falha (sim/não):** a suíte detecta se tem **pelo menos 1 teste DETECTA**. Falsos alarmes e testes instáveis não anulam a detecção, mas são contados e relatados à parte.
- **Cobertura de linha e de branch:** medida na **versão com bug**, que é a versão entregue aos testadores e à IA. A cobertura na versão corrigida também é registrada.
- **Contagens auxiliares:** número de testes, de testes DETECTA, de falsos alarmes e de instáveis.

## Verificação do script (pilotos)

| Suíte | Esperado | Obtido |
|---|---|---|
| P01 e P02: testes originais do projeto | detecta, pelo teste gatilho | ✓ detecta, só o teste gatilho |
| P01 e P02: suíte trivial (só constrói o objeto) | não detecta | ✓ não detecta |
| P01: suíte que não compila | compilação falha | ✓ marcada como não compila |
| P01: um assert errado e um certo | 1 falso alarme, não detecta | ✓ |
