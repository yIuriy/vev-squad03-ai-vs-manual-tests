# Roteiro de classificação das não detecções da IA (Tabela 5 do artigo)

Instrumento usado na classificação das 12 suítes de IA que não detectaram a falha (tarefa T4 do Andamento 5). As definições e o procedimento abaixo reproduzem, sem mudança de conteúdo, o que os dois classificadores receberam no plano de trabalho do grupo em 21/11/2026; a regra de desempate é a que o curador usou em 23/11/2026. A classificação foi definida **depois** de os resultados serem conhecidos e é uma análise exploratória.

## Categorias

| Categoria | Definição |
|---|---|
| **A** | A suíte não executou nenhuma linha alterada pelo patch. |
| **B** | A suíte executou as linhas alteradas pelo patch, mas sem a entrada que dispara a falha. |
| **C** | A suíte usou uma entrada equivalente à do teste gatilho, mas a asserção aceitou o comportamento errado. |

A categoria A não depende de julgamento: sai do diagnóstico automático (`privado/medicao/diagnostico/cobertura_patch.csv`), que mostra, para cada suíte, quantas linhas executáveis do patch ela executou. Só o F08 ficou em A. As outras 11 suítes executaram as linhas do patch, todas (F01, F02, F03, F04, F06, F07, F10, F12) ou parte delas (F05, F09, F11), e foram classificadas em B ou C.

## Procedimento dado aos classificadores

1. Cada classificador trabalhou **separadamente**, sem ver a classificação do outro e sem usar IA.
2. Para cada uma das 11 suítes, comparar a suíte de IA (`privado/medicao/entrada/<falha>/ia/`) com os testes gatilho do Defects4J (`privado/medicao/diagnostico/gatilhos/<falha>.md`).
3. Decidir B ou C pelas definições acima.
4. Entregar a lista ao curador.

Observação registrada por transparência: o plano entregue aos classificadores dizia que o F01 e o F12 têm testes que passam na versão com bug e falham na corrigida (oráculo invertido) e os apontava como casos de C. Os dois classificadores marcaram C nesses dois casos.

## Desempate

O curador resolveu as divergências com uma única pergunta: *algum teste da suíte de IA usa uma entrada equivalente à do teste gatilho?* Se sim, C; se não, B.

## Resultado e concordância

- Registro completo, com as duas classificações e o desempate: `privado/medicao/diagnostico/classificacao_t4.csv`.
- Concordância entre os dois classificadores: 7 de 12 suítes; kappa de Cohen 0,31; alfa de Krippendorff (nominal) 0,30.
- O alfa fica abaixo de 0,667, o limiar abaixo do qual uma classificação não deve sustentar conclusões. Por isso, no artigo, a divisão entre B e C é apresentada só como descritiva. A divisão entre A e "B ou C" vem do diagnóstico automático e não depende dessa concordância (os dois classificadores concordam nela em 12 de 12).
- Resultado final: A 1 (F08); B 6 (F02, F05, F06, F09, F10, F11); C 5 (F01, F03, F04, F07, F12).
