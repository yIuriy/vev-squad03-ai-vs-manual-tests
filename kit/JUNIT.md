# Cola rápida: JUnit 4.12

Versão disponível no kit: **JUnit 4.12**, com Java 11. Não existe `assertThrows` (só a partir do 4.13).

## Estrutura

```java
package org.exemplo;                      // mesmo pacote da classe sob teste

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class F00Test {

    private Exemplo obj;

    @Before                               // roda antes de cada teste
    public void prepara() {
        obj = new Exemplo();
    }

    @Test
    public void somaDoisPositivos() {     // um comportamento por método
        assertEquals(5, obj.soma(2, 3));
    }
}
```

## Asserts

| Assert | Uso |
|---|---|
| `assertEquals(esperado, obtido)` | Igualdade (usa `equals`) |
| `assertEquals(esperado, obtido, delta)` | `double` e `float`: **sempre com tolerância**, por exemplo `1e-9` |
| `assertNotEquals(a, b)` | Diferentes |
| `assertTrue(cond)` / `assertFalse(cond)` | Condição booleana |
| `assertNull(x)` / `assertNotNull(x)` | Nulo / não nulo |
| `assertSame(a, b)` / `assertNotSame(a, b)` | Mesma referência / referências diferentes |
| `assertArrayEquals(esperado, obtido)` | Arrays (para `double[]`, passe o delta) |
| `fail("mensagem")` | Falha o teste na hora |

Todos aceitam uma mensagem como primeiro argumento: `assertEquals("soma errada", 5, r)`.

## Exceções esperadas

```java
@Test(expected = IllegalArgumentException.class)
public void rejeitaNegativo() {
    obj.raiz(-1);
}
```

Para verificar também a mensagem ou o estado depois da exceção:

```java
@Test
public void rejeitaNegativoComMensagem() {
    try {
        obj.raiz(-1);
        fail("deveria lançar IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        assertTrue(e.getMessage().contains("negativo"));
    }
}
```

## Tempo limite

```java
@Test(timeout = 1000)                     // falha se passar de 1 segundo
public void terminaRapido() { ... }
```

## Métodos herdados

A classe sob teste pode ter métodos públicos **herdados da classe mãe**, que não aparecem no arquivo dela, mas fazem parte da API. Para ver todos: escreva o nome do objeto, um ponto e aperte **`Ctrl+Espaço`** (por exemplo `obj.` + `Ctrl+Espaço`). A classe mãe aparece na declaração `extends` da classe.

## Lembretes

- **Um comportamento por método**, com nome que diga o que é verificado.
- Pense em **valores-limite** (0, 1, -1, vazio, `null`, máximo e mínimo do tipo), **caminhos normais** e **casos de erro**.
- Métodos que declaram exceção verificada: coloque `throws Exception` na assinatura do teste.
