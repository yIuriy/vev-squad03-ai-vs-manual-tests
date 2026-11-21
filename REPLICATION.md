# Replication Guide

This guide has three levels. Pick the one that fits your goal:

1. **Recompute the tables** from the published data: Python only, seconds.
2. **Re-measure the 24 frozen suites**: Docker, checks that coverage and detection come out the same.
3. **Run the experiment again**, with new testers or another AI model: the full pipeline, from fault selection to the final tables.

Commands assume the repository root as the working directory. Scripts and protocols are in Portuguese; the [README](README.md) has a glossary.

## Requirements

- Python 3.9 or later (standard library only).
- Docker, for levels 2 and 3. Everything that needs Java, Defects4J or JaCoCo runs inside the image built from [env/Dockerfile](env/Dockerfile): Ubuntu 22.04, OpenJDK 11, Defects4J v3.0.1, JaCoCo 0.8.12.
- On Windows, use Git Bash or WSL. In Git Bash, prefix `docker run` with `MSYS_NO_PATHCONV=1` so the `/work` path is not rewritten.

Build the image once:

```bash
docker build -t vev-d4j env
```

Every container command below has the form:

```bash
docker run --rm -v "${PWD}:/work" vev-d4j <command>
```

## Level 1: recompute the tables

```bash
python scripts/tabelas.py
```

The script reads `privado/medicao/resultados_finais.csv`, `privado/medicao/resultados_rodada1_bruta.csv` and `privado/revisado/revisao.csv` and prints Table 3 (per fault) and Table 4 (per condition). To rebuild `resultados_finais.csv` itself from the two raw measurement rounds:

```bash
python scripts/consolidar.py privado/medicao/resultados_rodada1_bruta.csv privado/medicao/resultados_rodada2_revisadas.csv
git diff --stat privado/medicao/resultados_finais.csv
```

Round 1 measured the 24 suites as delivered. Round 2 re-measured the 9 suites changed by the mechanical review; the last round wins. `consolidar.py` also counts a suite with no active `@Test` method as 0 tests instead of 1 false alarm (see its docstring). An empty diff means the result matched.

## Level 2: re-measure the frozen suites

The 24 suites that entered the results are in `privado/medicao/entrada/<fault>/<condition>/`, one `.java` file each, with origin and SHA-256 in `privado/medicao/entrada/manifesto.csv`. To check the files against the manifest:

```bash
python -c "import csv,hashlib;[print('OK' if hashlib.sha256(open(r['entrada'],'rb').read()).hexdigest()==r['sha256'] else 'DIFFERENT', r['entrada']) for r in csv.DictReader(open('privado/medicao/entrada/manifesto.csv'))]"
```

The F08 human suite reports DIFFERENT: its content changed during anonymization ([ANONYMIZATION.md](ANONYMIZATION.md)).

Measure each suite. For each fault, `scripts/medir.sh` checks out the buggy and the fixed version with Defects4J, compiles the suite against both, runs every test in isolation 3 times on each version with JaCoCo restricted to the class under test, and prints one CSV line:

```bash
docker run --rm -v "${PWD}:/work" vev-d4j bash -c 'for f in F01 F02 F03 F04 F05 F06 F07 F08 F09 F10 F11 F12; do for c in humano ia; do bash scripts/medir.sh $f privado/medicao/entrada/$f/$c privado/medicao/resultados; done; done' > remedicao.csv
python scripts/consolidar.py remedicao.csv
git diff privado/medicao/resultados_finais.csv privado/medicao/resultados
python scripts/tabelas.py
```

Per-test outcomes go to `privado/medicao/resultados/<fault>/<condition>/testes.csv`. Each test is classified as:

| Class | Rule |
|---|---|
| `DETECTA` | fails on the buggy version and passes on the fixed version, in all repetitions |
| `FALSO_ALARME` | fails on the fixed version in all repetitions |
| `INSTAVEL` | outcome changes between repetitions |
| `PASSA` | passes on both versions |
| `NAO_EXECUTADO` | ignored, or the test class did not load |

A suite detects the fault if at least one test is `DETECTA`. Coverage in the article is measured on the fixed version.

Complementary diagnostics (not part of the tables): `scripts/diagnosticar.sh <fault> <condition>` records the failure message of every test not classified as `PASSA` and whether the suite executed the lines changed by the patch; `python scripts/resumo_diagnostico.py` consolidates the 24 suites. Our output and its interpretation are in `privado/medicao/diagnostico/RESUMO.md`.

```bash
docker run --rm -v "${PWD}:/work" vev-d4j bash -c 'for f in F01 F02 F03 F04 F05 F06 F07 F08 F09 F10 F11 F12; do for c in humano ia; do bash scripts/diagnosticar.sh $f $c; done; done'
python scripts/resumo_diagnostico.py
```

Expected differences: the F08 human suite (anonymized test data) may change the coverage of that suite slightly; JaCoCo `.exec` files are binary and change on every run. Detection and coverage of the other 23 suites should match exactly.

## Level 3: run the experiment again

