# 📈 InvestTracker API - Gerenciador de Rentabilidade de Ativos

API RESTful robusta desenvolvida para monitoramento de investimentos em tempo real. O projeto foca em **Integração de Sistemas**, **Arquitetura Limpa (DDD)** e **Performance (Cache)**. O diferencial deste sistema é a implementação do padrão **Anti-Corruption Layer (ACL)**, protegendo o núcleo do domínio contra mudanças nas APIs externas de cotação (Brapi/B3).

## 📋 Índice

1. [Visão Geral e Arquitetura](#-visão-geral-e-arquitetura)
2. [Tecnologias Utilizadas](#-tecnologias-utilizadas)
3. [Instalação e Execução (Docker)](#-instalação-e-execução-passo-a-passo)
4. [Documentação da API (Swagger)](#-documentação-interativa-swagger)
5. [Guia de Uso (Exemplos Práticos)](#-guia-de-uso-exemplos-práticos)
6. [Estrutura de Dados e Cache](#-estrutura-de-dados-e-cache)

---

## 🏛️ Visão Geral e Arquitetura

O sistema é dividido em camadas estritas baseadas no **Domain-Driven Design (DDD)** para garantir o desacoplamento:

* **Interfaces (Controller):** Ponto de entrada REST. Recebe parâmetros (Ticker, Quantidade, Preço), cria o DTO e chama o Caso de Uso.
* **Application:** Orquestra o fluxo. Não contém regras de negócio complexas, apenas delega.
* **Domain (Core):** Coração da regra de negócio.
    * *Service:* Calcula PnL (Lucro/Prejuízo) e ROI.
    * *Model:* Entidades puras (`Asset`, `Position`).
    * *Port:* Interfaces que definem *o que* o sistema precisa (ex: `QuoteProvider`), sem saber *como* obter.
* **Infrastructure (Adapter):** Implementação técnica.
    * Consome a API externa via **WebClient**.
    * Gerencia o Cache no **Redis**.

### Regras de Negócio e Patterns

* **Isolamento (ACL):** O domínio nunca toca na API externa diretamente. Um *Adapter* converte o JSON "sujo" da API externa para o objeto de domínio limpo.
* **Performance (Caching):** Para evitar *Rate Limiting* e latência, as cotações são salvas no Redis com TTL (Time-to-Live). Se a cotação já existe no cache, a API não chama a B3.
* **Matemática Financeira:** Uso estrito de `BigDecimal` para evitar erros de arredondamento em cálculos monetários.
* **Resiliência:** Preparado para lidar com falhas na API terceira (Design pattern Circuit Breaker ready).

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 21 (LTS)
* **Framework:** Spring Boot 3.x
* **Cliente HTTP:** Spring WebFlux (WebClient - Non-blocking)
* **Cache:** Spring Data Redis
* **Infraestrutura:** Docker & Docker Compose
* **Documentação:** SpringDoc OpenAPI (Swagger UI)
* **Build Tool:** Gradle (Groovy DSL)
* **Ferramentas:** Lombok.

---

## 🐳 Instalação e Execução (Passo a Passo)

A aplicação é **Dockerizada**. Isso significa que você não precisa instalar Java ou Redis na sua máquina, apenas o Docker.

### Pré-requisitos

* **Docker Desktop** instalado e rodando.

### Como Rodar

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/Joaovirone/invest-tracker.git](https://github.com/Joaovirone/invest-tracker.git)
    cd invest-tracker
    ```

2.  **Gere o executável (Build):**
    Como usamos Gradle, precisamos gerar o `.jar` antes de subir o container.
    * *Windows (PowerShell):*
        ```powershell
        ./gradlew clean build -x test
        ```
    * *Linux/Mac:*
        ```bash
        ./gradlew clean build -x test
        ```

3.  **Suba o ambiente com Docker Compose:**
    Este comando irá criar a imagem da API, baixar a imagem do Redis e conectar ambos numa rede isolada.
    ```bash
    docker-compose up --build
    ```

4.  **Aguarde a inicialização:**
    O processo é rápido. Aguarde até ver a mensagem no terminal: `Started InvestTrackerApplication in X seconds`.

---

## 📚 Documentação Interativa (Swagger)

Com a aplicação rodando, você pode testar todos os endpoints e ver os esquemas de dados visualmente.

👉 **Acesse:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

> **Nota:** Diferente da Fintech API, esta API é pública e focada em consulta, portanto não exige autenticação via Token para os testes iniciais.

---

## 🧪 Guia de Uso (Exemplos Práticos)

Aqui estão os cenários para validar o funcionamento do sistema e a integração.

### Cenário 1: Consultar Lucro (Profit)

Vamos simular que compramos Petrobras (PETR4) barato e queremos ver o lucro atual.

* **Endpoint:** `GET /api/portfolio/{ticker}`
* **Parâmetros (Query):**
    * `amount`: 100 (ações)
    * `price`: 20.00 (custo médio)
* **Exemplo de URL:**
    `http://localhost:8080/api/portfolio/PETR4?amount=100&price=20.00`

**Resposta Esperada (JSON):**
```json
{
  "asset": {
    "ticker": "PETR4",
    "quantity": 100,
    "averagePrice": 20.00
  },
  "currentPrice": 38.50,
  "totalValue": 3850.00,
  "profitOrLoss": 1850.00,
  "roi": 92.50,
  "formattedRoi": "92.50%"
}
