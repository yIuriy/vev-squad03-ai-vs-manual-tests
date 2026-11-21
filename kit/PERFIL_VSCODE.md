# Perfil padrão do VS Code

Todos os testadores escrevem as suítes no mesmo ambiente: um IDE com **feedback do compilador e verificação de sintaxe**, sem ferramentas de IA, sem analisadores de código e sem execução de testes (artigo, seção 3.5).

## Extensões

| Extensão | Situação |
|---|---|
| **Language Support for Java(TM) by Red Hat** (`redhat.java`) | **Obrigatória.** Dá o compilador, a sintaxe e o autocompletar básico da linguagem |
| Pacote de idioma Português (`MS-CEINTL.vscode-language-pack-pt-BR`) | Opcional |
| Qualquer outra | **Proibida** |

Exemplos do que fica de fora e por quê:

- **GitHub Copilot, Copilot Chat, Codeium, Tabnine, Continue, Cline, Claude e semelhantes:** geram código com IA.
- **IntelliCode e o "Extension Pack for Java":** o pacote instala o IntelliCode, que sugere código por IA, e o Test Runner.
- **Test Runner for Java e Debugger for Java:** permitem executar os testes.
- **SonarLint (SonarQube for IDE), SpotBugs, PMD e Checkstyle:** analisadores que podem apontar o defeito.
- **Oracle Java (`Oracle.oracle-java`):** outro servidor de linguagem, com dicas e execução próprias.

## Como criar

1. No VS Code: ⚙️ (canto inferior esquerdo) → **Perfis** → **Novo Perfil**. Nome: `Experimento`. Em "Copiar de", escolha **Nenhum**.
2. Com o perfil `Experimento` ativo, instale **só** a extensão `redhat.java`. Pelo terminal:
   ```
   code --profile "Experimento" --install-extension redhat.java
   ```
3. Ainda no perfil: `Ctrl+Shift+P` → `Preferences: Open User Settings (JSON)` e deixe o arquivo exatamente assim:
   ```json
   {
       "chat.disableAIFeatures": true,
       "editor.inlineSuggest.enabled": false,
       "java.compile.nullAnalysis.mode": "disabled",
       "redhat.telemetry.enabled": false
   }
   ```
4. É preciso ter um JDK instalado (11 ou mais recente).

Cada pasta de falha do kit já traz um `.vscode/settings.json` que configura o projeto (fontes, bibliotecas, nível Java 11) e desliga os avisos (warnings) do compilador, para que só erros reais apareçam.

## Como conferir

Abra uma pasta de falha do kit (por exemplo `P01/`) no perfil `Experimento`:

- [ ] Na lista de extensões instaladas aparece só `redhat.java` (e o idioma, se instalado).
- [ ] `Ctrl+I` não abre nada, e não há ícone de chat ou Copilot.
- [ ] Ao digitar, não aparece texto cinza sugerindo código.
- [ ] O arquivo de teste e a classe abrem **sem sublinhados vermelhos**.
- [ ] Ao escrever algo errado no teste (por exemplo `int x = "a";`), aparece um erro vermelho. Ou seja, o compilador funciona.
- [ ] Não aparece botão "Run Test" nem "Debug Test" acima dos métodos de teste.
