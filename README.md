# Human vs. Generative AI JUnit Tests on Real Defects4J Faults

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.24423863.svg)](https://doi.org/10.5281/zenodo.24423863)

Replication package for the article **"Effectiveness of Generative AI in Test Case Generation: A Comparative Study with Manual Test Case Design"** (AL0342 Software Verification and Validation, Universidade Federal do Pampa, Squad 03, 2026).

We ran a controlled, paired experiment on 12 real faults from Defects4J 3.0.1 (6 from Apache Commons Lang, 6 from Apache Commons Math). For each fault, one human tester wrote a JUnit 4 suite in 30 minutes without running it, and Gemini 3.8 Flash generated one suite from a fixed prompt. Every suite was measured in Docker against the buggy and the fixed version of the program, 3 times on each version, for fault detection, line coverage and branch coverage (JaCoCo 0.8.12).

| | Human (12 suites) | AI (12 suites) |
|---|---|---|
| Faults detected | 1/12 | 0/12 |
| Line coverage, mean (median) | 25.9% (15.7%) | 94.7% (98.2%) |
| Branch coverage, mean (median) | 19.4% (8.8%) | 91.5% (93.9%) |
| Tests executed | 48 | 549 |
| False alarms | 8 | 3 |

Run `python scripts/tabelas.py` to recompute these numbers from the published data.

## Language of the artifacts

This README, [REPLICATION.md](REPLICATION.md), [ANONYMIZATION.md](ANONYMIZATION.md) and the license files are in English. The experimental artifacts are in Portuguese, as they were used during the experiment: protocols, instructions given to participants, the AI prompt, the kits, the curation log, script comments and folder names. They were not translated, because translating them would change files whose SHA-256 hashes were recorded when each delivery was frozen.

Glossary of folder and column names:

| Portuguese | English |
|---|---|
| `privado/` | experiment data (kept out of the repository until all sessions ended, to keep the faults secret) |
| `amostra.csv` | fault sample (F01–F12) |
| `elegiveis.csv`, `ordem_sorteio.csv` | eligible faults, random draw order |
| `validacao.csv` | fault validation results |
| `distribuicao/` | assignment of faults to testers and the zips sent to them |
| `entregas/` | suites as delivered (frozen) |
| `revisado/` | suites after the mechanical review (frozen) |
| `recebidos/` | raw files as received |
| `medicao/entrada/`, `medicao/resultados/` | measurement inputs and outputs |
| `evidencias/` | session evidence (anonymized attendance, AI configuration screenshot) |
| `log_curadoria.md` | curation log: every decision, deviation and hash, in chronological order |
| `humano`, `ia` | human condition, AI condition |
| `ensaio`, `piloto` | dry run, pilot (P01, P02; not part of the results) |
| `compila_b`, `compila_f` | compiles against the buggy / fixed version |
| `detecta`, `falsos_alarmes`, `instaveis` | detects the fault, false alarms, flaky tests |
| `linhas_cob/tot`, `branches_cob/tot` | covered / total lines and branches |
| `sim`, `nao` | yes, no |

## Repository structure

| Path | Contents |
|---|---|
| [env/Dockerfile](env/Dockerfile) | Ubuntu 22.04, OpenJDK 11, Defects4J v3.0.1, JaCoCo 0.8.12 |
| [docs/](docs) | Protocols: fault selection (`protocolo_selecao.md`), AI generation (`protocolo_ia.md`, `prompt_ia.txt`, `configuracao_ia.md`), measurement (`protocolo_medicao.md`), kit neutralization, participant instructions, VS Code profile |
| [kit/](kit) | What each tester received for each fault: the class under test with fault-revealing comments removed, a test skeleton and instructions. `SHA256SUMS` freezes it |
| [scripts/](scripts) | Every step of the pipeline, from fault selection to the final tables |
| [privado/](privado) | All experiment data: sample, assignments, frozen suites, review records, raw measurement output, curation log |

## Reproducing the results

The short path re-measures the 24 frozen suites. It requires Docker and Python 3:

```bash
docker build -t vev-d4j env
docker run --rm -v "${PWD}:/work" vev-d4j bash -c 'for f in F01 F02 F03 F04 F05 F06 F07 F08 F09 F10 F11 F12; do for c in humano ia; do bash scripts/medir.sh $f privado/medicao/entrada/$f/$c /tmp/res; done; done' > remedicao.csv
python scripts/consolidar.py remedicao.csv
git diff --stat privado/medicao/resultados_finais.csv
```

An empty diff means the re-measurement matched the published results. [REPLICATION.md](REPLICATION.md) explains this path in detail, the full pipeline (from fault selection to measurement) and how to run a new replication with other testers or another model.

Which file produces which table of the article:

| Article | Source |
|---|---|
| Table 1 (sample) | `privado/amostra.csv` |
| Table 2 (deviations from the plan) | `privado/log_curadoria.md`, amendments in `docs/protocolo_medicao.md` |
| Table 3 (results per fault) | `privado/medicao/resultados_finais.csv` (from `scripts/consolidar.py`) |
| Table 4 (summary per condition) | `python scripts/tabelas.py` |
| Table 5 (causes of the 12 AI non-detections) | `privado/medicao/diagnostico/classificacao_t4.csv` (two independent classifications and the tie-break), using `privado/medicao/diagnostico/gatilhos/` |
| Patch-line execution and false-alarm causes (Discussion) | `privado/medicao/diagnostico/RESUMO.md`, `cobertura_patch.csv`, `falhas.csv` (from `scripts/diagnosticar.sh` and `scripts/resumo_diagnostico.py`) |

Table 5 and the diagnostic files are an **exploratory analysis defined after the results were known** (21 and 23 November 2026), to explain the 0/12 detections of the AI condition; they do not change any number in Tables 3 and 4. The curation log records when each was done.

## Anonymization

Participant data was anonymized before publication. Testers appear only as codes T1 to T5, and no file links an identified person to their performance. The table linking people to codes is not in this repository. The earlier history, which contained names, was discarded, so this repository starts from an anonymized snapshot. [ANONYMIZATION.md](ANONYMIZATION.md) gives the details, including the one test suite whose content changed.

## Use of AI

Gemini 3.8 Flash generated the suites of the AI condition, as part of the experiment (configuration in `docs/configuracao_ia.md`). Claude (Anthropic) assisted the curator with scripts, the mechanical review, data consolidation and documentation. The curation log records each of these uses.

## License

- Code (`scripts/`, `env/`): [MIT](LICENSE).
- Data and documentation (`privado/`, `docs/`, the test suites, this README): [CC BY 4.0](LICENSE-DATA.md).
- Third-party material in `kit/` and the programs under test keep their own licenses. See [NOTICE](NOTICE).

## How to cite

Archived on Zenodo: [doi:10.5281/zenodo.24423863](https://doi.org/10.5281/zenodo.24423863) (all versions; this DOI always resolves to the latest one, and each version has its own DOI on the Zenodo page). See also [CITATION.cff](CITATION.cff).