The steps below are the pipeline we followed, in order. The protocol for each step is in `docs/`, and `privado/log_curadoria.md` records what happened in our run, including every deviation. Keep a log of your own in the same way.

### 3.1 Select the faults ([docs/protocolo_selecao.md](docs/protocolo_selecao.md))

```bash
docker run --rm -v "${PWD}:/work" vev-d4j bash scripts/tamanho_classes.sh Lang Math > privado/tamanho_classes.csv
```

Size of the modified class of every Lang and Math fault. Faults whose single modified class has at most 300 NCLOC (and meet the other criteria of the protocol) form `privado/elegiveis.csv`.

```bash
python scripts/sorteio.py privado/elegiveis.csv > privado/ordem_sorteio.csv
```

Random validation order, per project, with the fixed seed 7202620. Use a new seed for a new sample and record it before drawing.

```bash
docker run --rm -v "${PWD}:/work" vev-d4j bash scripts/validar.sh Lang 51
```

Validates one fault (criterion C5): the fault-triggering tests of Defects4J must fail on the buggy version and pass on the fixed version, twice each. Validate in draw order, appending to `privado/validacao.csv`, until 6 faults per project are approved. List them in `privado/amostra_sem_codigo.csv`, then assign the codes F01–F12:

```bash
python scripts/codigos.py privado/amostra_sem_codigo.csv > privado/amostra.csv
```

The pilot faults used for training (P01, P02) are in `privado/piloto.csv` and are not part of the results.

### 3.2 Build the kits ([docs/neutralizacao_kit.md](docs/neutralizacao_kit.md))

```bash
docker run --rm -v "${PWD}:/work" vev-d4j bash scripts/gerar_kit.sh /work/kit /work/privado/kit_remocoes.tsv
```

For each fault: the buggy version of the class under test, with comments that could reveal the fault removed by `scripts/Neutralizar.java` (each removal listed in `kit_remocoes.tsv`), the compiled project as `lib/dependencias.jar`, JUnit, a test skeleton and instructions (`docs/instrucoes_kit.md`, `docs/junit_cola.md`, `docs/perfil_vscode.md`). Freeze the kit with `sha256sum` (`kit/SHA256SUMS`).

### 3.3 Assign faults and prepare the AI condition ([docs/protocolo_ia.md](docs/protocolo_ia.md))

```bash
python scripts/distribuir.py privado/amostra.csv kit privado/distribuicao
```

Draws, with the protocol seed, which tester writes the human suite of each fault and which operator runs the AI on it (never the same person), plus the order of each tester's faults. Produces `distribuicao.csv`, `ordem.csv`, one zip per fault per person and a warm-up zip. Edit the participant list in the script for your own team. The AI zips carry the fixed prompt (`docs/prompt_ia.txt`); the model and its settings are in `docs/configuracao_ia.md`.

### 3.4 Run the sessions and freeze each delivery

Human condition: each tester gets one zip at a time, has 30 minutes per fault, writes the suite in a VS Code profile with no AI features, and does not run it. AI condition: the operator submits the prompt with the kit to the model, once per fault, and saves the returned class without editing it.

Freeze every file as soon as it arrives:

```bash
python scripts/receber.py F05 humano T1 F05Test.java
python scripts/receber.py F05 ia execucao1 F05Test.java
```

This copies the file to `privado/entregas/<condition>/<fault>/<label>/` and appends date, time and SHA-256 to `privado/entregas/registro.csv`. A frozen delivery is never overwritten.

### 3.5 Mechanical review ([docs/protocolo_medicao.md](docs/protocolo_medicao.md))

Suites that do not compile may be fixed only within a closed list of categories: `import`, `cast`, `assinatura` (signature), `throws`, `pontuacao` (punctuation). A test that cannot be fixed within these categories is commented out (`metodo_comentado`). The reviewer cannot be the author of the suite.

```bash
python scripts/revisar.py preparar F03 humano T1 curador
python scripts/revisar.py registrar F03 humano T1 curador import "adicionado import java.util.List"
python scripts/revisar.py congelar F03 humano T1 curador
```

Each correction is one line in `privado/revisado/revisao.csv`, with the hashes before and after.

### 3.6 Measure and consolidate

```bash
python scripts/preparar_medicao.py
```

Copies the last frozen version of each suite (reviewed if it was reviewed, delivered otherwise) to `privado/medicao/entrada/` and writes `manifesto.csv`. Then measure as in level 2, once for the suites as delivered (round 1) and once more for the reviewed suites (round 2), and consolidate:

```bash
python scripts/consolidar.py rodada1.csv rodada2.csv
python scripts/tabelas.py
```

## Where our run deviated from the plan

The article (Table 2) and `privado/log_curadoria.md` list every deviation. The ones that matter most for a replication:

- One AI generation per fault instead of the three planned.
- The mechanical review was done after round 1, with the closed list of categories added to the measurement protocol as an amendment.

## Anonymization

If you publish your own run, anonymize the participants before the first public commit. [ANONYMIZATION.md](ANONYMIZATION.md) describes what we did.
