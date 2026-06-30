# Barbearia E2E

Aplicação full-stack: **Spring Boot** + **React**. Dados em memória (sem banco de dados).

## Pré-requisitos

- **Java 17+** e **Maven** (backend)
- **Node.js 18+** (frontend)

## Scripts de execução (Windows)

Na raiz do projeto existem três scripts `.bat`:

### `start.bat` — Aplicação completa (recomendado)

Sobe frontend e backend juntos em **uma única porta**.

1. Instala dependências do frontend (`npm install`), se necessário
2. Gera o build de produção do React (`npm run build`)
3. Copia os arquivos para `backend/src/main/resources/static`
4. Inicia o Spring Boot na porta **8080**

**Acesso:** http://localhost:8080

### `start-backend.bat` — Somente backend

Inicia apenas a API Spring Boot (porta **8080**), sem interface web.

```bash
start-backend.bat
```

**Acesso:** http://localhost:8080/api

Use quando quiser rodar o frontend separadamente com hot reload.

### `start-frontend.bat` — Somente frontend (desenvolvimento)

Instala dependências (`npm install`), se necessário, e inicia o Vite em modo dev (porta **5173**).

```bash
start-frontend.bat
```

**Acesso:** http://localhost:5173

O Vite faz proxy das chamadas `/api` para `http://localhost:8080`. **O backend precisa estar rodando** (`start-backend.bat` ou `mvn spring-boot:run`).

### `stop.bat` — Parar aplicação

Encerra processos pelas portas **8080** (backend) e **5173** (frontend).

**Menu interativo** (duplo clique ou sem argumentos):
```bash
stop.bat
```

**Por argumento:**
```bash
stop.bat backend    # ou: back, b
stop.bat frontend   # ou: front, f
stop.bat all        # ou: both, a
```

Atalhos:
```bash
stop-backend.bat    # equivale a stop.bat backend
stop-frontend.bat   # equivale a stop.bat frontend
```

---

## Desenvolvimento com hot reload

Terminal 1:
```bash
start-backend.bat
```

Terminal 2:
```bash
start-frontend.bat
```

Acesse: **http://localhost:5173**

---

## Documentação Swagger (OpenAPI)

Com o **backend em execução**, acesse:

| Recurso | URL |
|---------|-----|
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs |

### Autenticação nos endpoints admin

1. Execute `POST /api/admin/login` com:
   ```json
   { "usuario": "admin", "senha": "admin123" }
   ```
2. Copie o `token` da resposta
3. Clique em **Authorize** no Swagger UI
4. Informe: `Bearer {seu-token}`

---

## Credenciais Admin

| Usuário | Senha |
|---------|-------|
| admin   | admin123 |

---

## Funcionalidades

**Cliente:** Home, agendamento (Cabelo R$100, Barba R$100, Combo R$180), pagamento, editar/cancelar até 2h antes, horários 09h–20h, avaliação.

**Admin:** Agendamentos, clientes, serviços/preços, barbeiros, faturamento mensal, cancelamentos, validar avaliações, ranking.

---

## Estrutura

```
barbearia_e2e/
├── backend/          # Spring Boot MVC + Swagger
├── frontend/         # React + Vite
├── start.bat         # Build + backend (porta 8080)
├── start-backend.bat # API apenas
├── start-frontend.bat# Frontend dev (porta 5173)
├── stop.bat          # Parar backend, frontend ou ambos
├── stop-backend.bat  # Parar backend
└── stop-frontend.bat # Parar frontend
```
