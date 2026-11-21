# Anonymization of participant data

The data in this repository was anonymized before publication. No file links an identified person to their own performance in the experiment.

## What was done

- **Testers** were replaced by the codes **T1 to T5**, assigned by a non-reproducible shuffle that matches neither alphabetical order nor the author order of the article. The three AI operators are also testers and keep the same T code, so that [scripts/distribuir.py](scripts/distribuir.py) still reproduces exactly the same assignment of faults (checked: `distribuicao.csv` and `ordem.csv` are identical).
- **The curator** appears only as "curador".
- The codes were applied to file **contents** (delivery and review records, assignments, measurement manifest, curation log, documents) and to **folder and file names** (`entregas/humano/<fault>/<code>/`, assignment zips).
- **Attendance reports** (`privado/evidencias/*_presenca_meet.csv`) keep only the code and the times. The original Google Meet reports, with names and e-mail addresses, are not published.
- **Gender markers** tied to the codes were neutralized in the curation log (Portuguese marks gender in pronouns, articles and nouns).
- The table linking people to codes **is not part of the repository** and is kept by the curator.
- The earlier commit history, which contained names, was discarded: this repository starts from an already anonymized snapshot.

The article's author list (and [CITATION.cff](CITATION.cff)) names the squad members, who were also the participants. Because the codes were shuffled, that list does not reveal which code belongs to whom.

## Files whose content changed

In almost every file, only people's names in records and documents changed. For the files below, this affects the SHA-256 recorded in the curation log, which refers to the original (non-anonymized) file:

| File | What changed |
|---|---|
| `privado/entregas/humano/F08/T2/F08Test.java` (and the copies in `privado/recebidos/` and `privado/medicao/entrada/F08/humano/`) | The suite used its author's first name as test data in two strings. The name was replaced by the code `T2`, consistently in the input and in the expected value of each test. Anonymized SHA-256: `bbedc01164a00bba…`; the original hash (`0c8ccbe4f626fa03…`) remains in the log and in `privado/medicao/entrada/manifesto.csv`. |
| `privado/distribuicao/ia/*.zip`, `privado/distribuicao/piloto_ia.zip` | The copy of the AI protocol inside each zip (`PROTOCOLO_IA.md`) named the three AI operators; the names were replaced by codes. Entry names, dates and all other entries are unchanged, but the zip bytes differ from the zips sent during the experiment. |
| `privado/entregas/registro.csv`, `privado/revisado/revisao.csv`, `privado/medicao/entrada/manifesto.csv`, `privado/distribuicao/*.csv`, `privado/log_curadoria.md`, `privado/medicao/revisao_pendente.md`, `privado/evidencias/*.csv` | Names replaced by codes. |

The results do not depend on the names: [scripts/consolidar.py](scripts/consolidar.py) reproduces `privado/medicao/resultados_finais.csv` byte for byte from the anonymized data.
