# LLM study reporting checklist, as applied

Checklist of the course AL0342 (2026/2), reduced by the course from Baltes et al., *Guidelines for Empirical Studies in Software Engineering involving Large Language Models*, Empirical Software Engineering 32:64, 2026 (doi:10.1007/s10664-026-10922-3). **O** = mandatory; **R** = recommended (may be waived with a justification).

Section numbers refer to the final article: 1 Introduction; 3.1 Research Design; 3.2 Benchmark Selection; 3.3 Scope and Selection of Defects4J Bugs; 3.4 Evaluation Metrics; 3.5 Manual Test Design Protocol; 3.6 AI Test Generation Protocol; 3.7 Runs and Stability; 3.9 Scope and Exclusions; 3.10 Threats to Validity; 4 Changes from the Original Plan; 5 Results; 6 Discussion; 7 Limitations and Future Work.

Two LLMs took part in the study. **Gemini 3.8 Flash** is the object of the experiment: it generated the 12 test suites of the AI condition. **Claude** (Anthropic) supported the curator: it wrote the experiment scripts, applied the mechanical compilation fixes within a closed list, proposed a classification of the false-alarm causes (checked by a team member), anonymized the data, documented this repository, checked that the reference DOIs resolve, and drafted parts of the article. The items below refer to Gemini unless they say otherwise.

| # | Item | Type | Status | Where / why |
|---|---|---|---|---|
| **Introduction** | | | | |
| 1 | Declare that an LLM was used, which one, for which task, and in which part of the research | O | Applied | Section 1 (objective), for both Gemini and Claude; Declaration of AI Use |
| 2 | Purpose of the use and expected benefit | R | Applied | Section 1: test whether a widely available model can replace part of the manual effort of test design without losing fault detection |
| **Methodology: model and configuration** | | | | |
| 3 | Exact model or tool name, with version | O | Applied | 3.6: `gemini-3.8-flash`, stable version, Google AI Studio |
| 4 | Date of data collection | O | Applied | 3.6 and 3.8: 14 November 2026 (pilot on 13 November) |
| 5 | Every configured parameter that affects the output | O | Applied | 3.6: thinking level High, output length 65,536 tokens, tools and grounding disabled, no system instructions |
| 6 | Values left at default, and justification of the model choice | R | Applied | 3.6: temperature left at the default because the interface does not expose it; Table 2: model available to the three operators with a free tier |
| 7 | If the model is commercial, what this limits in reproduction | R | Applied | 3.6 and Section 7: a proprietary model may change over time |
| **Methodology: architecture and prompts** | | | | |
| 8 | Describe the architecture: what the LLM does and what the rest of the system does | O | Applied | 3.6: the operator pasted the prompt, attached the class as `.txt`, and saved the single code block of the answer; no other system around the model |
| 9 | Publish all prompts as supplementary material, with representative examples in the body | O | Applied | Full prompt in `docs/prompt_ia.txt`; excerpt in 3.6 |
| 10 | Zero-shot, one-shot, or few-shot | O | Applied | 3.6: zero-shot, single turn, new conversation per execution |
| 11 | If the model was called directly through the API without pre- or post-processing, say so | O | Not applicable | The model was used through the AI Studio web interface, not the API (3.6) |
| 12 | Context files and configurations (CLAUDE.md, AGENTS.md, rules, subagents) | O | Not applicable for Gemini | No context file, rule, or system instruction was used (3.6). Claude was not an object of the study |
| 13 | How the prompts were developed and why | R | Applied | 3.6: written by the operators, fixed in a pilot on a fault outside the sample, where only the answer format changed |
| **Methodology: session traces** | | | | |
| 14 | Publish the full interaction log | R | Partially applied | The generated code of every run is archived with its SHA-256 hash, but the operators saved only the code block, not the full answer (Table 2, 14 Nov) |
| 15 | Publish the execution trace (tools called, arguments, returns, order) | R | Not applicable | No tool or agent was used: one prompt and one answer per execution |
| **Results: benchmark, metrics, and repetitions** | | | | |
| 16 | Justify each benchmark and metric and discuss its validity | O | Applied | 3.2 (Defects4J instead of mutation), 3.4 (LCov, BCov, FDR), 3.10 (construct validity) |
| 17 | Operational definition of what each metric measures | R | Applied | 3.4: per-test classification and detection rule |
| 18 | How cases were sampled and what this limits | R | Applied | 3.3 (criteria C1 to C6, seeded draw) and Section 7 |
| 19 | Limits of the benchmark | R | Applied | 3.10 (external validity) and Section 7 |
| 20 | Repeat executions and report the distribution | R | Not applied | One execution per fault, decided during the AI session for lack of time and team availability (3.7, Table 2); listed as a limitation and future work (Section 7) |
| 21 | Justify the number of repetitions | R | Not applied | Same reason as item 20 (3.7) |
| 22 | Error analysis: categorize the failures and give their frequencies | R | Applied | Table 5 (causes of the 12 non-detections) and the false-alarm causes (Section 6); exploratory, defined after the results (Table 2) |
| 23 | Open-weight model as a baseline and agreement between models | R | Not applied | Running a capable model locally required hardware the team did not have, and the members' computers differed (3.6); listed in Section 7 |
| **Results: human validation** | | | | |
| 24 | Define the construct that the person and the model judge | O | Applied | Table 5: categories A, B, and C (Section 6 and `docs/roteiro_classificacao_t4.md`); false-alarm causes (Section 6, `privado/medicao/diagnostico/RESUMO.md`) |
| 25 | Share the instruments created (form, scale, classification guide) | O | Applied | `docs/roteiro_classificacao_t4.md` |
| 26 | Aggregation rule when there is more than one human rater | R | Applied | Divergences resolved by a third member with a stated rule (Section 6, 3.10, roteiro) |
| 27 | Report agreement with a declared threshold | R | Applied | 7 of 12, Cohen's κ = 0.31, Krippendorff's α = 0.30, below the 0.667 threshold; the B/C split is reported as descriptive only (Section 6, 3.10) |
| **Limitations** | | | | |
| 28 | Training leakage | O | Applied | 3.10 (construct validity): F04, where the interface displayed the original test class as a source |
| 29 | Non-determinism and what was done about it | O | Applied | 3.7 and 3.10: one generation per fault; fixed configuration and prompt, outputs frozen on arrival, every test run three times per version |
| 30 | Construct validity | O | Applied | 3.10 |
| 31 | External validity: one model, one language, one domain, commercial model that changes over time | O | Applied | 3.10 and Section 7 |
