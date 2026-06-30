# Barbearia E2E

Aplicação full-stack: **Spring Boot** + **React**. Dados em memória (sem banco).

## Execução rápida (1 comando)

```bash
start.bat
```

Ou manualmente:

```bash
cd frontend && npm install && npm run build
xcopy /E /I /Y frontend\dist backend\src\main\resources\static
cd backend && mvn spring-boot:run
```

Acesse: **http://localhost:8080**

## Desenvolvimento (hot reload)

Terminal 1 — Backend:
```bash
cd backend && mvn spring-boot:run
```

Terminal 2 — Frontend:
```bash
cd frontend && npm install && npm run dev
```

Acesse: **http://localhost:5173** (proxy para API na porta 8080)

## Credenciais Admin

| Usuário | Senha |
|---------|-------|
| admin   | admin123 |

## Funcionalidades

**Cliente:** Home, agendamento (Cabelo R$100, Barba R$100, Combo R$180), pagamento, editar/cancelar até 2h antes, horários 09h–20h, avaliação.

**Admin:** Agendamentos, clientes, serviços/preços, barbeiros, faturamento mensal, cancelamentos, validar avaliações, ranking.
