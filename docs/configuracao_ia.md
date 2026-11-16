# Configuração congelada da condição de IA

> **Status: CONGELADO em 2026-11-13**, antes da S3. O SHA-256 deste arquivo e do prompt está no log de curadoria. Nada aqui muda durante as execuções.

## Modelo

| Item | Valor |
|---|---|
| Ferramenta | Google AI Studio (interface web, chat) |
| Modelo | **Gemini 3.8 Flash** |
| ID do modelo | `gemini-3.8-flash` |
| Versão | Estável (não preview) |

## Configuração (Run settings)

| Opção | Valor |
|---|---|
| Thinking level | **High** |
| System instructions | Vazio (não usado) |
| Structured outputs | Desligado |
| Code execution | Desligado |
| Function calling | Desligado |
| Grounding with Google Search | Desligado |
| Grounding with Google Maps | Desligado |
| URL context | Desligado |
| Media resolution | Default |
| Safety settings | Padrão (não alterado) |
| Stop sequence | Nenhuma |
| Output length | 65536 |
| Temperatura | Não aparece no painel para este modelo; fica o padrão do modelo |

## Prompt

- Texto: [prompt_ia.txt](prompt_ia.txt), escrito pelos operadores de IA (Dyonathan, com aprovação de Rafaela e Sidnei)
- SHA-256 do modelo de prompt: `40b3e70e7bfa442cc73db8f6185ca382578944b2353ca051ace64f6477954664`
- Único campo variável: `{CODIGO}`, já substituído pelo curador no `PROMPT.txt` de cada zip da S3
- A classe sob teste vai como anexo `.txt`, idêntica ao arquivo dos testadores

## Validação no piloto (P02, fora da amostra)

Resposta do operador Sidnei com esta configuração: compila, `public class P02Test` no pacote correto, só JUnit 4, 30 testes. Procedimento completo em [protocolo_ia.md](protocolo_ia.md).

## Antes de cada sessão

Cada operador confere, no próprio AI Studio, que o painel está **igual a esta tabela** e manda um print ao curador na primeira execução do dia.
